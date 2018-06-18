package org.securityrat.casemanagement.service.dto;

import java.util.Objects;

public class SkeletonDTO {

    private boolean active;
    private String description;
    private Long id;
    private String name;
    private RequirementSetDTO requirementSet;
    private Integer showOrder;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public RequirementSetDTO getRequirementSet() {
        return requirementSet;
    }

    public void setRequirementSet(RequirementSetDTO requirementSet) {
        this.requirementSet = requirementSet;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SkeletonDTO skeletonDTO = (SkeletonDTO) o;
        if(skeletonDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), skeletonDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SkeletonDTO{" +
            "id=" + getId() +
            ", name='" + getName() + '\'' +
            ", description='" + getDescription() + '\'' +
            ", requirementSet=" + getRequirementSet() +
            ", showOrder=" + getShowOrder() +
            ", active=" + isActive() +
            '}';
    }
}
