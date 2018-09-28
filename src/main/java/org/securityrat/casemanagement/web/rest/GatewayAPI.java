package org.securityrat.casemanagement.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.securityrat.casemanagement.domain.enumeration.AttributeType;
import org.securityrat.casemanagement.service.RequirementManagementAPIService;
import org.securityrat.casemanagement.service.dto.AttributeDTO;
import org.securityrat.casemanagement.service.dto.RequirementDTO;
import org.securityrat.casemanagement.service.dto.RequirementSetDTO;
import org.securityrat.casemanagement.service.dto.SkAtExDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
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
     * @return the ResponseEntity with status 200 (OK) and the list of requirementSets in body
     */
    @GetMapping("/requirementSets")
    @Timed
    public ResponseEntity<List<RequirementSetDTO>> getActiveRequirementSets() {
        log.debug("REST request to get active RequirementSets");

        List<RequirementSetDTO> result = requirementManagementAPIService.getActiveRequirementSets();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET /attributes : Get active attributes for a given requirementSet and (optionally) attribute key types.
     *
     * @param requirementSetId Return only attributes related to the requirement set with this ID.
     * @param attributeTypes If not empty: Return only attributes which match one of the given types.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of attributes in body
     */
    @GetMapping("/attributes")
    @Timed
    public ResponseEntity<List<AttributeDTO>> getActiveAttributes(
        @RequestParam(value = "requirementSet") Long requirementSetId,
        @RequestParam(value = "types", required = false) String attributeTypes
    ) {
        log.debug("REST request to get active Attributes for RequirementSet with ID " + requirementSetId
            + " of types " + attributeTypes);

        List<AttributeType> type = new LinkedList<>();
        if(attributeTypes != null) {
            for(String typeAsString : attributeTypes.split(",")) {
                 type.add(AttributeType.valueOf(typeAsString));
            }
        }
        List<AttributeDTO> result = requirementManagementAPIService.getActiveAttributes(requirementSetId, type);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET /attribute : Get active attributes with given ids.
     *
     * @param attributeIds A list of ids (comma separated string)
     * @return the ResponseEntity with status 200 (OK) and the list of attributes in body
     *         or 404 (Not found) if one of the specified IDs does not exist
     */
    @GetMapping("/attribute")
    @Timed
    public ResponseEntity<List<AttributeDTO>> getAttribute(@RequestParam(value = "ids") String attributeIds) {
        log.debug("REST request to get Attributes with IDs " + attributeIds);

        List<Long> ids = new LinkedList<>();
        if(attributeIds != null) {
            for(String idAsString : attributeIds.split(",")) {
                ids.add(Long.parseLong(idAsString));
            }
        }
        List<AttributeDTO> result;
        result = requirementManagementAPIService.getAttributesByIds(ids); // may throw IDNotFoundException (-> 404)

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * GET /requirements : Get active attributes with given ids.
     *
     * @param requirementSetId Id of requirementSet
     * @param List of parameter ids (comma seperated string)
     * @return the ResponseEntity with status 200 (OK) and the list of requirements in body
     *         or 404 (Not found) if one of the specified IDs does not exist
     */
    @GetMapping("/requirements")
    @Timed
    public ResponseEntity<List<RequirementDTO>> getRequirements(
    @RequestParam(value = "requirementSet") Long requirementSetId,
    @RequestParam(value = "parameters") String parameters) {
        List<Long> params = new LinkedList<>();
        if(parameters != null) {
            for(String idAsString : parameters.split(",")) {
                params.add(Long.parseLong(idAsString));
            }
        }
        List<RequirementDTO> result;
       result = requirementManagementAPIService.getActiveRequirements(requirementSetId,params);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
