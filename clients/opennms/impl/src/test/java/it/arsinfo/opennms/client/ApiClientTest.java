package it.arsinfo.opennms.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.arsinfo.opennms.client.impl.api.DefaultApi;
import it.arsinfo.opennms.client.impl.handler.ApiClient;
import it.arsinfo.opennms.client.impl.handler.ApiException;
import it.arsinfo.opennms.client.impl.model.OnmsAcknowledgmentCollection;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Base64;


public class ApiClientTest {

    private static DefaultApi getClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.setUsername("admin");
        apiClient.setPassword("COdiMI2019!");
        apiClient.setBasePath("http://10.63.138.140:8980/opennms/rest");
        return new DefaultApi(apiClient);
    }

    @Test
    public void canGetAcks() throws ApiException {
        DefaultApi defaultApi = getClient();
        OnmsAcknowledgmentCollection collection = defaultApi.getAcks();
        System.out.println("---getAcks---");
        System.out.println(defaultApi.getAcks());
        System.out.println("---getAcks(2,4)---");
        System.out.println(defaultApi.getAcks(2,4));
        System.out.println("---getAck(17202209)---");
        System.out.println(defaultApi.getAck(17202209));
        System.out.println("---getAcksByAlarmId(147624304)---");
        System.out.println(defaultApi.getAcksByAlarmId(147624304));

    }
}
