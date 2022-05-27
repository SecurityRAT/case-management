package org.securityrat.casemanagement.service.ticketsystems.jira.oauth;

import com.google.api.client.auth.oauth.OAuthRsaSigner;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import org.securityrat.casemanagement.config.Constants;
import org.securityrat.casemanagement.security.SecurityUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class JiraOAuthTokenFactory {
    protected final String accessTokenUrl;
    protected final String requestTokenUrl;


    public JiraOAuthTokenFactory(String jiraBaseUrl) {
        this.accessTokenUrl = String.format("%s%s", jiraBaseUrl, Constants.JIRASERVERACCESSTOKENPATH);
        this.requestTokenUrl = String.format("%s%s", jiraBaseUrl, Constants.JIRASERVERREQUESTTOKENPATH);
    }

    /**
     * Initialize JiraOAuthGetAccessToken
     * by setting it to use POST method, secret, request token
     * and setting consumer and private keys.
     *
     * @param tmpToken    temporary request token
     * @param secret      secret (verification code provided by JIRA after request token authorization)
     * @param consumerKey consumer key
     * @param privateKey  private key in PKCS8 format
     * @return JiraOAuthGetAccessToken request
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public JiraOAuthGetAccessToken getJiraOAuthGetAccessToken(String tmpToken, String secret, String consumerKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        JiraOAuthGetAccessToken accessToken = new JiraOAuthGetAccessToken(accessTokenUrl);
        accessToken.consumerKey = consumerKey;
        accessToken.signer = getOAuthRsaSigner(privateKey);
        accessToken.transport = new ApacheHttpTransport();
        accessToken.verifier = secret;
        accessToken.temporaryToken = tmpToken;
        return accessToken;
    }


    /**
     * Initialize JiraOAuthGetTemporaryToken
     * by setting it to use POST method, oob (Out of Band) callback
     * and setting consumer and private keys.
     *
     * @param consumerKey consumer key
     * @param privateKey  private key in PKCS8 format
     * @return JiraOAuthGetTemporaryToken request
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public JiraOAuthGetTemporaryToken getTemporaryToken(String consumerKey, String privateKey, String callback) throws NoSuchAlgorithmException, InvalidKeySpecException {
        JiraOAuthGetTemporaryToken oAuthGetTemporaryToken = new JiraOAuthGetTemporaryToken(requestTokenUrl);
        oAuthGetTemporaryToken.consumerKey = consumerKey;
        oAuthGetTemporaryToken.signer = getOAuthRsaSigner(privateKey);
        oAuthGetTemporaryToken.transport = new ApacheHttpTransport();
        oAuthGetTemporaryToken.callback = callback;
        return oAuthGetTemporaryToken;
    }

    /**
     * @param privateKey private key in PKCS8 format
     * @return OAuthRsaSigner
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private OAuthRsaSigner getOAuthRsaSigner(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        OAuthRsaSigner oAuthRsaSigner = new OAuthRsaSigner();
        oAuthRsaSigner.privateKey = SecurityUtils.getPrivateKey(privateKey);
        return oAuthRsaSigner;
    }

}

