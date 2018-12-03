package org.securityrat.casemanagement.service.dto;

import org.securityrat.casemanagement.domain.enumeration.ExtensionSection;
import org.securityrat.casemanagement.domain.enumeration.ExtensionType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

public class ExtensionKeyDTO {
	
	@NotNull
	private Long id;
	
	@JsonIgnore
	private RequirementSetDTO requirementSet;

	@NotNull
	private String name;

	@Lob
	private String description;

	@NotNull
	@JsonIgnore
	private ExtensionSection section;
	
	private ExtensionType type;

	private Integer showOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@JsonIgnore
	public RequirementSetDTO getRequirementSet() {
		return requirementSet;
	}
	
	@JsonProperty
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

	@JsonIgnore
	public ExtensionSection getSection() {
		return section;
	}
	
	@JsonProperty
	public void setSection(ExtensionSection section) {
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

}
