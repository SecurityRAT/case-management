package org.securityrat.casemanagement.web.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.securityrat.casemanagement.CaseManagementApp;
import org.securityrat.casemanagement.client.RequirementManagementServiceClient;
import org.securityrat.casemanagement.service.RequirementManagementAPIService;
import org.securityrat.casemanagement.service.dto.RequirementSetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaseManagementApp.class)
public class GatewayAPITest {

    @MockBean
    private RequirementManagementServiceClient requirementManagementServiceClient;

    @Autowired
    private RequirementManagementAPIService requirementManagementAPIService;

    /**
     * Test for getActiveRequirementSet()
     */
    @Test
    public void getActiveRequirementSetTest() {
        List<RequirementSetDTO> expectedResult = new ArrayList<>();
        RequirementSetDTO dto1 = new RequirementSetDTO();
        dto1.setActive(true);
        expectedResult.add(dto1);
        RequirementSetDTO dto2 = new RequirementSetDTO();
        dto2.setActive(true);
        expectedResult.add(dto2);

        given(requirementManagementServiceClient.getRequirementSetsFromRequirementManagement(true))
            .willReturn(expectedResult);

        List<RequirementSetDTO> result = requirementManagementAPIService.getActiveRequirementSets();

        boolean allActive = true;
        for (RequirementSetDTO requirementSetDTO : result) {
            if (!requirementSetDTO.isActive())
                allActive = false;
        }
        assert (allActive);

        assert(result.contains(dto1));
        assert(result.contains(dto2));
    }
    }

}
