package org.securityrat.casemanagement.service.dto;

import java.util.List;

public class StatusForReqDTO {

    private Integer keyId;
    private List<Integer> values;

    public Integer getKeyId() {
        return keyId;
    }

    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }
}
