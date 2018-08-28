package org.securityrat.casemanagement.service.dto;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

public class ExtensionWithoutDescDTO {

    private Long id;

    @Lob
    @NotNull
    private String content;

    private Integer showOrder;

    @JsonIgnore
    @NotNull
    private Boolean active;

    private ExtensionKeyDTO extensionKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getContent() {
        return content;
    }

    public void setContent(@NotNull String content) {
        this.content = content;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
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
