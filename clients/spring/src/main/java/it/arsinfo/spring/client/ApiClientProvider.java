package it.arsinfo.spring.client;


public class ApiClientProvider {
    /**
     * Create a client for a OpenNMS account.
     *
     * @param credentials the credentials to use for the client.
     * @return a NutanixApiClient client
     */
    public ApiClientService client(final ApiClientCredentials credentials) {
        return new ApiClientService(getApiClient(credentials));
    }

    private static ApiClient getApiClient(ApiClientCredentials credentials) {
        return new ApiClient(credentials.url);
    }

}
