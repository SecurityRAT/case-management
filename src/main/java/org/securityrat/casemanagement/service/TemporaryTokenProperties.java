package org.securityrat.casemanagement.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TemporaryTokenProperties {

    @Getter
    private String tmpToken;

    @Getter
    private String tmpSecret;

    @Getter
    private String authorizationUrl;

    protected TemporaryTokenProperties(String tmpToken, String authorizationUrl) {
        this.tmpToken = tmpToken;
        this.authorizationUrl = authorizationUrl;

    }

    @Override
    public String toString(){
        if (this.getTmpSecret() != null) {
            return String.format("%s,%s,%s", this.getTmpToken(), this.getTmpSecret(), this.getAuthorizationUrl());
        }
        return String.format("%s,%s,%s", this.getTmpToken(), "", this.getAuthorizationUrl());
    }
}
