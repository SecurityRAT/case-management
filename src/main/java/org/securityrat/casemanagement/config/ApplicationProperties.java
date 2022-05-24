package org.securityrat.casemanagement.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.net.URISyntaxException;

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
    private final ApplicationProperties.JiraServer jiraServer = new JiraServer();


    public static class TicketSystem {

        @Getter
        @Setter
        private String type;

        public TicketSystem() {
            this.type = org.securityrat.casemanagement.domain.enumeration.TicketSystem.JIRASERVER.name();
        }
    }

    @NoArgsConstructor
    public static class JiraServer {

        @Getter
        private String callback;

        @Getter
        @Setter
        private String privateKey;

        @Getter
        @Setter
        private Long validationPeriod;

        public void setCallback(String callback) throws URISyntaxException {
            if (!callback.equals("oob")) {
                URI uri = new URI(callback);
                this.callback = uri.toString();
            } else {
                this.callback = "oob";
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
            private String secretKey;
        }
    }

}
