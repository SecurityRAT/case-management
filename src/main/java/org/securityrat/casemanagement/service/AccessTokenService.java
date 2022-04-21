package org.securityrat.casemanagement.service;


import lombok.extern.slf4j.Slf4j;
import org.securityrat.casemanagement.config.ApplicationProperties;
import org.securityrat.casemanagement.domain.AccessToken;
import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.securityrat.casemanagement.domain.User;
import org.securityrat.casemanagement.domain.enumeration.TicketSystem;
import org.securityrat.casemanagement.repository.AccessTokenRepository;
import org.securityrat.casemanagement.service.exceptions.TempTokenNotGeneratedException;
import org.securityrat.casemanagement.service.interfaces.OAuthClient;
import org.securityrat.casemanagement.service.ticketsystems.jiraserver.JiraOAuthClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
@Slf4j
public class AccessTokenService {

    private final ApplicationProperties applicationProperties;

    private final AccessTokenRepository accessTokenRepository;
    private OAuthClient oauthClient;

    public AccessTokenService(ApplicationProperties applicationProperties, AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
        this.applicationProperties = applicationProperties;
    }

    public void setOAuthClient(TicketSystemInstance ticketSystemInstance) {
        this.oauthClient = getOauthClient(this.applicationProperties.getTicketSystem().getType(), ticketSystemInstance);
    }

    public TemporaryTokenProperties createTempToken() {

        try {
            return this.oauthClient.getAndAuthorizeTemporaryToken();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new TempTokenNotGeneratedException("The private key of the ticket system is invalid.", e);
        } catch (IOException e) {
            throw new TempTokenNotGeneratedException("Possibly a GeneralSecurityIssue", e);
        }
    }

    public boolean updateTempTokenWithAccessToken(User user, String accessToken) {
        return false;
    }

    /**
     * Returns the access token of a given the user for the ticket system if it exists and null otherwise
     *
     * @param user the given user
     * @return the access token or null if it doesn't exist
     */
    public AccessToken getExistingAccessToken(User user) {

        return null;
    }

    public boolean deleteAccessTokenEntry(User user) {
        return false;
    }

    private OAuthClient getOauthClient(String ticketSystemType, TicketSystemInstance ticketSystemInstance) {
        OAuthClient defaultClient = new JiraOAuthClient(ticketSystemInstance);

        if (TicketSystem.JIRACLOUD.equals(TicketSystem.valueOf(ticketSystemType))) {
            defaultClient = null;
        }

        return defaultClient;
    }
}
