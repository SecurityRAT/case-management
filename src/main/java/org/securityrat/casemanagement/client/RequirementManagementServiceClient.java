package org.securityrat.casemanagement.client;

import java.util.List;

import org.securityrat.casemanagement.service.dto.*;
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

    /**
     * Get a list of Attributes from requirement management, filter by active.
     *
     * @param active Add the given value of type Boolean as a filter for the field 'active' to the request to requirement management
     * @return List of Attributes as returned by requirement management.
     */
    @RequestMapping(value = "/api/attributes")
    List<AttributeDTO> getAttributesFromRequirementManagement(
        @RequestParam(value = "active.equals") Boolean active
    );

    /**
     * Get a list of all Attributes from requirement management.
     *
     * @return List of Attributes as returned by requirement management.
     */
    @RequestMapping(value = "/api/attributes")
    List<AttributeDTO> getAllAttributesFromRequirementManagement();


    /**
     * Get a list of all ExtensionKeys from requirement management.
     *
     * param active Add the given value of type Boolean as a filter for the field 'active' to the request to requirement management
     * @return List of ExtensionKeys as returned by requirement management.
     */
    @RequestMapping(value = "/api/extension-keys")
    List<ExtensionKeyDTO> getAllExtensionKeysFromRequirementManagement(@RequestParam(value = "active.equals") Boolean active);


    /**
     * Get a list of all Skeletons from requirement management.
     *
     * param active Add the given value of type Boolean as a filter for the field 'active' to the request to requirement management
     * @return List of Skeletons as returned by requirement management.
     */
    @RequestMapping(value = "/api/skeletons")
    List<SkeletonDTO> getAllSkeletonsFromRequirementManagement(@RequestParam(value = "active.equals") Boolean active);


    /**
     * Get a list of all Extensions from requirement management.
     *
     * param active Add the given value of type Boolean as a filter for the field 'active' to the request to requirement management
     * @return List of Extensions as returned by requirement management.
     */
    @RequestMapping(value = "/api/extensions")
    List<ExtensionDTO> getAllExtensionsFromRequirementManagement(@RequestParam(value = "active.equals") Boolean active);

    /**
     * Get a list of all SkAtEx from requirement management.
     *
     * param active Add the given value of type Boolean as a filter for the field 'active' to the request to requirement management
     * @return List of SkAtEx as returned by requirement management.
     */
    @RequestMapping(value = "/api/sk-at-exes")
    List<SkAtExDTO> getAllSkAtExFromRequirementManagement(@RequestParam(value = "active.equals") Boolean active);

    /**
     * Get a list of all AttributeKeys from requirement management.
     *
     * param active Add the given value of type Boolean as a filter for the field 'active' to the request to requirement management
     * @return List of AttributeKeys as returned by requirement management.
     */
    @RequestMapping(value = "/api/attribute-keys")
    List<AttributeKeyDTO> getAttributeKeysFromRequirementManagement(@RequestParam(value = "active.equals") Boolean active);


    @RequestMapping(value = "/api/sk-at-exes/{id}")
    SkAtExDTO getSkAtExById(@RequestParam(value = "id") Integer id);

    @RequestMapping(value = "/api/attributes/{id}")
    AttributeDTO getAttributeById(@RequestParam(value = "id") Integer id);

    @RequestMapping(value = "/api/skeletons/{id}")
    SkeletonDTO getSkeletonById(@RequestParam(value = "id") Integer id);

    @RequestMapping(value = "/api/extension-keys/{id}")
    ExtensionKeyDTO getExtensionKeyById(@RequestParam(value = "id") Integer id);


}
