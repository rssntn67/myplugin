package it.arsinfo.opennms.client.api;

import java.util.List;

import it.arsinfo.opennms.client.api.model.Ack;

public interface ApiClientService {

    /**
     * Get the Ack by uuid.
     *
     * @param  id the id of the Ack
     * @return a {@link Ack}s
     * @throws OpenNMSApiException "see message for detail"
     */
    Ack getAckById(String id) throws OpenNMSApiException;

    /**
     * Get the Ack by uuid.
     *
     * @param  alarmId the alarm id of the Ack
     * @return a List od {@link Ack}s
     * @throws OpenNMSApiException "see message for detail"
     */
    List<Ack> getAckByAlarmId(String alarmId) throws OpenNMSApiException;

    /**
     * Get the Acks.
     *
     * @return a list of {@link Ack}s
     * @throws OpenNMSApiException "see message for detail"
     */
    List<Ack> getAcks() throws OpenNMSApiException;

}
