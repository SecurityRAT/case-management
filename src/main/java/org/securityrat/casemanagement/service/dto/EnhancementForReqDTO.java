package org.securityrat.casemanagement.service.dto;

import java.util.List;

public class EnhancementForReqDTO {

    private Integer keyId;
    private List<ExtensionWithoutDescDTO> contents;

    public Integer getKeyId() {
        return keyId;
    }

    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    public List<ExtensionWithoutDescDTO> getContents() {
        return contents;
    }

    public void setContents(List<ExtensionWithoutDescDTO> contents) {
        this.contents = contents;
    }
}
