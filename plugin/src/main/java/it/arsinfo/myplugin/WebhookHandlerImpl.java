package it.arsinfo.myplugin;

import it.arsinfo.myplugin.client.ClientManager;
import it.arsinfo.snmp.client.ApiClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

public class WebhookHandlerImpl implements WebhookHandler {
    private static final Logger LOG = LoggerFactory.getLogger(WebhookHandlerImpl.class);

    private final ApiClientService snmpService;

    public WebhookHandlerImpl(ClientManager manager) {
        this.snmpService = manager.getSnmpApiService();
    }

    @Override
    public Response ping() {
        return Response.ok("pong").build();
    }

    @Override
    public Response handleWebhook(String body) {
        LOG.debug("Got payload: {}", body);
        return Response.ok().build();
    }

    @Override
    public Response handleSync(String body) {
        boolean responseSet = this.snmpService.set(".1.2.3.4.5.6.7", "true");
        if (responseSet) {
            return Response.ok("Sync Started").build();
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).entity("Sync Failed").build();
    }
}

