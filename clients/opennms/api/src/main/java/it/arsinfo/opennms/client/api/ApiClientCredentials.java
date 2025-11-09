package it.arsinfo.opennms.client.api;

import java.util.Objects;

/**
 * Credentials for a OpenNMS API connection.
 */
public class ApiClientCredentials {
    /**
     * The URL of the OpenNMS orchestrator.
     */
    public final String url;

    /**
     * The username used to authenticate the connection to the PRISM ELEMENT.
     */
    public final String username;

    /**
     * The password used to authenticate the connection to the PRISM ELEMENT.
     */
    public final String password;


    private ApiClientCredentials(final Builder builder) {
        this.url = Objects.requireNonNull(builder.url);
        this.username = builder.username;
        this.password = builder.password;
    }

    public static class Builder {
        private String url;
        private String username;
        private String password;

        private Builder() {
        }

        public Builder withUrl(final String orchestratorUrl) {
            this.url = orchestratorUrl;
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

    @Override
    public String toString() {
        return "ApiClientCredentials{" +
                "prismUrl='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiClientCredentials that)) {
            return false;
        }
        return Objects.equals(this.url, that.url) &&
                Objects.equals(this.username, that.username) &&
                Objects.equals(this.password, that.password);

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.url,
                 this.username, this.password);
    }

}
