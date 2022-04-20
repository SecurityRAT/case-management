package org.securityrat.casemanagement.service.ticketsystems.jiraserver;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import lombok.extern.slf4j.Slf4j;
import org.securityrat.casemanagement.config.ApplicationProperties;
import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.securityrat.casemanagement.service.TemporaryTokenProperties;
import org.securityrat.casemanagement.service.interfaces.OAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

// todo add check that ticket instance exist and return error if not
@Slf4j
@Component
@Transactional
public class JiraOAuthClient implements OAuthClient {

    private final JiraOAuthTokenFactory oAuthGetAccessTokenFactory;
    private final String authorizationUrl;
    @Autowired
    private ApplicationProperties applicationProperties;

    public JiraOAuthClient(TicketSystemInstance ticketSystemInstance) {
        String jiraBaseUrl = ticketSystemInstance.getUrl();
        this.oAuthGetAccessTokenFactory = new JiraOAuthTokenFactory(jiraBaseUrl);
        this.authorizationUrl = String.format("%s%s", jiraBaseUrl, "/plugins/servlet/oauth/authorize");
    }

    /**
     * Gets temporary request token and creates url to authorize it
     *
     * @return request token value
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws IOException
     */
    @Override
    public TemporaryTokenProperties getAndAuthorizeTemporaryToken() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        JiraOAuthGetTemporaryToken temporaryToken = oAuthGetAccessTokenFactory.getTemporaryToken(
            this.applicationProperties.getJiraServer().getConsumerKey(), this.applicationProperties.getJiraServer().getPrivateKey(),
            this.applicationProperties.getJiraServer().getCallback());
        OAuthCredentialsResponse response = temporaryToken.execute();

        OAuthAuthorizeTemporaryTokenUrl authorizationURL = new OAuthAuthorizeTemporaryTokenUrl(authorizationUrl);
        authorizationURL.temporaryToken = response.token;

        return new JiraTemporaryTokenProperties(response.token, response.tokenSecret, authorizationURL.toString());
    }


    /**
     * Gets access token from JIRA server
     *
     * @param tmpToken temporary request token
     * @param secret   secret (verification code provided by JIRA after request token authorization)
     * @return access token value
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws IOException
     */
    @Override
    public String getAccessToken(String tmpToken, String secret)
        throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        JiraOAuthGetAccessToken oAuthAccessToken = oAuthGetAccessTokenFactory.getJiraOAuthGetAccessToken(
            tmpToken, secret, this.applicationProperties.getJiraServer().getConsumerKey(), this.applicationProperties.getJiraServer().getPrivateKey());
        OAuthCredentialsResponse response = oAuthAccessToken.execute();

        return response.token;
    }

}
