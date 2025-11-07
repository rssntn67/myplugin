package it.arsinfo.myplugin;

import javax.ws.rs.core.Response;

import it.arsinfo.myplugin.snmp.AdvancedSnmpSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WebhookHandlerImpl implements WebhookHandler {
    private static final Logger LOG = LoggerFactory.getLogger(WebhookHandlerImpl.class);

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
        try {
            AdvancedSnmpSet snmpsetter = new AdvancedSnmpSet(body, body);
            snmpsetter.setString("body", "sync");
            //tell the system to call
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

