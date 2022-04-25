package org.securityrat.casemanagement.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class TokenNotGeneratedException extends RuntimeException {
    public TokenNotGeneratedException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
