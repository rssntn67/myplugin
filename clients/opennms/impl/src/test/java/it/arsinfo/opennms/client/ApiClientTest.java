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

    @Test
    public void canGetAcks() throws ApiException {
        ApiClient apiClient = new ApiClient();
        apiClient.setUsername("admin");
        apiClient.setPassword("COdiMI2019!");
        apiClient.setBasePath("http://10.63.138.140:8980/opennms/rest");

        DefaultApi defaultApi = new DefaultApi(apiClient);

        System.out.println(apiClient.getBasePath());

        OnmsAcknowledgmentCollection collection = defaultApi.getAcks();
        System.out.println(collection);

    }
}
