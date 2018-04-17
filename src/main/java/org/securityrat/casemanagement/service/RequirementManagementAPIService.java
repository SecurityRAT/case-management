package org.securityrat.casemanagement.service;

import org.securityrat.casemanagement.client.RequirementManagementServiceClient;
import org.securityrat.casemanagement.domain.enumeration.AttributeType;
import org.securityrat.casemanagement.service.dto.AttributeDTO;
import org.securityrat.casemanagement.service.dto.RequirementSetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
            .getAttributesFromRequirementMangament(true);

        if(result != null) {
            result.removeIf(attributeDTO -> attributeDTO.getAttributeKey() != null
                && attributeDTO.getAttributeKey().getRequirementSet().getId() != requirementSetId);
            result.removeIf(attributeDTO -> attributeDTO.getAttributeKey() != null
                && !attributeDTO.getAttributeKey().isActive());
            if(types != null && types.size() > 0)
                result.removeIf((attributeDTO -> attributeDTO.getAttributeKey() != null && types.indexOf(attributeDTO.getAttributeKey().getType()) == -1));
        }

        // generate a HashMap which knows about all parent relation in result
        HashMap<Long, List<AttributeDTO>> toTreeHelper = new HashMap<>(); // key: parentId, value: list of children
        for(AttributeDTO attributeDTO : result) {
            if(attributeDTO.getParent() != null) {
                Long parentId = attributeDTO.getParent().getId();
                if (parentId != null) {
                    if (toTreeHelper.get(parentId) == null)
                        toTreeHelper.put(parentId, new ArrayList<>());
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
