package org.securityrat.casemanagement.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Base64Utils;

import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

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
        @Setter
        private String consumerKey;

        @Getter
        @Setter
        private String callback;

        @Getter
        private String privateKey;

        @Getter
        @Setter
        private Long validationPeriod;

        public void setPrivateKey(String privateKey) {
            this.privateKey = Base64Utils.encodeToString(privateKey.getBytes(StandardCharsets.UTF_8));
        }

        public void setCallback(String callback) throws URISyntaxException {
            URI uri = new URI(callback);
            this.callback = uri.toString();
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
