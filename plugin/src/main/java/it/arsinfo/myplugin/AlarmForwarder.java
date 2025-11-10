package it.arsinfo.myplugin;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import it.arsinfo.myplugin.client.ClientManager;
import it.arsinfo.opennms.client.api.ApiClientService;
import it.arsinfo.opennms.client.api.OpenNMSApiException;
import it.arsinfo.opennms.client.api.model.Ack;
import it.arsinfo.spring.client.model.AckAlert;
import it.arsinfo.spring.client.model.Alert;
import org.opennms.integration.api.v1.alarms.AlarmLifecycleListener;
import org.opennms.integration.api.v1.events.EventForwarder;
import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.immutables.ImmutableEventParameter;
import org.opennms.integration.api.v1.model.immutables.ImmutableInMemoryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AlarmForwarder implements AlarmLifecycleListener {
    private static final Logger LOG = LoggerFactory.getLogger(AlarmForwarder.class);

    private static final String UEI_PREFIX = "uei.opennms.org/mypluginPlugin";
    private static final String SEND_EVENT_FAILED_UEI = UEI_PREFIX + "/sendEventFailed";
    private static final String SEND_EVENT_SUCCESSFUL_UEI = UEI_PREFIX + "/sendEventSuccessful";

    private final MetricRegistry metrics = new MetricRegistry();
    private final Meter eventsForwarded = metrics.meter("eventsForwarded");
    private final Meter eventsFailed = metrics.meter("eventsFailed");

    private final EventForwarder eventForwarder;
    private final ApiClientService onmsService;
    private final it.arsinfo.spring.client.ApiClientService springService;

    public AlarmForwarder(ClientManager manager, EventForwarder eventForwarder) {
        Objects.requireNonNull(manager);
        this.onmsService = manager.getOnmsApiService();
        this.springService = manager.getSpringApiService();
        this.eventForwarder = Objects.requireNonNull(eventForwarder);
    }

    @Override
    public void handleNewOrUpdatedAlarm(Alarm alarm) {
        if (alarm.getReductionKey().startsWith(UEI_PREFIX)) {
            // Never forward alarms that the plugin itself creates
            return;
        }

        // Map the alarm to the corresponding model object that the API requires
        Alert alert = toAlert(alarm);

        System.out.println(alarm);
        // Forward the alarm
        boolean success = springService.sendAlert(alert, alarm.getReductionKey());
        if (success) {
            System.out.println(SEND_EVENT_SUCCESSFUL_UEI);
            eventsForwarded.mark();
        } else {
            System.out.println(SEND_EVENT_FAILED_UEI);
            eventsFailed.mark();
        }
    }

    @Override
    public void handleAlarmSnapshot(List<Alarm> alarms) {
        // pass
    }

    @Override
    public void handleDeletedAlarm(int alarmId, String reductionKey) {
        try {
            onmsService.getAckByAlarmId(alarmId)
                    .acks
                    .forEach(ack ->
                            springService.sendAck(toAckAlert(ack)));
        } catch (OpenNMSApiException e) {
            throw new RuntimeException(e);
        }
        // pass
    }

    public static AckAlert toAckAlert(Ack ack) {
        AckAlert ackAlert = new AckAlert();
        ackAlert.setId(ack.id);
        ackAlert.setAckAction(convert(ack.ackAction));
        ackAlert.setAckType(convert(ack.ackType));
        ackAlert.setAlarmId(ack.refId);
        ackAlert.setAckUser(ack.ackUser);
        ackAlert.setAckTime(ack.ackTime.toInstant());
        return ackAlert;
    }

    public static Alert toAlert(Alarm alarm) {
        Alert alert = new Alert();
        alert.setStatus(toStatus(alarm));
        alert.setAttribute("node", alarm.getNode().getLabel());
        alert.setAttribute("reductionKey", alarm.getReductionKey());
        alert.setDescription(alarm.getDescription());
        alert.setTimestamp(alarm.getLastEventTime().toInstant());
        alert.setAlarmAckUser("admin");
        return alert;
    }

    private static AckAlert.AckAction convert(Ack.AckAction action) {
        return switch (action) {
            case UNSPECIFIED -> AckAlert.AckAction.UNSPECIFIED;
            case ACKNOWLEDGE -> AckAlert.AckAction.ACKNOWLEDGE;
            case UNACKNOWLEDGE -> AckAlert.AckAction.UNACKNOWLEDGE;
            case ESCALATE -> AckAlert.AckAction.ESCALATE;
            case CLEAR -> AckAlert.AckAction.CLEAR;
        };

    }

    private static AckAlert.AckType convert(Ack.AckType type) {
        return switch (type) {
            case UNSPECIFIED -> AckAlert.AckType.UNSPECIFIED;
            case ALARM -> AckAlert.AckType.ALARM;
            case NOTIFICATION -> AckAlert.AckType.NOTIFICATION;
        };

    }

    private static Alert.Status toStatus(Alarm alarm) {
        if (alarm.isAcknowledged()) {
            return Alert.Status.ACKNOWLEDGED;
        }
        return switch (alarm.getSeverity()) {
            case INDETERMINATE, CLEARED, NORMAL -> Alert.Status.OK;
            case WARNING, MINOR -> Alert.Status.WARNING;
            default -> Alert.Status.CRITICAL;
        };
    }

    public MetricRegistry getMetrics() {
        return metrics;
    }
}
