package org.securityrat.casemanagement.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

import org.securityrat.casemanagement.domain.enumeration.AttributeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * A DTO for the AttributeKey entity.
 */
public class AttributeKeyDTO implements Serializable {
	
	public AttributeKeyDTO() {
	}
	
	public AttributeKeyDTO(Long id, String name, AttributeType type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}
	public AttributeKeyDTO(Long id, String name, AttributeType type, String description, Integer showOrder) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.description = description;
		this.showOrder = showOrder;
	}

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    @JsonIgnore
    private AttributeType type;

    private Integer showOrder;

    @NotNull
    @JsonIgnore
    private Boolean active;
    
    @JsonIgnore
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
    
    @JsonIgnore
    public AttributeType getType() {
        return type;
    }
    
    @JsonProperty
    public void setType(AttributeType type) {
        this.type = type;
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
    public RequirementSetDTO getRequirementSet() {
        return requirementSet;
    }
    
    @JsonProperty
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
            "}";
    }
}
