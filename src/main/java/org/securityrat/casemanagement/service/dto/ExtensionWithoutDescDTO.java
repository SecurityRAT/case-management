package org.securityrat.casemanagement.service.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

public class ExtensionWithoutDescDTO {

    private Long id;

    @Lob
    @NotNull
    private String content;

    private Integer showOrder;


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


}
