package it.arsinfo.opennms.client.api.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ack {
    public final String uuid;
    public final String severity;
    public final String alertType;
    public final String message;
    public final String descr;



    private Ack(Builder builder) {
        this.uuid = Objects.requireNonNull(builder.uuid);
        this.severity = Objects.requireNonNull(builder.severity);
        this.alertType = Objects.requireNonNull(builder.alertType);
        this.message = Objects.requireNonNull(builder.message);
        this.descr = Objects.requireNonNull(builder.descr);
    }

    @Override
    public String toString() {
        return "Alert{" +
                "uuid='" + uuid + '\'' +
                ", severity='" + severity + '\'' +
                ", alertType='" + alertType + '\'' +
                ", message='" + message + '\'' +
                ", descr='" + descr + '\'' +
                '}';
    }

    public static class Builder {
        private String uuid;
        private String severity;
        private String alertType;

        private String message;
        private String descr;

        private Builder() {
        }

        public Ack.Builder withUuid(final String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Ack.Builder withSeverity(final String severity) {
            this.severity = severity;
            return this;
        }

        public Ack.Builder withMessage(final String message) {
            this.message = message;
            return this;
        }

        public Ack.Builder withDescr(final String descr) {
            this.descr = descr;
            return this;
        }

        public Ack.Builder withAlertType(final String alertType) {
            this.alertType = alertType;
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
