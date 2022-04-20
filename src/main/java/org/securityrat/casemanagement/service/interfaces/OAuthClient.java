package org.securityrat.casemanagement.service.interfaces;

import org.securityrat.casemanagement.service.TemporaryTokenProperties;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface OAuthClient {

    TemporaryTokenProperties getAndAuthorizeTemporaryToken() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

    String getAccessToken(String tmpToken, String secret)
        throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;
}
