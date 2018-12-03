package org.securityrat.casemanagement.service.dto;

public class SkAtExDTO {

    private Long id;
    private SkeletonDTO skeleton;
    private AttributeDTO attribute;
    private ExtensionReqDTO extension;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SkeletonDTO getSkeleton() {
        return skeleton;
    }

    public void setSkeleton(SkeletonDTO skeleton) {
        this.skeleton = skeleton;
    }

    public AttributeDTO getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeDTO attribute) {
        this.attribute = attribute;
    }

    public ExtensionReqDTO getExtension() {
        return extension;
    }

    public void setExtension(ExtensionReqDTO extension) {
        this.extension = extension;
    }



}
