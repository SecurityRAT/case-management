package org.securityrat.casemanagement.service;

import lombok.*;


// todo add Ticket System Instance property
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TemporaryTokenProperties {

    @Getter
    private String tmpToken;

    @Getter
    private String tmpSecret;

    @Getter
    private String authorizationUrl;

    protected TemporaryTokenProperties(@NonNull String tmpToken, @NonNull String authorizationUrl) {
        this.tmpToken = tmpToken;
        this.authorizationUrl = authorizationUrl;

    }

    @Override
    public String toString(){
        //todo: add ticket system instance id
        if (this.getTmpSecret() != null) {
            return String.format("%s,%s,%s", this.getTmpToken(), this.getTmpSecret(), this.getAuthorizationUrl());
        }
        return String.format("%s,%s,%s", this.getTmpToken(), "", this.getAuthorizationUrl());
    }
}
