package it.arsinfo.spring.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class AckAlert {
    public enum AckType {
        UNSPECIFIED,
        ALARM,
        NOTIFICATION;

        public static class Serializer extends StdSerializer<AckAlert> {
            protected Serializer() {
                super(AckAlert.class);
            }

            @Override
            public void serialize(AckAlert alert, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(alert.toString().toLowerCase());
            }
        }

    }

    public enum AckAction {
        UNSPECIFIED,
        ACKNOWLEDGE,
        UNACKNOWLEDGE,
        ESCALATE,
        CLEAR;

        public static class Serializer extends StdSerializer<AckAction> {
            protected Serializer() {
                super(AckAction.class);
            }

            @Override
            public void serialize(AckAction action, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(action.toString().toLowerCase());
            }
        }

    }

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("ackType")
    @JsonSerialize(using = AckType.Serializer.class)
    private AckType ackType;

    @JsonProperty("ackAction")
    @JsonSerialize(using = AckAction.Serializer.class)
    private AckAction ackAction;

    @JsonProperty("ackUser")
    private String ackUser;

    @JsonProperty("ackTime")
    @JsonSerialize(using = Alert.InstantSerializer.class)
    private Instant ackTime;

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public Instant getAckTime() {
        return ackTime;
    }

    public void setAckTime(Instant ackTime) {
        this.ackTime = ackTime;
    }

    public String getAckUser() {
        return ackUser;
    }

    public void setAckUser(String ackUser) {
        this.ackUser = ackUser;
    }

    public AckAction getAckAction() {
        return ackAction;
    }

    public void setAckAction(AckAction ackAction) {
        this.ackAction = ackAction;
    }

    public AckType getAckType() {
        return ackType;
    }

    public void setAckType(AckType ackType) {
        this.ackType = ackType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("alarmId")
    private Integer alarmId;



}
