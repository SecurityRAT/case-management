package org.securityrat.casemanagement.web.rest;


import io.micrometer.core.annotation.Timed;
import org.securityrat.casemanagement.domain.enumeration.AttributeType;
import org.securityrat.casemanagement.domain.enumeration.ExtensionSection;
import org.securityrat.casemanagement.service.RequirementManagementAPIService;
import org.securityrat.casemanagement.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GatewayAPI {
	private final Logger log = LoggerFactory.getLogger(GatewayAPI.class);

	@Autowired
	private RequirementManagementAPIService requirementManagementAPIService;

	/**
	 * GET /requirementSets : Get active requirementSets.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         requirementSets in body
	 */

	@GetMapping("/requirementSets")
	@Timed
	public ResponseEntity<List<RequirementSetDTO>> getActiveRequirementSets() {
		log.debug("REST request to get active RequirementSets");

		List<RequirementSetDTO> result = requirementManagementAPIService.getActiveRequirementSets();
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * GET /parameters?requirementSet : Get parameters for a given requirementSet.
	 *
	 * @param requirementSetId
	 *            The requirementSet ID.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of parameters in
	 *         body
	 */
	@GetMapping(value="/parameters", params="requirementSet")
	@Timed
	public ResponseEntity<List<GenericAttributeGatewayDTO>> getParameters(
			@RequestParam(value = "requirementSet") Long requirementSetId) {
		log.info("REST request to get all active parameter for RequirementSet with ID {}", requirementSetId);

		return new ResponseEntity<>(this.getAttributes(requirementSetId, AttributeType.PARAMETER), HttpStatus.OK);
	}

	/**
	 * GET /parameters?ids : Get parameters of the given parameter ids.
	 *
	 * @param paramterValueIds
	 *            List of parameter ids.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of parameters in
	 *         body
	 */
	@GetMapping(value="/parameters", params="ids")
	@Timed
	public ResponseEntity<List<GenericAttributeGatewayDTO>> getParametersByIds(
			@RequestParam(value = "ids") String paramterValueIds) {
		log.info("REST request to get all active parameters in a given list of ids");
		List<Long> ids = this.parseStringToList(paramterValueIds);
		List<AttributeDTO> attributes = requirementManagementAPIService.getAttributesByIds(ids);

		List<GenericAttributeGatewayDTO> result = requirementManagementAPIService
				.generateGatewayAttributeDTO(attributes);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

    /**
     * GET /attributeKeys?requirementSet&type : Get list of attribute keys in a given requirement set.
     *
     * @param requirementSetId
     *            RequirementSet id.
     * @param type
     *            attributeKey type
     *
     * @return the ResponseEntity with status 200 (OK) and the list of attributeKeys in
     *         body
     */
    @GetMapping(value="/attributeKeys", params = {"requirementSet", "type"})
    @Timed
    public ResponseEntity<List<AttributeKeyDTO>> getAttributeKeysByRequirementSet(
        @RequestParam(value = "requirementSet") Long requirementSetId,
        @RequestParam(value= "type") String type) {
        log.info("REST request to get attributeKeys active parameters for a given requirementSet");
        List<AttributeKeyDTO> attributeKeys = requirementManagementAPIService.getAttributeKeysByRequirementSet(requirementSetId, type);

        // implement GenericAttributeKeyGatewayDTO if necessary
        //List<GenericAttributeKeyGatewayDTO> result = requirementManagementAPIService
        //    .generateGatewayAttributeKeyDTO(attributeKeys);
        return new ResponseEntity<>(attributeKeys, HttpStatus.OK);
    }

    /**
     * GET /attributeKeys?ids : Get list of attribute keys with given ids.
     *
     * @param
     *            attributeKeyIds ids
     *
     * @return the ResponseEntity with status 200 (OK) and the list of attributeKeys in
     *         body
     */
    @GetMapping(value="/attributeKeys", params="ids")
    @Timed
    public ResponseEntity<List<AttributeKeyDTO>> getAttributeKeysByIds(
        @RequestParam(value= "ids") String attributeKeyIds) {
        log.info("REST request to get all active attributeKeys in a given list of ids {}", attributeKeyIds);
        List<Long> ids = this.parseStringToList(attributeKeyIds);
        List<AttributeKeyDTO> attributeKeys = requirementManagementAPIService.getAttributeKeysByIds(ids);
        return new ResponseEntity<>(attributeKeys, HttpStatus.OK);
    }

    /**
     * GET /attributes?requirementSet&type : Get list of attributes in a given requirement set.
     *
     * @param requirementSetId
     *            RequirementSet id.
     * @param type
     *            attribute type
     *
     * @return the ResponseEntity with status 200 (OK) and the list of attributes in
     *         body
     */
    @GetMapping(value="/attributes")
    @Timed
    public ResponseEntity<List<GenericAttributeGatewayDTO>> getAttributesByRequirementSet(
        @RequestParam(value = "requirementSet") Long requirementSetId,
        @RequestParam(value= "type") AttributeType type) {
        log.info("REST request to get active attributes for RequirementSet with ID {} and type {}", requirementSetId, type);
        return new ResponseEntity<>(this.getAttributes(requirementSetId, type), HttpStatus.OK);
    }

    /**
     * GET /attributes?ids : Get list of attributes by ids.
     *
     * @param attributeIds
     *            Attribute ids.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of attributes in
     *         body
     */
    @GetMapping(value="/attributes", params="ids")
    @Timed
    public ResponseEntity<List<GenericAttributeGatewayDTO>> getAttributesByIds(
        @RequestParam(value = "ids") String attributeIds) {
        log.info("REST request to get all active attributes in a given list of ids {}", attributeIds);
        List<Long> ids = this.parseStringToList(attributeIds);
        List<AttributeDTO> attributes = requirementManagementAPIService.getAttributesByIds(ids);

        List<GenericAttributeGatewayDTO> result = requirementManagementAPIService
            .generateGatewayAttributeDTO(attributes);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

	/**
	 * GET /parameters?requirementSet : Get all tags for a given requirementSet.
	 *
	 * @param requirementSetId
	 *            The requirementSet ID.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of tags in
	 *         body
	 */
	@GetMapping("/tags")
	@Timed
	public ResponseEntity<List<GenericAttributeGatewayDTO>> getTags(
			@RequestParam(value = "requirementSet") Long requirementSetId) {
		log.info("REST request to get all active tags for RequirementSet with ID {}", requirementSetId);
		return new ResponseEntity<>(this.getAttributes(requirementSetId, AttributeType.FE_TAG), HttpStatus.OK);
	}

	/**
	 * GET /parameters?requirementSet : Get all categories for a given requirementSet.
	 *
	 * @param requirementSetId
	 *            The requirementSet ID.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of categories in
	 *         body
	 */
	@GetMapping("/categories")
	@Timed
	public ResponseEntity<List<GenericAttributeGatewayDTO>> getCategories(
			@RequestParam(value = "requirementSet") Long requirementSetId) {
		log.info("REST request to get all active parameter for RequirementSet with ID {}", requirementSetId);

		return new ResponseEntity<>(this.getAttributes(requirementSetId, AttributeType.CATEGORY), HttpStatus.OK);
	}

	/**
	 * GET /requirementStructure?requirementSet : Retrieves the requirement table structure of a given requirement set.
	 * @param requirementSetId the requirementSet ID
	 * @return
	 */
	@GetMapping("/requirementStructure")
	public ResponseEntity<RequirementStructureDTO> getRequirementStructure(
		@RequestParam(value = "requirementSet") Long requirementSetId
	) {
		List<ExtensionKeyDTO> extensionKeys = requirementManagementAPIService.getActiveExtensionKeys(requirementSetId);
		List<GenericExtensionDTO> extensions = requirementManagementAPIService.getActiveExtensionForReqStructure(requirementSetId);


		return new ResponseEntity<>(this.requirementManagementAPIService.generateRequirementStructureDTO(extensionKeys, extensions), HttpStatus.OK);
	}

	/**
	 * GET /requirements : Get active attributes with given ids.
	 *
	 * @param requirementSetId
	 *            Id of requirementSet
	 * @param attributeIds
	 *            list of parameter ids (comma seperated string)
	 * @return the ResponseEntity with status 200 (OK) and the list of requirements
	 *         in body or 404 (Not found) if one of the specified IDs does not exist
	 */
	@GetMapping("/requirements")
	@Timed
	public ResponseEntity<List<RequirementDTO>> getRequirements(
			@RequestParam(value = "requirementSet") Long requirementSetId,
			@RequestParam(value = "attributeIds") String attributeIds) {
		List<Long> attributeIdsList = new ArrayList<>();
		if (attributeIds != null) {
			for (String idAsString : attributeIds.split(",")) {
                attributeIdsList.add(Long.parseLong(idAsString));
			}
		}
		List<RequirementDTO> result;
		result = requirementManagementAPIService.getActiveRequirements(requirementSetId, attributeIdsList);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

    /**
     * GET /enhancements : Get enhancements for given requirementSet.
     *
     * @param requirementSetId
     *            Id of requirementSet
     *
     * @return the ResponseEntity with status 200 (OK) and the list of (ExtentionKey) enahncements
     *         in body or 404 (Not found) if one of the specified IDs does not exist
     */
    @GetMapping("/enhancements")
    @Timed
    public ResponseEntity<List<ExtensionKeyDTO>> getEnhancements(
        @RequestParam(value = "requirementSet") Long requirementSetId) {

        List<ExtensionKeyDTO> result;
        result = requirementManagementAPIService.getActiveExtensionKeysOfExtensionSection(requirementSetId, ExtensionSection.ENHANCEMENT);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET /status : Get status for given requirementSet.
     *
     * @param requirementSetId
     *            Id of requirementSet
     *
     * @return the ResponseEntity with status 200 (OK) and the list of (ExtentionKey) status
     *         in body or 404 (Not found) if one of the specified IDs does not exist
     */
    @GetMapping("/status")
    @Timed
    public ResponseEntity<List<ExtensionKeyDTO>> getStatus(
        @RequestParam(value = "requirementSet") Long requirementSetId) {

        List<ExtensionKeyDTO> result;
        result = requirementManagementAPIService.getActiveExtensionKeysOfExtensionSection(requirementSetId, ExtensionSection.STATUS);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }



    /**
	 * Parse a comma separated string to long values.
	 * @param values comma separated string
	 * @return
	 */
	private List<Long> parseStringToList(String values) {
		List<Long> result = new ArrayList<>();
		for (String value: values.split(",")) {
			result.add(Long.parseLong(value));
		}

		return result;
	}

	private List<GenericAttributeGatewayDTO> getAttributes(Long requirementSetId, AttributeType type) {
		List<AttributeType> types = new ArrayList<>();
		types.add(type);
		List<AttributeDTO> attributes = requirementManagementAPIService.getActiveAttributes(requirementSetId, types);

		return requirementManagementAPIService.generateGatewayAttributeDTO(attributes);
	}




}
