package org.securityrat.casemanagement.service.ticketsystems.jira.oauth;

import org.securityrat.casemanagement.service.TemporaryTokenProperties;

public class JiraTemporaryTokenProperties extends TemporaryTokenProperties {
    protected JiraTemporaryTokenProperties(String authorizationUrl) {
        super(authorizationUrl);
    }

    protected JiraTemporaryTokenProperties(String tempToken, String verificationCode) {
        super(tempToken, verificationCode);
    }
}
