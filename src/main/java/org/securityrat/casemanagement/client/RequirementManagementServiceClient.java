package org.securityrat.casemanagement.client;

import java.util.List;

import org.securityrat.casemanagement.service.dto.RequirementSetDTO;
import org.springframework.web.bind.annotation.*;

@AuthorizedFeignClient(name = "requirementManagement")
public interface RequirementManagementServiceClient {

    /**
     * Get a list of RequirementSets from requirement management.
     *
     * @param active Add the given value of type Boolean as a filter for the field 'active' to the request to requirement management
     * @return List of RequirementSets as returned by requirement management.
     */
    @RequestMapping(value = "/api/requirement-sets")
    List<RequirementSetDTO> getRequirementSetsFromRequirementManagement(
        @RequestParam(value = "active.equals") Boolean active
    );
}
