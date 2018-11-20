package org.securityrat.casemanagement.service.dto;

import java.util.List;

import org.securityrat.casemanagement.domain.enumeration.AttributeType;

public class GenericAttributeGatewayDTO extends AttributeKeyDTO{
	
	private static final long serialVersionUID = 1L;
	
	public GenericAttributeGatewayDTO(Long id, String name, AttributeType type) {
		super(id, name, type);
	}
	public GenericAttributeGatewayDTO(Long id, String name, AttributeType type, String description, Integer showOrder) {
		super(id, name, type, description, showOrder);
	}

	
	private List<AttributeDTO> values;

	public List<AttributeDTO> getValues() {
		return values;
	}

	public void setValues(List<AttributeDTO> values) {
		this.values = values;
	}

}
