package org.securityrat.casemanagement.service.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"type"})
public class EnhancementForReqStructureDTO extends ExtensionKeyDTO {
	
	public EnhancementForReqStructureDTO() {}
	
	public EnhancementForReqStructureDTO(ExtensionKeyDTO exKey) {
		this.setId(exKey.getId());
		this.setName(exKey.getName());
		this.setRequirementSet(exKey.getRequirementSet());
		this.setDescription(exKey.getDescription());
		this.setSection(exKey.getSection());
		this.setShowOrder(exKey.getShowOrder());
		this.setType(exKey.getType());
	}
}
