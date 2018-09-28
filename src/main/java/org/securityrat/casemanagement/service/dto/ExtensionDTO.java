package org.securityrat.casemanagement.service.dto;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.validation.constraints.NotNull;

public class ExtensionDTO extends ExtensionWithoutDescDTO{

    private String description;

    @JsonIgnore
    @NotNull
    private Boolean active;

    private ExtensionKeyDTO extensionKey;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    public Boolean getActive() {
        return active;
    }

    public void setActive(@NotNull Boolean active) {
        this.active = active;
    }

    public ExtensionKeyDTO getExtensionKey() {
        return extensionKey;
    }

    public void setExtensionKey(ExtensionKeyDTO extensionKey) {
        this.extensionKey = extensionKey;
    }
}
