package org.securityrat.casemanagement.service.interfaces;

import org.securityrat.casemanagement.service.TemporaryTokenProperties;

import java.time.ZonedDateTime;

public interface OAuthClient {

    TemporaryTokenProperties getAndAuthorizeTemporaryToken();

    String getAccessToken(String tmpToken, String authorizationCode);

    ZonedDateTime getDefaultExpirationDate();

    String getCallbackUrl();
}
