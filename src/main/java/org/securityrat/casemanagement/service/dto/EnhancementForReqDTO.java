package org.securityrat.casemanagement.service.dto;

import java.util.List;

public class EnhancementForReqDTO {

    private Long keyId;
    private List<GenericExtensionDTO> contents;

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public List<GenericExtensionDTO> getContents() {
        return contents;
    }

    public void setContents(List<GenericExtensionDTO> contents) {
        this.contents = contents;
    }
}
