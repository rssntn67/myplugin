package it.arsinfo.spring.client;

import java.util.Objects;

/**
 * Credentials for a OpenNMS API connection.
 */
public class ApiClientCredentials {
    /**
     * The URL of the OpenNMS orchestrator.
     */
    public final String url;


    private ApiClientCredentials(final Builder builder) {
        this.url = Objects.requireNonNull(builder.url);
    }

    public static class Builder {
        private String url;

        private Builder() {
        }

        public Builder withUrl(final String url) {
            this.url = url;
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
                "url='" + url + '\'' +
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
        return Objects.equals(this.url, that.url);

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.url);
    }

}
