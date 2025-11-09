package it.arsinfo.snmp.client;


import java.io.IOException;

public class ApiClientProvider {
    /**
     * Create a client for a OpenNMS account.
     *
     * @param credentials the credentials to use for the client.
     * @return a NutanixApiClient client
     */
    ApiClientService client(final ApiClientCredentials credentials) {
        try {
            return new ApiClientService(getService(credentials));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static AdvancedSnmpSet getService(ApiClientCredentials credentials) throws IOException {
        return new AdvancedSnmpSet(credentials.address, credentials.password, credentials.version);
    }

}
