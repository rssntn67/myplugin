package it.arsinfo.opennms.client.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AckCollection {

    public final Integer offset;
    public final Integer count;
    public final Integer totalCount;
    public final List<Ack> acks;

    private AckCollection(Builder builder) {
        this.offset = Objects.requireNonNull(builder.offset);
        this.count = Objects.requireNonNull(builder.count);
        this.totalCount = Objects.requireNonNull(builder.totalCount);
        this.acks = Objects.requireNonNull(builder.acks);
    }

    public static class Builder {

        private Integer offset;
        private Integer count;
        private Integer totalCount;
        private final List<Ack> acks = new ArrayList<>();

        private Builder() {
        }

        public Builder withOffset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Builder withCount(Integer count) {
            this.count = count;
            return this;
        }

        public Builder withTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
            return this;
        }

        public Builder addAcks(Ack ack) {
            this.acks.add(ack);
            return this;
        }

        public AckCollection build() {
            return new AckCollection(this);
        }
    }

    public static AckCollection.Builder builder() {
        return new AckCollection.Builder();
    }

}