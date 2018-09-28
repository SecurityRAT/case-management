package org.securityrat.casemanagement.service.dto;

import java.util.List;

public class EnhancementForReqDTO {

    private Long keyId;
    private List<ExtensionWithoutDescDTO> contents;

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public List<ExtensionWithoutDescDTO> getContents() {
        return contents;
    }

    public void setContents(List<ExtensionWithoutDescDTO> contents) {
        this.contents = contents;
    }
}
