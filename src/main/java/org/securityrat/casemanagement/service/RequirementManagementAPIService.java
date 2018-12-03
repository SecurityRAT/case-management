package org.securityrat.casemanagement.service;

import org.securityrat.casemanagement.client.RequirementManagementServiceClient;
import org.securityrat.casemanagement.domain.enumeration.AttributeType;
import org.securityrat.casemanagement.service.dto.*;
import org.securityrat.casemanagement.web.rest.errors.IDNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequirementManagementAPIService {

	private RequirementManagementServiceClient requirementManagementServiceClient;
	
	private final Logger log = LoggerFactory.getLogger(RequirementManagementAPIService.class);

	@Autowired
	public RequirementManagementAPIService(RequirementManagementServiceClient requirementManagementServiceClient) {
		this.requirementManagementServiceClient = requirementManagementServiceClient;
	}

	public List<RequirementSetDTO> getActiveRequirementSets() {
		return this.requirementManagementServiceClient.getRequirementSetsFromRequirementManagement(true);
	}

	public List<AttributeDTO> getActiveAttributes(Long requirementSetId) {
		return getActiveAttributes(requirementSetId, null);
	}

	public List<AttributeKeyDTO> getActiveAttributeKeys(Long requirementSetId, String type) {
		return this.requirementManagementServiceClient.getAttributeKeysFromRequirementManagement(true, type);
	}

	/**
	 * Get active attributes in a given requirement set and with attributes key type
	 * present in a given list of types
	 * 
	 * @param requirementSetId
	 *            the requirement set id
	 * @param types
	 *            the list of attribute key types
	 * @return
	 */
	public List<AttributeDTO> getActiveAttributes(Long requirementSetId, List<AttributeType> types) {
		List<AttributeDTO> result = this.requirementManagementServiceClient
				.getAttributesFromRequirementManagement(true);

		if (result != null) {
			result.removeIf(attributeDTO -> attributeDTO.getAttributeKey() != null
					&& attributeDTO.getAttributeKey().getRequirementSet().getId() != requirementSetId);
			result.removeIf(attributeDTO -> attributeDTO.getAttributeKey() != null
					&& !attributeDTO.getAttributeKey().isActive()); // include only attributes with active attributeKey
																	// or no attributeKey
			if (types != null && types.size() > 0)
				result.removeIf(attributeDTO -> attributeDTO.getAttributeKey() != null
						&& !types.contains(attributeDTO.getAttributeKey().getType()));
		}

		return unflattenAttributeHierarchy(result);
	}

	public List<AttributeDTO> getAttributesByIds(List<Long> ids) throws IDNotFoundException {
		List<AttributeDTO> result = this.requirementManagementServiceClient.getAllAttributesFromRequirementManagement();

		if (result != null)
			result.removeIf(attributeDTO -> !ids.contains(attributeDTO.getId()));

		if (result.size() < ids.size())
			throw new IDNotFoundException(); // 404 if user requested non-existent ids

		if (result != null) {
			result.removeIf(attributeDTO -> !attributeDTO.isActive());
			result.removeIf(attributeDTO -> attributeDTO.getAttributeKey() != null
					&& !attributeDTO.getAttributeKey().isActive()); // include only attributes with active attributeKey
																	// or no attributeKey
		}

		return unflattenAttributeHierarchy(result);
	}

	private List<AttributeDTO> unflattenAttributeHierarchy(List<AttributeDTO> attributeDTOList) {
		List<AttributeDTO> result = new LinkedList<>(attributeDTOList);

		// generate a HashMap to map parent relation in result
		HashMap<Long, List<AttributeDTO>> toTreeHelper = new HashMap<>(); // key: parentId, value: list of children
		for (AttributeDTO attributeDTO : result) {
			if (attributeDTO.getParent() != null) {
				Long parentId = attributeDTO.getParent().getId();
				if (parentId != null) {
					toTreeHelper.putIfAbsent(parentId, new LinkedList<>());
					toTreeHelper.get(parentId).add(attributeDTO);
				}
			}
		}

		// move children to their parent's list of children (but only if result contains
		// both child and parent)
		List<AttributeDTO> toRemove = new LinkedList<>();
		for (AttributeDTO possibleParent : result) {
			List<AttributeDTO> childAttributes = toTreeHelper.get(possibleParent.getId());
			if (childAttributes != null) {
				for (AttributeDTO childAttribute : childAttributes) {
					if (result.contains(childAttribute)) {

						if (possibleParent.getChildren() == null)
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

	/**
	 * Restructure the given list of attributes to be conform with the
	 * {@link GenericAttributeGatewayDTO}
	 * 
	 * @param attributes
	 *            list of attributes
	 * @return the generated restructured list
	 */
	public List<GenericAttributeGatewayDTO> generateGatewayAttributeDTO(List<AttributeDTO> attributes) {
		List<GenericAttributeGatewayDTO> result = new ArrayList<>();

		for (AttributeDTO attrDto : attributes) {
			// Checks whether the attribute key is already in array
			List<GenericAttributeGatewayDTO> foundAttributeKeys = result.stream()
					.filter(x -> x.getId() == attrDto.getAttributeKey().getId()).collect(Collectors.toList());
			GenericAttributeGatewayDTO genericAttribute;

			if (foundAttributeKeys.size() == 0) {
				// add a new entry to result array
				genericAttribute = new GenericAttributeGatewayDTO(attrDto.getAttributeKey().getId(),
						attrDto.getAttributeKey().getName(), attrDto.getAttributeKey().getType(),
						attrDto.getAttributeKey().getDescription(), attrDto.getAttributeKey().getShowOrder());
				List<AttributeDTO> values = new ArrayList<>();
				values.add(attrDto);
				genericAttribute.setValues(values);
				result.add(genericAttribute);
			} else {
				// update the values array (attributes list for particular attribute key)
				genericAttribute = foundAttributeKeys.get(0);
				genericAttribute.getValues().add(attrDto);
			}
		}

		return result;
	}

	public List<ExtensionKeyDTO> getActiveExtensionKeys(Long requirementSetId) {
		List<ExtensionKeyDTO> result = this.requirementManagementServiceClient
				.getAllExtensionKeysFromRequirementManagement(true);
		result.removeIf(ex -> ex.getRequirementSet().getId() != requirementSetId);
		
		if (result.size() == 0) {
			log.error("Empty list of extension key to requirementSetId {}");
			throw new IDNotFoundException();
		}
		
		return result;
	}

	public List<GenericExtensionDTO> getActiveExtensionForReqStructure(Long requirementSetId) {
		List<GenericExtensionDTO> result = this.requirementManagementServiceClient
				.getAllExtensionsFromRequirementManagement(true);
		
		result.removeIf(x -> x.getExtensionKey().getRequirementSet().getId() != requirementSetId);
		
		return result;
	}

	public RequirementStructureDTO generateRequirementStructureDTO(List<ExtensionKeyDTO> extensionKeys,
			List<GenericExtensionDTO> extensions) {
		
		RequirementStructureDTO result = new RequirementStructureDTO(true);
		
		for (ExtensionKeyDTO exKey : extensionKeys) {
			switch (exKey.getSection()) {
			case ENHANCEMENT:
				EnhancementForReqStructureDTO enhacement = new EnhancementForReqStructureDTO(exKey);
				result.getEnhancements().add(enhacement);
				break;

			case STATUS:
				StatusForReqStructureDTO status = new StatusForReqStructureDTO(exKey);
				List<GenericExtensionDTO> statusValues = extensions.stream().filter(ex -> ex.getExtensionKey().getId() == exKey.getId()).collect(Collectors.toList());
				for (GenericExtensionDTO ex: statusValues) {
					ExtensionReqStructureDTO value = new ExtensionReqStructureDTO(ex);
					status.getValues().add(value);
				}
				result.getStatus().add(status); 
				break;
				
			default:
				break;
			}
		}
		return result;
	}

	/**
	 * Adds a AttributeType to the RequirementDTO depending on a given AttributeDTO
	 *
	 * @param requirement
	 *            RequirementDTO we want to add the AttributeType
	 * @param attributeKey
	 *            AttributeKey we use to find out the AttributeType
	 */
	private void addAttributeTypesToRequirement(RequirementDTO requirement, AttributeKeyDTO attributeKey) {

		switch (attributeKey.getType()) {
		case FE_TAG:
			if (requirement.getFeTags() == null) {
				requirement.setFeTags(new ArrayList<>());
			}
			if (!requirement.getFeTags().contains(attributeKey.getId())) {
				requirement.getFeTags().add(attributeKey.getId());
			}
			break;

		case PARAMETER:
			if (requirement.getParameters() == null) {
				requirement.setParameters(new ArrayList<>());
			}
			if (!requirement.getParameters().contains(attributeKey.getId())) {

				requirement.getParameters().add(attributeKey.getId());
			}

			break;

		case CATEGORY:
			requirement.setCatergoryId(attributeKey.getId());
			break;
		}
	}

	/**
	 * Adds Extensions to the RequirementDTO depending on a given ExtensionDTO
	 */

	private void addExtensionsToRequirement(RequirementDTO requirement, ExtensionReqDTO extension) {

		switch (extension.getExtensionKey().getSection()) {
		// When section is "Status"

		case STATUS:

			boolean extensionKeyFound = false;
			boolean extensionFound = false;

			if (requirement.getStatus() == null) {
				requirement.setStatus(new ArrayList<>());
			}

			// search if we have already have the extension Key in Our list
			for (StatusForReqDTO status : requirement.getStatus()) {
				if (status.getKeyId().equals(extension.getExtensionKey().getId())) {
					extensionKeyFound = true;
					break;
				}
			}

			// if we didn't find it yet we add the Extension and the extensionKey
			if (!extensionKeyFound) {
				StatusForReqDTO tempStatus = new StatusForReqDTO();
				tempStatus.setKeyId(extension.getExtensionKey().getId());
				tempStatus.setValues(new ArrayList<>());
				tempStatus.getValues().add(extension.getId());
				requirement.getStatus().add(tempStatus);
			} else {

				// if we already have the extensionKey in our list, we need to check if we
				// have all extension with the extensiionKey
				for (StatusForReqDTO status : requirement.getStatus()) {
					if (status.getKeyId().equals(extension.getExtensionKey().getId())) {
						for (Long value : status.getValues()) {
							if (extension.getId().equals(value)) {
								extensionFound = true;
								break;
							}
						}
						if (!extensionFound) {
							// if the extension isn't in the list we add it
							status.getValues().add(extension.getId());
						}
					}
				}
			}

			break;

		case ENHANCEMENT:

			boolean enhancementExtensionKeyFound = false;
			boolean enhancementExtensionFound = false;

			if (requirement.getEnhancements() == null) {
				requirement.setEnhancements(new ArrayList<>());
			}

			// search if we have already have the extension Key in Our list
			for (EnhancementForReqDTO enhancement : requirement.getEnhancements()) {
				if (enhancement.getKeyId().equals(extension.getExtensionKey().getId())) {
					enhancementExtensionKeyFound = true;
				}
			}
			// if we didn't find it yet we add the Extension and the extensionKey.
			// before adding we build up the content
			if (!enhancementExtensionKeyFound) {
				EnhancementForReqDTO tempEnhancement = new EnhancementForReqDTO();
				GenericExtensionDTO tempExtension = new GenericExtensionDTO();

				tempEnhancement.setKeyId(extension.getExtensionKey().getId());
				tempEnhancement.setContents(new ArrayList<>());

				tempExtension.setId(extension.getId());
				tempExtension.setShowOrder(extension.getShowOrder());
				tempExtension.setContent(extension.getContent());

				tempEnhancement.getContents().add(tempExtension);

				requirement.getEnhancements().add(tempEnhancement);

			} else {
				// if we already have the extensionKey in our list, we need to check if we
				// have all extension with the extensiionKey
				for (EnhancementForReqDTO enhancement : requirement.getEnhancements()) {

					if (enhancement.getKeyId().equals(extension.getExtensionKey().getId())) {
						for (GenericExtensionDTO extensionWithoutDescDTO : enhancement.getContents()) {
							if (extension.getId().equals(extensionWithoutDescDTO.getId())) {
								enhancementExtensionFound = true;
							}
						}
						if (!enhancementExtensionFound) {
							// if the extension isn't in the list we add it
							// before adding we build up the extension
							EnhancementForReqDTO tempEnhancement = new EnhancementForReqDTO();
							GenericExtensionDTO tempExtension = new GenericExtensionDTO();

							tempEnhancement.setKeyId(extension.getExtensionKey().getId());
							tempExtension.setId(extension.getId());
							tempExtension.setContent(extension.getContent());
							tempExtension.setShowOrder(extension.getShowOrder());

							enhancement.getContents().add(tempExtension);
						}
					}
				}
				break;
			}
		}
	}

	/**
	 * Return a List of Requirements which have requested conditions.
	 *
	 * @param requirementSetId
	 *            Id of RequirementSet we want to search for the Requirement
	 * @param parameters
	 *            Id of Parameters the Requirement
	 * @return RequirementsDTO which has all the requested conditions
	 */
	public List<RequirementDTO> getActiveRequirements(Long requirementSetId, List<Long> parameters) {
		/*
		 * TODO: currently we only build up the requirements without paying attention to
		 * the parameters
		 * 
		 */

		HashSet<SkeletonDTO> skeletonHashSet = new HashSet<>();
		HashSet<AttributeDTO> attributeHashSet = new HashSet<>();
		HashSet<ExtensionReqDTO> extensionHashSet = new HashSet<>();
		HashSet<AttributeKeyDTO> attributeKeyHashSet = new HashSet<>();
		HashSet<ExtensionKeyDTO> extensionKeyHashSet = new HashSet<>();
		List<RequirementDTO> result = new ArrayList<>();

		List<SkAtExDTO> skAtExDTOs = this.requirementManagementServiceClient
				.getAllSkAtExFromRequirementManagement(true);
		skAtExDTOs.removeIf(skAtExDTO -> !skAtExDTO.getSkeleton().getRequirementSet().getId().equals(requirementSetId));

		for (SkAtExDTO skAtExDTO : skAtExDTOs) {
			if (skAtExDTO.getSkeleton() != null) {
				skeletonHashSet.add(skAtExDTO.getSkeleton());
			}

			if (skAtExDTO.getAttribute() != null) {
				attributeHashSet.add(skAtExDTO.getAttribute());
			}

			if (skAtExDTO.getAttribute() != null) {
				if (skAtExDTO.getAttribute().getAttributeKey() != null) {
					attributeKeyHashSet.add(skAtExDTO.getAttribute().getAttributeKey());
				}
			}

			if (skAtExDTO.getExtension() != null) {
				if (skAtExDTO.getExtension().getExtensionKey() != null) {
					extensionKeyHashSet.add(skAtExDTO.getExtension().getExtensionKey());
				}
			}

			if (skAtExDTO.getExtension() != null) {
				extensionHashSet.add(skAtExDTO.getExtension());
			}
		}

		for (SkeletonDTO skeletonDTO : skeletonHashSet) {
			RequirementDTO tempRequirement = new RequirementDTO();
			tempRequirement.setId(skeletonDTO.getId());
			tempRequirement.setName(skeletonDTO.getName());
			tempRequirement.setDescription(skeletonDTO.getDescription());
			tempRequirement.setShowOrder(skeletonDTO.getShowOrder());

			for (SkAtExDTO skAtExDTO : skAtExDTOs) {
				if (skAtExDTO.getSkeleton().getId() == skeletonDTO.getId()) {
					if (skAtExDTO.getAttribute() != null && skAtExDTO.getAttribute().getAttributeKey() != null) {
						addAttributeTypesToRequirement(tempRequirement, skAtExDTO.getAttribute().getAttributeKey());
					}

					if (skAtExDTO.getExtension() != null && skAtExDTO.getExtension().getExtensionKey() != null) {
						addExtensionsToRequirement(tempRequirement, skAtExDTO.getExtension());
					}
				}
			}

			// addExtensionsToRequirement(tempRequirement, extensionKeyHashSet,
			// extensionHashSet);
			result.add(tempRequirement);
		}

		return result;
	}

}
