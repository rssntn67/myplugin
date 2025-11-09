package it.arsinfo.myplugin;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import it.arsinfo.spring.client.ApiClientService;
import it.arsinfo.spring.client.model.Alert;
import org.opennms.integration.api.v1.alarms.AlarmLifecycleListener;
import org.opennms.integration.api.v1.events.EventForwarder;
import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.immutables.ImmutableEventParameter;
import org.opennms.integration.api.v1.model.immutables.ImmutableInMemoryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final ApiClientService apiClientService;
    private final EventForwarder eventForwarder;

    public AlarmForwarder(ApiClientService apiClientService, EventForwarder eventForwarder) {
        this.apiClientService = Objects.requireNonNull(apiClientService);
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

        // Forward the alarm
        boolean success = apiClientService.sendAlert(alert, alarm.getReductionKey());
            if (!success) {
                eventsForwarded.mark();
                eventForwarder.sendAsync(ImmutableInMemoryEvent.newBuilder()
                        .setUei(SEND_EVENT_FAILED_UEI)
                        .addParameter(ImmutableEventParameter.newBuilder()
                                .setName("reductionKey")
                                .setValue(alarm.getReductionKey())
                                .build())
                        .addParameter(ImmutableEventParameter.newBuilder()
                                .setName("message")
                                .setValue("ERROR see LOGS")
                                .build())
                        .build());
            } else {
                eventsFailed.mark();
                eventForwarder.sendAsync(ImmutableInMemoryEvent.newBuilder()
                        .setUei(SEND_EVENT_SUCCESSFUL_UEI)
                        .addParameter(ImmutableEventParameter.newBuilder()
                                .setName("reductionKey")
                                .setValue(alarm.getReductionKey())
                                .build())
                        .build());
            }
    }

    @Override
    public void handleAlarmSnapshot(List<Alarm> alarms) {
        // pass
    }

    @Override
    public void handleDeletedAlarm(int alarmId, String reductionKey) {
        // pass
    }

    public static Alert toAlert(Alarm alarm) {
        Alert alert = new Alert();
        alert.setStatus(toStatus(alarm));
        alert.setDescription(alarm.getDescription());
        return alert;
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
