package org.securityrat.casemanagement.service.dto;

import java.util.List;

public class StatusForReqDTO {

    private Long keyId;
    private List<Long> values;

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public List<Long> getValues() {
        return values;
    }

    public void setValues(List<Long> values) {
        this.values = values;
    }
}
