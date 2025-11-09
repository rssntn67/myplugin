package it.arsinfo.myplugin.client;


public class ClientManager {

    private final it.arsinfo.snmp.client.ApiClientProvider snmpProvider;
    private final it.arsinfo.snmp.client.ApiClientCredentials snmpCred;
    private final it.arsinfo.opennms.client.api.ApiClientProvider onmsProvider;
    private final it.arsinfo.opennms.client.api.ApiClientCredentials onmsCred;
    private final it.arsinfo.spring.client.ApiClientProvider provider;
    private final it.arsinfo.spring.client.ApiClientCredentials cred;
    public ClientManager(
                    it.arsinfo.opennms.client.api.ApiClientProvider onmsProvider,
                    String opennmsClientUrl,
                    String opennmsClientUsername,
                    String opennmsClientPassword,
                    it.arsinfo.spring.client.ApiClientProvider provider,
                    String springClientUrl,
                    it.arsinfo.snmp.client.ApiClientProvider snmpProvider,
                    String snmpClientAddress,
                    String snmpClientVersion,
                    String snmpClientCommunity
                     ) {
        this.snmpProvider = snmpProvider;
        this.onmsProvider = onmsProvider;
        this.provider = provider;
        this.snmpCred = it.arsinfo.snmp.client.ApiClientCredentials.builder()
                .withAddress(snmpClientAddress)
                .withVersion(Integer.valueOf(snmpClientVersion))
                .withPassword(snmpClientCommunity)
                .build();
        this.cred = it.arsinfo.spring.client.ApiClientCredentials.builder()
                .withUrl(springClientUrl)
                .build();
        this.onmsCred = it.arsinfo.opennms.client.api.ApiClientCredentials.builder()
                .withUrl(opennmsClientUrl)
                .withUsername(opennmsClientUsername)
                .withPassword(opennmsClientPassword)
                .build();
    }

    public it.arsinfo.opennms.client.api.ApiClientService getOnmsApiService() {
        return onmsProvider.client(this.onmsCred);
    }

    public it.arsinfo.spring.client.ApiClientService getSpringApiService() {
        return provider.client(this.cred);
    }

    public it.arsinfo.snmp.client.ApiClientService getSnmpApiService() {
        return snmpProvider.client(this.snmpCred);
    }


}
