package org.securityrat.casemanagement.service.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class StatusForReqStructureDTO extends ExtensionKeyDTO {
	
	public StatusForReqStructureDTO() {}
	
	public StatusForReqStructureDTO(ExtensionKeyDTO exKey) {
		this.setId(exKey.getId());
		this.setName(exKey.getName());
		this.setRequirementSet(exKey.getRequirementSet());
		this.setDescription(exKey.getDescription());
		this.setSection(exKey.getSection());
		this.setShowOrder(exKey.getShowOrder());
		this.setType(exKey.getType());
		this.values = new ArrayList<>();	
	}
	
	private List<ExtensionReqStructureDTO> values;
	
	public List<ExtensionReqStructureDTO> getValues() {
		return values;
	}

	public void setValues(List<ExtensionReqStructureDTO> values) {
		this.values = values;
	}
}
