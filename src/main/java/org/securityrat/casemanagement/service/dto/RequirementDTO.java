package org.securityrat.casemanagement.service.dto;

import java.util.List;

public class RequirementDTO {

    private Long id;
    private String name;
    private String description;
    private Integer showOrder;
    private List<Long> feTags;
    private List<Long> parameters;
    private Long catergoryId;
    private List<EnhancementForReqDTO> enhancements;
    private List<StatusForReqDTO> status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public List<Long> getFeTags() {
        return feTags;
    }

    public void setFeTags(List<Long> feTags) {
        this.feTags = feTags;
    }

    public List<Long> getParameters() {
        return parameters;
    }

    public void setParameters(List<Long> parameters) {
        this.parameters = parameters;
    }

    public Long getCatergoryId() {
        return catergoryId;
    }

    public void setCatergoryId(Long catergoryId) {
        this.catergoryId = catergoryId;
    }

    public List<EnhancementForReqDTO> getEnhancements() {
        return enhancements;
    }

    public void setEnhancements(List<EnhancementForReqDTO> enhancements) {
        this.enhancements = enhancements;
    }

    public List<StatusForReqDTO> getStatus() {
        return status;
    }

    public void setStatus(List<StatusForReqDTO> status) {
        this.status = status;
    }
}
