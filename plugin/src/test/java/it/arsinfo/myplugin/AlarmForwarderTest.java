package it.arsinfo.myplugin;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.v1.model.immutables.ImmutableAlarm;
import it.arsinfo.spring.client.model.Alert;
import org.opennms.integration.api.v1.model.immutables.ImmutableNode;

import java.util.Date;

public class AlarmForwarderTest {

    @Test
    public void canConvertAlarmToAlert() {
        Alarm alarm = ImmutableAlarm.newBuilder()
                .setId(1)
                .setReductionKey("hey:oh")
                .setSeverity(Severity.CRITICAL)
                .setNode(ImmutableNode.newBuilder().setId(19).setLabel("prove").build())
                .setLastEventTime(new Date())
                .build();

        Alert alert = AlarmForwarder.toAlert(alarm);

        assertThat(alert.getStatus(), equalTo(Alert.Status.CRITICAL));
    }
}
