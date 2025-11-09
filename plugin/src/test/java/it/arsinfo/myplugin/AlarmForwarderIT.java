package it.arsinfo.myplugin;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import it.arsinfo.myplugin.client.ClientManager;
import it.arsinfo.opennms.client.api.OpenNMSApiException;
import it.arsinfo.opennms.client.api.model.Ack;
import it.arsinfo.opennms.client.api.model.AckCollection;
import it.arsinfo.spring.client.ApiClient;
import it.arsinfo.spring.client.ApiClientService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.opennms.integration.api.v1.events.EventForwarder;
import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.v1.model.immutables.ImmutableAlarm;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlarmForwarderIT {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void canForwardAlarm() throws OpenNMSApiException {
        // Wire it up
        ApiClient apiClient = new ApiClient(wireMockRule.url("/data/v2/alerts"));
        it.arsinfo.opennms.client.api.ApiClientService onmsApiClientService =
                mock(it.arsinfo.opennms.client.api.ApiClientService.class);
        Ack ack = Ack.builder()
                .withAckAction(Ack.AckAction.CLEAR)
                .withRefId(1)
                .withAckTime(new Date())
                .withAckUser("antonio")
                .withId(10)
                .withAckType(Ack.AckType.ALARM)
                .build();
        AckCollection ackCollection = AckCollection
                .builder()
                .withTotalCount(1)
                .withOffset(0)
                .withCount(1)
                .addAcks(ack)
                .build();
        when(onmsApiClientService.getAckByAlarmId(Mockito.anyInt())).thenReturn(ackCollection);
        ApiClientService apiClientService = new ApiClientService(apiClient);
        ClientManager clientManager = mock(ClientManager.class);
        EventForwarder eventForwarder = mock(EventForwarder.class);
        when(clientManager.getSpringApiService()).thenReturn(apiClientService);
        AlarmForwarder alarmForwarder = new AlarmForwarder(clientManager, eventForwarder);

        // Stub the endpoint
        stubFor(post((urlEqualTo("/data/v2/alerts")))
                .willReturn(aResponse()
                        .withStatus(200)));

        // Handle some alarm
        Alarm alarm = ImmutableAlarm.newBuilder()
                .setId(1)
                .setReductionKey("hey:oh")
                .setSeverity(Severity.CRITICAL)
                .build();
        alarmForwarder.handleNewOrUpdatedAlarm(alarm);

        // Verify that the call was made
        await().atMost(15, TimeUnit.SECONDS)
                .catchUncaughtExceptions()
                .until(() -> {
                    verify(1, postRequestedFor(urlPathEqualTo("/data/v2/alerts")));
                    return true;
                });
    }
}
