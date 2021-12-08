package org.securityrat.casemanagement.service;


import org.securityrat.casemanagement.domain.AccessToken;
import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.securityrat.casemanagement.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessTokenService {
    private final Logger log = LoggerFactory.getLogger(AccessTokenService.class);

    public boolean createTempToken(TicketSystemInstance ticketInstance, User user, String tmpToken) {

        return false;
    }

    public boolean updateTempTokenWithAccessToken(TicketSystemInstance ticketSystemInstance, User user, String accessToken) {
        return false;
    }

    /**
     * Returns the access token of a given the user for the ticket system if it exists and null otherwise
     *
     * @param ticketSystemInstance the ticket system instance object
     * @param user                 the given user
     * @return the access token or null if it doesn't exist
     */
    public AccessToken getExistingAccessToken(TicketSystemInstance ticketSystemInstance, User user) {

        return null;
    }

    public boolean deleteAccessTokenEntry(TicketSystemInstance ticketSystemInstance, User user) {
        return false;
    }
}
