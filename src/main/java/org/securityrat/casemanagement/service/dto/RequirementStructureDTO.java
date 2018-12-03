package org.securityrat.casemanagement.service.dto;

import java.util.ArrayList;
import java.util.List;

public class RequirementStructureDTO {

	public RequirementStructureDTO() {

	}

	public RequirementStructureDTO(boolean init) {
		if (init) {
			this.enhancements = new ArrayList<>();
			this.status = new ArrayList<>();
		}
	}

	private List<EnhancementForReqStructureDTO> enhancements;

	private List<StatusForReqStructureDTO> status;

	public List<EnhancementForReqStructureDTO> getEnhancements() {
		return enhancements;
	}

	public void setEnhancements(List<EnhancementForReqStructureDTO> enhancements) {
		this.enhancements = enhancements;
	}

	public List<StatusForReqStructureDTO> getStatus() {
		return status;
	}

	public void setStatus(List<StatusForReqStructureDTO> status) {
		this.status = status;
	}

}
