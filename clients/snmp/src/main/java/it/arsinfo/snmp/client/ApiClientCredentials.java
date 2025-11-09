package it.arsinfo.snmp.client;

import java.util.Objects;

/**
 * Credentials for a OpenNMS API connection.
 */
public class ApiClientCredentials {
    /**
     * Address in the for udp|tcp:<ipaddresse>/port</>r.
     */
    public final String address;

    /**
     * The version to use for the protocol 0=v1 1=v2c 3=v3(not supported) .
     */
    public final Integer version;

    /**
     * The private community password used to authenticate with the peer.
     */
    public final String password;


    private ApiClientCredentials(final Builder builder) {
        this.address = Objects.requireNonNull(builder.address);
        this.version = builder.version;
        this.password = builder.password;
    }

    public static class Builder {
        private String address;
        private Integer version;
        private String password;

        private Builder() {
        }

        public Builder withAddress(final String address) {
            this.address = address;
            return this;
        }

        public Builder withVersion(final Integer version) {
            this.version = version;
            return this;
        }


        public Builder withPassword(final String password) {
            this.password = password;
            return this;
        }

        public ApiClientCredentials build() {
            return new ApiClientCredentials(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "ApiClientCredentials{" +
                "address='" + address + '\'' +
                ", version=" + version +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ApiClientCredentials that)) return false;
        return Objects.equals(address, that.address) && Objects.equals(version, that.version) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, version, password);
    }
}
