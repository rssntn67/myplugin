package it.arsinfo.myplugin.shell;


import org.opennms.integration.api.v1.events.EventListener;
import org.opennms.integration.api.v1.events.EventSubscriptionService;
import org.opennms.integration.api.v1.model.InMemoryEvent;

import java.util.Collection;

public class AckEventListener implements EventSubscriptionService {
    private final String ACK_UEI="uei.opennms.org/ackd/acknowledge";
    private final String ACK_TYPE_KEY = "ackType";
    private final String ACK_REF_ID_KEY = "refId";
    private final String ACK_USER_KEY = "ackUser";
    private final String ACK_ACTION_KEY = "ackAction";


    @Override
    public void addEventListener(EventListener eventListener) {

    }

    @Override
    public void addEventListener(EventListener eventListener, Collection<String> collection) {

    }

    @Override
    public void addEventListener(EventListener eventListener, String s) {

    }

    @Override
    public void removeEventListener(EventListener eventListener) {

    }

    @Override
    public void removeEventListener(EventListener eventListener, Collection<String> collection) {

    }

    @Override
    public void removeEventListener(EventListener eventListener, String s) {

    }

    @Override
    public boolean hasEventListener(String s) {
        return false;
    }
}
