package org.securityrat.casemanagement.service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A DTO for the Attribute entity.
 */
public class AttributeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    private Integer showOrder;

    @NotNull
    @JsonIgnore
    private Boolean active;

    @JsonIgnore
    private AttributeDTO parent;

    @JsonIgnore
    private AttributeKeyDTO attributeKey;

    private List<AttributeDTO> children;

    public AttributeDTO() {
        children = new ArrayList<>();
    }

    @JsonIgnore
    public AttributeDTO getParent() {
        return parent;
    }

    @JsonProperty
    public void setParent(AttributeDTO parent) {
        this.parent = parent;
    }

    public List<AttributeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<AttributeDTO> children) {
        this.children = children;
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

    @JsonIgnore
    public Boolean isActive() {
        return active;
    }

    @JsonProperty
    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonIgnore
    public AttributeKeyDTO getAttributeKey() {
        return attributeKey;
    }

    @JsonProperty
    public void setAttributeKey(AttributeKeyDTO attributeKey) {
        this.attributeKey = attributeKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AttributeDTO attributeDTO = (AttributeDTO) o;
        if(attributeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attributeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AttributeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", showOrder=" + getShowOrder() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
