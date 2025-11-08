package it.arsinfo.opennms.client.api;

public interface ApiClientProvider {
    /**
     * Create a client for a OpenNMS account.
     *
     * @param credentials the credentials to use for the client.
     * @return a NutanixApiClient client
     */
    ApiClientService client(final ApiClientCredentials credentials);

    boolean validate(final ApiClientCredentials credentials);

}
