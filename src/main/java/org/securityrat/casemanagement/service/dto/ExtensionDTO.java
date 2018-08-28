package org.securityrat.casemanagement.service.dto;

public class ExtensionDTO extends ExtensionWithoutDescDTO{

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
