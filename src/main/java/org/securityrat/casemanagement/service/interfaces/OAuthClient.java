package org.securityrat.casemanagement.service.interfaces;

import org.securityrat.casemanagement.service.AbstractTemporaryTokenProperties;

import java.time.ZonedDateTime;

public interface OAuthClient {

    AbstractTemporaryTokenProperties getAndAuthorizeTemporaryToken();

    String getAccessToken(String tmpToken, String authorizationCode);

    ZonedDateTime getDefaultExpirationDate();

    String getCallbackUrl();
}
