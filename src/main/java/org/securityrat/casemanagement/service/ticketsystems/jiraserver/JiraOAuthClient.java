package org.securityrat.casemanagement.service.ticketsystems.jiraserver;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class JiraOAuthClient {

    public final String jiraBaseUrl;
    private final JiraOAuthTokenFactory oAuthGetAccessTokenFactory;
    private final String authorizationUrl;

    public JiraOAuthClient() throws Exception {
        jiraBaseUrl = "";
        this.oAuthGetAccessTokenFactory = new JiraOAuthTokenFactory(this.jiraBaseUrl);
        authorizationUrl = jiraBaseUrl + "/plugins/servlet/oauth/authorize";
    }

    /**
     * Gets temporary request token and creates url to authorize it
     *
     * @param consumerKey consumer key
     * @param privateKey  private key in PKCS8 format
     * @return request token value
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws IOException
     */
    public String getAndAuthorizeTemporaryToken(String consumerKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        JiraOAuthGetTemporaryToken temporaryToken = oAuthGetAccessTokenFactory.getTemporaryToken(consumerKey, privateKey);
        OAuthCredentialsResponse response = temporaryToken.execute();
        // todo: save temporary token (response.token) and token Secret (response.tokenSecret)
//        System.out.println("Token:\t\t\t" + response.token);
//        System.out.println("Token secret:\t" + response.tokenSecret);

        OAuthAuthorizeTemporaryTokenUrl authorizationURL = new OAuthAuthorizeTemporaryTokenUrl(authorizationUrl);
        authorizationURL.temporaryToken = response.token;

//        System.out.println("Retrieve request token. Go to " + authorizationURL.toString() + " to authorize it.");

//        return response.token;
        return authorizationURL.toString();
    }

    /**
     * Gets access token from JIRA server
     *
     * @param tmpToken    temporary request token
     * @param secret      secret (verification code provided by JIRA after request token authorization)
     * @param consumerKey consumer key
     * @param privateKey  private key in PKCS8 format
     * @return access token value
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws IOException
     */
    public boolean storeAccessToken(String tmpToken, String secret, String consumerKey, String privateKey)
        throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        JiraOAuthGetAccessToken oAuthAccessToken = oAuthGetAccessTokenFactory.getJiraOAuthGetAccessToken(
            tmpToken, secret, consumerKey, privateKey);
        OAuthCredentialsResponse response = oAuthAccessToken.execute();

        // todo encrypt response.token and store it
        System.out.println("Access token:\t\t\t" + response.token);
        return true;
    }
}
