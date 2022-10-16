package org.securityrat.casemanagement.service;

import lombok.*;


// todo add Ticket System Instance property
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractTemporaryTokenProperties {

    @Getter
    private String tempToken;

    @Getter
    private String tmpSecret;

    @Getter
    private String authorizationUrl;

    @Getter
    private String verificationCode;

    protected AbstractTemporaryTokenProperties(@NonNull String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }
    protected AbstractTemporaryTokenProperties(@NonNull String tempToken, @NonNull String verificationCode) {
        this.tempToken = tempToken;
        this.verificationCode = verificationCode;
    }

    @Override
    public String toString(){
        //todo: rework this toString method
        return String.format("%s,%s,%s", this.getTempToken(), "", this.getAuthorizationUrl());
    }
}
