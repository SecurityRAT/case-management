package org.securityrat.casemanagement.service;


import lombok.extern.slf4j.Slf4j;
import org.securityrat.casemanagement.config.ApplicationProperties;
import org.securityrat.casemanagement.domain.AccessToken;
import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.securityrat.casemanagement.domain.User;
import org.securityrat.casemanagement.domain.enumeration.TicketSystem;
import org.securityrat.casemanagement.service.interfaces.OAuthClient;
import org.securityrat.casemanagement.service.ticketsystems.jiraserver.JiraOAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccessTokenService {

    @Autowired
    private ApplicationProperties applicationProperties;

    private TicketSystemInstance ticketSystemInstance;
    private OAuthClient oauthClient;


    public AccessTokenService(TicketSystemInstance ticketSystemInstance) {
        this.oauthClient = getOauthClient(this.applicationProperties.getTicketSystem().getType());
        this.ticketSystemInstance = ticketSystemInstance;
    }

    public boolean createTempToken(User user, String tmpToken) {

        return false;
    }

    public boolean updateTempTokenWithAccessToken(User user, String accessToken) {
        return false;
    }

    /**
     * Returns the access token of a given the user for the ticket system if it exists and null otherwise
     *
     * @param user                 the given user
     * @return the access token or null if it doesn't exist
     */
    public AccessToken getExistingAccessToken(User user) {

        return null;
    }

    public boolean deleteAccessTokenEntry(User user) {
        return false;
    }

    private OAuthClient getOauthClient(String type) {
        OAuthClient defaultClient = new JiraOAuthClient(this.ticketSystemInstance);

        if (TicketSystem.JIRACLOUD.equals(TicketSystem.valueOf(type))) {
            defaultClient = null;
        }

        return defaultClient;
    }
}
