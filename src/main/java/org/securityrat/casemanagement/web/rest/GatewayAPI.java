package org.securityrat.casemanagement.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.securityrat.casemanagement.service.dto.RequirementSetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.securityrat.casemanagement.service.RequirementManagementAPIService;

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
}
