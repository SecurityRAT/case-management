package org.securityrat.casemanagement.service;

import org.securityrat.casemanagement.service.dto.RequirementSetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.securityrat.casemanagement.client.RequirementManagementServiceClient;

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
}
