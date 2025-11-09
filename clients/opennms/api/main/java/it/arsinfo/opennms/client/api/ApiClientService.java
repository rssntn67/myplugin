package it.arsinfo.opennms.client.api;

import java.util.List;

import it.arsinfo.opennms.client.api.model.Ack;
import it.arsinfo.opennms.client.api.model.AckCollection;

public interface ApiClientService {

    /**
     * Get the Ack by uuid.
     *
     * @param  id the id of the Ack
     * @return a {@link Ack}s
     * @throws OpenNMSApiException "see message for detail"
     */
    Ack getAckById(Integer id) throws OpenNMSApiException;

    /**
     * Get the Ack by uuid.
     *
     * @param  alarmId the alarm id of the Ack
     * @return a {@link AckCollection}s
     * @throws OpenNMSApiException "see message for detail"
     */
    AckCollection getAckByAlarmId(Integer alarmId) throws OpenNMSApiException;

    /**
     * Get the Acks.
     *
     * @return {@link AckCollection}s
     * @throws OpenNMSApiException "see message for detail"
     */
    AckCollection getAcks(Integer limit, Integer offset) throws OpenNMSApiException;

}
