package org.securityrat.casemanagement.config;

import org.securityrat.casemanagement.domain.enumeration.TicketSystem;
import org.securityrat.casemanagement.security.SecurityUtils;
import org.springframework.context.annotation.Configuration;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Configuration
public class TicketSystemConfiguration {

    public TicketSystemConfiguration(ApplicationProperties applicationProperties) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (TicketSystem.valueOf(applicationProperties.getTicketSystem().getType()).equals(TicketSystem.JIRASERVER)) {
            SecurityUtils.getPrivateKey(applicationProperties.getJiraServer().getPrivateKey());
        }

    }

}
