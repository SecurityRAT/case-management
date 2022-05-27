package org.securityrat.casemanagement.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Properties specific to Case Management.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@NoArgsConstructor
public class ApplicationProperties {

    @Getter
    private final ApplicationProperties.Security security = new ApplicationProperties.Security();

    @Getter
    private final ApplicationProperties.TicketSystem ticketSystem = new ApplicationProperties.TicketSystem();

    @Getter
    private final Jira jira = new Jira();


    public static class TicketSystem {

        @Getter
        @Setter
        private String type;

        public TicketSystem() {
            this.type = org.securityrat.casemanagement.domain.enumeration.TicketSystem.JIRASERVER.name();
        }
    }

    @NoArgsConstructor
    public static class Jira {

        @Getter
        private final ApplicationProperties.Jira.Oauth oauth = new ApplicationProperties.Jira.Oauth();

        @NoArgsConstructor
        public static class Oauth {
            @Getter
            private String callbackUrl;

            @Getter
            @Setter
            private String privateKey;

            @Getter
            @Setter
            private Long validationPeriod;

            public void setCallbackUrl(String callbackUrl) throws URISyntaxException {
                if (!callbackUrl.equals("oob")) {
                    URI uri = new URI(callbackUrl);
                    this.callbackUrl = uri.toString();
                } else {
                    this.callbackUrl = "oob";
                }
            }
        }
        @NoArgsConstructor
        public static class Oauth2 {
            @Getter
            private String callbackUrl;

            @Getter
            @Setter
            private String clientId;

            @Getter
            @Setter
            private String clientSecret;

            @Getter
            @Setter
            private String prompt;

            @Getter
            @Setter
            private String audience;

            @Getter
            @Setter
            private List<String> scopes;

            public void setCallbackUrl(String callbackUrl) throws URISyntaxException {
                URI uri = new URI(callbackUrl);
                this.callbackUrl = uri.toString();
            }
        }
    }



    @NoArgsConstructor
    public static class Security {
        @Getter
        private final ApplicationProperties.Security.Aes aes = new ApplicationProperties.Security.Aes();

        @NoArgsConstructor
        public static class Aes {
            @Getter
            @Setter
            @NotBlank
            private String secret;
        }
    }

}
