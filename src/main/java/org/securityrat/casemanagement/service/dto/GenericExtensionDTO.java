package org.securityrat.casemanagement.service.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericExtensionDTO {
	
	@NotNull
	private Long id;

	@Lob
	@NotNull
	private String content;

	private Integer showOrder;
	
	@Lob
	private String description;
	
	@JsonIgnore
	private ExtensionKeyDTO extensionKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	public ExtensionKeyDTO getExtensionKey() {
		return extensionKey;
	}
	
	@JsonProperty
	public void setExtensionKey(ExtensionKeyDTO extensionKey) {
		this.extensionKey = extensionKey;
	}

}
