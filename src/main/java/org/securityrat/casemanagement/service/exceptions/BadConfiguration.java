package org.securityrat.casemanagement.service.exceptions;

public class BadConfiguration extends Exception {
    public BadConfiguration(String errorMessage) {
        super(errorMessage);
    }
}
