package it.arsinfo.spring.client;

import it.arsinfo.spring.client.model.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletionException;

public class ApiClientService {

    private final ApiClient apiClient;

    private static final Logger LOG = LoggerFactory.getLogger(ApiClientService.class);

    public ApiClientService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public boolean sendAlert(Alert alert, String reductionKey) {
        try {
            apiClient.sendAlert(alert).join(); // Blocks until complete
            LOG.info("Alert sent successfully for alarm with reduction-key: {}", reductionKey);
            return true;
        } catch (CompletionException e) {
            LOG.warn("Sending alert for alarm with reduction-key: {} failed.", reductionKey, e.getCause());
            return false;
        }
    }
}
