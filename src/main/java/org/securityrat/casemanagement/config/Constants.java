package org.securityrat.casemanagement.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";

    // Jira server variables
    public static final String JIRASERVERREQUESTTOKENPATH = "/plugins/servlet/oauth/request-token";
    public static final String JIRASERVERACCESSTOKENPATH = "/plugins/servlet/oauth/access-token";

    private Constants() {
    }
}
