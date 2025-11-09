package it.arsinfo.opennms.client.api.model;

import java.util.Date;
import java.util.Objects;

public class Ack {
    public enum AckType {
        UNSPECIFIED,
        ALARM,
        NOTIFICATION
    }

    public enum AckAction {
        UNSPECIFIED,
        ACKNOWLEDGE,
        UNACKNOWLEDGE,
        ESCALATE,
        CLEAR
    }

    public final Integer id;
    public final AckType ackType;
    public final AckAction ackAction;
    public final String ackUser;
    public final Date ackTime;
    public final Integer refId;



    private Ack(Builder builder) {
        this.id = Objects.requireNonNull(builder.id);
        this.ackType = Objects.requireNonNull(builder.ackType);
        this.ackAction = Objects.requireNonNull(builder.ackAction);
        this.ackUser = Objects.requireNonNull(builder.ackUser);
        this.ackTime = Objects.requireNonNull(builder.ackTime);
        this.refId = Objects.requireNonNull(builder.refId);
    }

    public static class Builder {
        private Integer id;
        private AckType ackType;
        private AckAction ackAction;
        private String ackUser;
        private Date ackTime;
        private Integer refId;

        private Builder() {
        }

        public Ack.Builder withId(final Integer id) {
            this.id = id;
            return this;
        }

        public Ack.Builder withAckType(final AckType ackType) {
            this.ackType = ackType;
            return this;
        }

        public Ack.Builder withAckAction(final AckAction ackAction) {
            this.ackAction = ackAction;
            return this;
        }

        public Ack.Builder withAckUser(final String ackUser) {
            this.ackUser = ackUser;
            return this;
        }

        public Ack.Builder withAckTime(final Date ackTime) {
            this.ackTime = ackTime;
            return this;
        }

        public Ack.Builder withRefId(Integer refId) {
            this.refId = refId;
            return this;
        }

        public Ack build() {
            return new Ack(this);
        }

    }
    public static Ack.Builder builder() {
        return new Ack.Builder();
    }
}
