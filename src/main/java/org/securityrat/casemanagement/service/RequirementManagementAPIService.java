package org.securityrat.casemanagement.service;

import org.securityrat.casemanagement.client.RequirementManagementServiceClient;
import org.securityrat.casemanagement.domain.enumeration.AttributeType;
import org.securityrat.casemanagement.service.dto.AttributeDTO;
import org.securityrat.casemanagement.service.dto.RequirementSetDTO;
import org.securityrat.casemanagement.web.rest.errors.IDNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class RequirementManagementAPIService {

    private RequirementManagementServiceClient requirementManagementServiceClient;

    @Autowired
    public RequirementManagementAPIService(RequirementManagementServiceClient requirementManagementServiceClient) {
        this.requirementManagementServiceClient = requirementManagementServiceClient;
    }

    public List<RequirementSetDTO> getActiveRequirementSets() {
        return this.requirementManagementServiceClient
            .getRequirementSetsFromRequirementManagement(true);
    }

    public List<AttributeDTO> getActiveAttributes(Long requirementSetId) {
        return getActiveAttributes(requirementSetId, null);
    }

    public List<AttributeDTO> getActiveAttributes(Long requirementSetId, List<AttributeType> types) {
        List<AttributeDTO> result = this.requirementManagementServiceClient
            .getAttributesFromRequirementManagement(true);

        if(result != null) {
            result.removeIf(attributeDTO -> attributeDTO.getAttributeKey() != null
                && attributeDTO.getAttributeKey().getRequirementSet().getId() != requirementSetId);
            result.removeIf(attributeDTO -> attributeDTO.getAttributeKey() != null
                && !attributeDTO.getAttributeKey().isActive()); // include only attributes with active attributeKey or no attributeKey
            if(types != null && types.size() > 0)
                result.removeIf((attributeDTO -> attributeDTO.getAttributeKey() != null
                    && types.indexOf(attributeDTO.getAttributeKey().getType()) == -1));
        }

        return unflattenAttributeHierarchy(result);
    }

    public List<AttributeDTO> getAttributesByIds(List<Long> ids) throws IDNotFoundException {
        List<AttributeDTO> result = this.requirementManagementServiceClient
            .getAllAttributesFromRequirementManagement();

        if(result != null)
            result.removeIf(attributeDTO -> !ids.contains(attributeDTO.getId()));

        if(result.size() < ids.size())
            throw new IDNotFoundException(); // 404 if user requested non-existent ids

        if(result != null) {
            result.removeIf(attributeDTO -> !attributeDTO.isActive());
            result.removeIf(attributeDTO -> attributeDTO.getAttributeKey() != null
                && !attributeDTO.getAttributeKey().isActive()); // include only attributes with active attributeKey or no attributeKey
        }

        return unflattenAttributeHierarchy(result);
    }

    private List<AttributeDTO> unflattenAttributeHierarchy(List<AttributeDTO> attributeDTOList) {
        List<AttributeDTO> result = new LinkedList<>(attributeDTOList);

        // generate a HashMap to map parent relation in result
        HashMap<Long, List<AttributeDTO>> toTreeHelper = new HashMap<>(); // key: parentId, value: list of children
        for(AttributeDTO attributeDTO : result) {
            if(attributeDTO.getParent() != null) {
                Long parentId = attributeDTO.getParent().getId();
                if (parentId != null) {
                    toTreeHelper.putIfAbsent(parentId, new ArrayList<>());
                    toTreeHelper.get(parentId).add(attributeDTO);
                }
            }
        }

        // move children to their parent's list of children (but only if result contains both child and parent)
        List<AttributeDTO> toRemove = new LinkedList<>();
        for(AttributeDTO possibleParent : result) {
            List<AttributeDTO> childAttributes = toTreeHelper.get(possibleParent.getId());
            if(childAttributes != null) {
                for(AttributeDTO childAttribute : childAttributes) {
                    if(result.contains(childAttribute)) {

                        if(possibleParent.getChildren() == null)
                            possibleParent.setChildren(new LinkedList<>());
                        possibleParent.getChildren().add(childAttribute);
                        toRemove.add(childAttribute);
                    }
                }
            }
        }

        // in case there are cyclic relations this will remove too many elements
        result.removeAll(toRemove);

        return result;
    }
}
