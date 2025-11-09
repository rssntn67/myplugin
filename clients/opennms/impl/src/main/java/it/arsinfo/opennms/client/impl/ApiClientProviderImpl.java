package it.arsinfo.opennms.client.impl;

import it.arsinfo.opennms.client.api.ApiClientCredentials;
import it.arsinfo.opennms.client.api.ApiClientProvider;
import it.arsinfo.opennms.client.api.ApiClientService;
import it.arsinfo.opennms.client.impl.handler.ApiClient;

public class ApiClientProviderImpl implements ApiClientProvider {
    @Override
    public ApiClientService client(ApiClientCredentials credentials) {
        return new ApiClientServiceImpl(getApiClient(credentials));
    }

    private static ApiClient getApiClient(ApiClientCredentials credentials) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(credentials.url);
        apiClient.setUsername(credentials.username);
        apiClient.setPassword(credentials.password);
        return apiClient;
    }
}
