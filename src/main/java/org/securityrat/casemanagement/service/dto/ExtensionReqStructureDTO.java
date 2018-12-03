package org.securityrat.casemanagement.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"extensionKey"})
public class ExtensionReqStructureDTO extends GenericExtensionDTO {

   public ExtensionReqStructureDTO() {}
   
   public ExtensionReqStructureDTO(GenericExtensionDTO ex) {
	   this.setId(ex.getId());
	   this.setContent(ex.getContent());
	   this.setDescription(ex.getDescription());
	   this.setShowOrder(ex.getShowOrder());
	   this.setExtensionKey(ex.getExtensionKey());
   }
    
}
