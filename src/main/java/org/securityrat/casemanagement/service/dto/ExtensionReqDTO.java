package org.securityrat.casemanagement.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"description"})
public class ExtensionReqDTO extends GenericExtensionDTO {

   public ExtensionReqDTO() {}
   
   public ExtensionReqDTO(GenericExtensionDTO ex) {
	   this.setId(ex.getId());
	   this.setContent(ex.getContent());
	   this.setDescription(ex.getDescription());
	   this.setShowOrder(ex.getShowOrder());
	   this.setExtensionKey(ex.getExtensionKey());
   }
    
}
