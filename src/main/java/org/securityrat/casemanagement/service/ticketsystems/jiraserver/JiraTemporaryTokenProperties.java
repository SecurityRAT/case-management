package org.securityrat.casemanagement.service.ticketsystems.jiraserver;

import org.securityrat.casemanagement.service.TemporaryTokenProperties;

public class JiraTemporaryTokenProperties extends TemporaryTokenProperties {
    protected JiraTemporaryTokenProperties(String tmpToken, String authorizationUrl) {
        super(tmpToken, authorizationUrl);
    }

    protected JiraTemporaryTokenProperties(String tmpToken, String tmpSecret, String authorizationUrl) {
        super(tmpToken, tmpSecret, authorizationUrl);
    }
}
