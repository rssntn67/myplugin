package it.arsinfo.opennms.client.api;

import java.util.Objects;

/**
 * Credentials for a OpenNMS API connection.
 */
public class ApiClientCredentials {
    /**
     * The URL of the OpenNMS orchestrator.
     */
    public final String opennmsBaseUrl;

    /**
     * The username used to authenticate the connection to the PRISM ELEMENT.
     */
    public final String username;

    /**
     * The password used to authenticate the connection to the PRISM ELEMENT.
     */
    public final String password;


    private ApiClientCredentials(final Builder builder) {
        this.opennmsBaseUrl = Objects.requireNonNull(builder.prismUrl);
        this.username = builder.username;
        this.password = builder.password;
    }

    public static class Builder {
        private String prismUrl;
        private String username;
        private String password;

        private Builder() {
        }

        public Builder withPrismUrl(final String orchestratorUrl) {
            this.prismUrl = orchestratorUrl;
            return this;
        }

        public Builder withUsername(final String username) {
            this.username = username;
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

    public static Builder builder(ApiClientCredentials credentials) {
        return builder()
                .withPrismUrl(credentials.opennmsBaseUrl)
                .withUsername(credentials.username)
                .withPassword(credentials.password);

    }

    @Override
    public String toString() {
        return "ApiClientCredentials{" +
                "prismUrl='" + opennmsBaseUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiClientCredentials)) {
            return false;
        }
        final ApiClientCredentials that = (ApiClientCredentials) o;
        return Objects.equals(this.opennmsBaseUrl, that.opennmsBaseUrl) &&
                Objects.equals(this.username, that.username) &&
                Objects.equals(this.password, that.password);

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.opennmsBaseUrl,
                 this.username, this.password);
    }

}
