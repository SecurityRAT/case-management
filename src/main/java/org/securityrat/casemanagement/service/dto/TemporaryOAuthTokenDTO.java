package org.securityrat.casemanagement.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class TemporaryOAuthTokenDTO {

    @Getter
    @Setter
    private String authorizationUrl;
}
