package org.securityrat.casemanagement.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

import org.securityrat.casemanagement.domain.enumeration.AttributeType;
import org.securityrat.casemanagement.domain.enumeration.AttributeType;

/**
 * A DTO for the AttributeKey entity.
 */
public class AttributeKeyDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    @NotNull
    private AttributeType type;

    private Integer showOrder;

    @NotNull
    private Boolean active;

    private RequirementSetDTO requirementSet;

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

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public RequirementSetDTO getRequirementSet() {
        return requirementSet;
    }

    public void setRequirementSet(RequirementSetDTO requirementSet) {
        this.requirementSet = requirementSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AttributeKeyDTO attributeKeyDTO = (AttributeKeyDTO) o;
        if(attributeKeyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attributeKeyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AttributeKeyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", showOrder=" + getShowOrder() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
