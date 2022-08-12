package org.securityrat.casemanagement.service.ticketsystems.jira.oauth;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import lombok.extern.slf4j.Slf4j;
import org.securityrat.casemanagement.config.ApplicationProperties;
import org.securityrat.casemanagement.config.Constants;
import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.securityrat.casemanagement.service.AbstractTemporaryTokenProperties;
import org.securityrat.casemanagement.service.Utils;
import org.securityrat.casemanagement.service.exceptions.TokenNotGeneratedException;
import org.securityrat.casemanagement.service.interfaces.OAuthClient;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.ZonedDateTime;

// todo add check that ticket instance exist and return error if not
@Slf4j
public class JiraOAuthClient implements OAuthClient {

    private final JiraOAuthTokenFactory oAuthGetAccessTokenFactory;
    private final String authorizationUrl;

    private final String consumerKey;
    private final ApplicationProperties applicationProperties;

    public JiraOAuthClient(TicketSystemInstance ticketSystemInstance, ApplicationProperties applicationProperties) {
        String jiraBaseUrl = Utils.removeTrailingSlashInUrl(ticketSystemInstance.getUrl());
        this.oAuthGetAccessTokenFactory = new JiraOAuthTokenFactory(jiraBaseUrl);
        consumerKey = ticketSystemInstance.getConsumerKey();
        this.authorizationUrl = String.format("%s%s", jiraBaseUrl, Constants.JIRASERVERAUTHORIZEPATH);
        this.applicationProperties = applicationProperties;
    }

    /**
     * Gets temporary request token and creates url to authorize it
     *
     * @return request token value
     * @throws TokenNotGeneratedException
     */
    @Override
    // todo: Add exception if invalid ticket system instance
    public AbstractTemporaryTokenProperties getAndAuthorizeTemporaryToken() {
        try {
            JiraOAuthGetTemporaryToken temporaryToken = oAuthGetAccessTokenFactory.getTemporaryToken(
                consumerKey, this.applicationProperties.getJira().getOauth().getPrivateKey(),
                this.applicationProperties.getJira().getOauth().getCallbackUrl());
            OAuthCredentialsResponse response = temporaryToken.execute();

            OAuthAuthorizeTemporaryTokenUrl authorizationURL = new OAuthAuthorizeTemporaryTokenUrl(authorizationUrl);
            authorizationURL.temporaryToken = response.token;

            return new JiraTemporaryTokenProperties(authorizationURL.toString());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new TokenNotGeneratedException("The private key of the ticket system is invalid.", e);
        } catch (IOException e) {
            throw new TokenNotGeneratedException("Temporary token not generated. Possibly a GeneralSecurityIssue.", e);
        }
    }


    /**
     * Gets access token from JIRA server
     *
     * @param tmpToken          temporary request token
     * @param authorizationCode secret (verification code provided by JIRA after request token authorization)
     * @return access token value
     * @throws TokenNotGeneratedException
     */
    @Override
    public String getAccessToken(String tmpToken, String authorizationCode) {
        try {
            JiraOAuthGetAccessToken oAuthAccessToken = oAuthGetAccessTokenFactory.getJiraOAuthGetAccessToken(
                tmpToken, authorizationCode, consumerKey, this.applicationProperties.getJira().getOauth().getPrivateKey());
            OAuthCredentialsResponse response = oAuthAccessToken.execute();

            return response.token;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new TokenNotGeneratedException("The private key of the ticket system is invalid.", e);
        } catch (IOException e) {
            throw new TokenNotGeneratedException("Access token not generated. Possibly a GeneralSecurityIssue.", e);
        }
    }

    @Override
    public ZonedDateTime getDefaultExpirationDate() {
        Long validationPeriodInDays = this.applicationProperties.getJira().getOauth().getValidationPeriod();
        return ZonedDateTime.now().plusDays(validationPeriodInDays);
    }

    @Override
    public String getCallbackUrl() {
        return this.applicationProperties.getJira().getOauth().getCallbackUrl();
    }


}
