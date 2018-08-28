package org.securityrat.casemanagement.service.dto;

import org.securityrat.casemanagement.domain.enumeration.ExtensionSection;
import org.securityrat.casemanagement.domain.enumeration.ExtensionType;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

public class ExtensionKeyDTO {

    private Long id;
    private RequirementSetDTO requirementSet;

    @NotNull
    private String name;

    @Lob
    private String description;

    @NotNull
    private ExtensionSection section;

    private ExtensionType type;
    private Integer showOrder;

    @NotNull
    private Boolean active;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RequirementSetDTO getRequirementSet() {
        return requirementSet;
    }

    public void setRequirementSet(RequirementSetDTO requirementSet) {
        this.requirementSet = requirementSet;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    public ExtensionSection getSection() {
        return section;
    }

    public void setSection(@NotNull ExtensionSection section) {
        this.section = section;
    }

    public ExtensionType getType() {
        return type;
    }

    public void setType(ExtensionType type) {
        this.type = type;
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
}
