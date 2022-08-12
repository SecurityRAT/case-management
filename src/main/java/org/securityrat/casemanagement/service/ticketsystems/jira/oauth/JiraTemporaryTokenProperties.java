package org.securityrat.casemanagement.service.ticketsystems.jira.oauth;

import org.securityrat.casemanagement.service.AbstractTemporaryTokenProperties;

public class JiraTemporaryTokenProperties extends AbstractTemporaryTokenProperties {
    protected JiraTemporaryTokenProperties(String authorizationUrl) {
        super(authorizationUrl);
    }

    protected JiraTemporaryTokenProperties(String tempToken, String verificationCode) {
        super(tempToken, verificationCode);
    }
}
