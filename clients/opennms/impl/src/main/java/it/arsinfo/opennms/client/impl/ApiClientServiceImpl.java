package it.arsinfo.opennms.client.impl;

import it.arsinfo.opennms.client.api.ApiClientService;
import it.arsinfo.opennms.client.api.OpenNMSApiException;
import it.arsinfo.opennms.client.impl.api.DefaultApi;
import it.arsinfo.opennms.client.impl.handler.ApiClient;
import it.arsinfo.opennms.client.impl.handler.ApiException;
import it.arsinfo.opennms.client.api.model.Ack;
import it.arsinfo.opennms.client.api.model.AckCollection;
import it.arsinfo.opennms.client.impl.model.OnmsAcknowledgment;
import it.arsinfo.opennms.client.impl.model.OnmsAcknowledgmentCollection;

import java.util.Objects;

public class ApiClientServiceImpl implements ApiClientService {
    final DefaultApi api;

    public ApiClientServiceImpl(ApiClient apiClient) {
        Objects.requireNonNull(apiClient);
        this.api= new DefaultApi(apiClient);
    }

    private static OpenNMSApiException getFrom(ApiException apiException) {
        return new OpenNMSApiException(
                apiException.getMessage(),
                apiException,
                apiException.getCode(),
                apiException.getResponseHeaders(),
                apiException.getResponseBody());
    }

    public static Ack.AckType convert(OnmsAcknowledgment.AckTypeEnum onmsAckType) {
        Ack.AckType ackType = Ack.AckType.UNSPECIFIED;
        ackType = switch (onmsAckType) {
            case ALARM -> Ack.AckType.ALARM;
            case NOTIFICATION -> Ack.AckType.NOTIFICATION;
            default -> ackType;
        };
        return ackType;
    }

    public static AckCollection convert(OnmsAcknowledgmentCollection onmsAcknowledgmentCollection) {
        final AckCollection.Builder ackCollectionBuilder = AckCollection.builder()
                .withCount(onmsAcknowledgmentCollection.getCount())
                .withOffset(onmsAcknowledgmentCollection.getOffset())
                .withTotalCount(onmsAcknowledgmentCollection.getTotalCount());
        onmsAcknowledgmentCollection.getOnmsAcknowledgment()
                .forEach(a-> ackCollectionBuilder.addAcks(convert(a)));
        return ackCollectionBuilder.build();
    }

    public static Ack.AckAction convert(OnmsAcknowledgment.AckActionEnum onmsAckAction) {
        Ack.AckAction ackAction = Ack.AckAction.UNSPECIFIED;
        ackAction = switch (onmsAckAction) {
            case ESCALATE -> Ack.AckAction.ESCALATE;
            case CLEAR -> Ack.AckAction.CLEAR;
            case ACKNOWLEDGE -> Ack.AckAction.ACKNOWLEDGE;
            case UNACKNOWLEDGE -> Ack.AckAction.UNACKNOWLEDGE;
            default -> ackAction;
        };
        return ackAction;
    }

    public static Ack convert(OnmsAcknowledgment onmsAcknowledgment) {
        return Ack.builder()
                .withId(onmsAcknowledgment.getId())
                .withAckType(convert(onmsAcknowledgment.getAckType()))
                .withAckAction(convert(onmsAcknowledgment.getAckAction()))
                .withAckUser(onmsAcknowledgment.getAckUser())
                .withAckTime(onmsAcknowledgment.getAckTime())
                .withRefId(onmsAcknowledgment.getRefId())
                .build();
    }

    @Override
    public Ack getAckById(Integer id) throws OpenNMSApiException {
        try {
            return convert(api.getAck(id));
        } catch (ApiException e) {
            throw getFrom(e);
        }
    }

    @Override
    public AckCollection getAckByAlarmId(Integer alarmId) throws OpenNMSApiException {
        try {
            return convert(api.getAcksByAlarmId(alarmId));
        } catch (ApiException e) {
            throw getFrom(e);
        }
    }

    @Override
    public AckCollection getAcks(Integer limit, Integer offset) throws OpenNMSApiException {
        try {
            return convert(api.getAcks(limit,offset));
        } catch (ApiException e) {
            throw getFrom(e);
        }
    }
}
