package org.securityrat.casemanagement.web.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.securityrat.casemanagement.CaseManagementApp;
import org.securityrat.casemanagement.client.RequirementManagementServiceClient;
import org.securityrat.casemanagement.domain.enumeration.AttributeType;
import org.securityrat.casemanagement.service.RequirementManagementAPIService;
import org.securityrat.casemanagement.service.dto.AttributeDTO;
import org.securityrat.casemanagement.service.dto.AttributeKeyDTO;
import org.securityrat.casemanagement.service.dto.RequirementSetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
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

    /**
     * Test for getAttributes()
     * Assure that parent relations are assembled correctly
     */
    @Test
    public void getAttributesTestTrees() {
        Long id = 0L;

        List<AttributeDTO> expectedResult = new ArrayList<>();

        AttributeDTO attributeParent = new AttributeDTO();
        attributeParent.setId(id++);
        attributeParent.setName("Parent");
        expectedResult.add(attributeParent);

        AttributeDTO attributeChild1 = new AttributeDTO();
        attributeChild1.setName("Child 1");
        attributeChild1.setId(id++);
        attributeChild1.setParent(attributeParent);
        expectedResult.add(attributeChild1);

        AttributeDTO attributeChild2 = new AttributeDTO();
        attributeChild2.setName("Child 2");
        attributeChild2.setId(id++);
        attributeChild2.setParent(attributeParent);
        expectedResult.add(attributeChild2);

        AttributeDTO attributeGrandChild1 = new AttributeDTO();
        attributeGrandChild1.setName("Grandchild 1");
        attributeGrandChild1.setId(id++);
        attributeGrandChild1.setParent(attributeChild2);
        expectedResult.add(attributeGrandChild1);

        AttributeDTO attributeGrandChild2 = new AttributeDTO();
        attributeGrandChild2.setName("Grandchild 2");
        attributeGrandChild2.setId(id++);
        attributeGrandChild2.setParent(attributeGrandChild2);
        expectedResult.add(attributeGrandChild2);

        // add a single attribute without parent or children
        AttributeDTO attribute = new AttributeDTO();
        attribute.setName("Another attribute");
        attribute.setParent(null);
        attribute.setId(id++);
        expectedResult.add(attribute);

        // add a attribute with a parent which is not included in the set
        AttributeDTO attributeWithUnknownParent = new AttributeDTO();
        attributeWithUnknownParent.setName("Attribute with unknown parent");
        attributeWithUnknownParent.setId(id++);
        AttributeDTO unknownParent = new AttributeDTO();
        unknownParent.setId(id+999L);
        attributeWithUnknownParent.setParent(unknownParent);
        expectedResult.add(attributeWithUnknownParent);

        given(requirementManagementServiceClient.getAttributesFromRequirementMangament(true))
            .willReturn(expectedResult);

        List<AttributeDTO> result = requirementManagementAPIService.getActiveAttributes(0L);

        // assure that 'children' members contain the correct elements
        for(AttributeDTO attributeDTO : result) {
            List<AttributeDTO> children = attributeDTO.getChildren();
            switch(attributeDTO.getName()) {
                case "Parent":
                    assert children != null;
                    assert children.contains(attributeChild1);
                    assert children.contains(attributeChild2);
                    break;

                case "Child 1":
                case "Grandchild 1":
                case "Grandchild 2":
                case "attribute":
                case "Attribute with unknown parent":
                    assert children == null || children.isEmpty();
                    break;

                case "Child 2":
                    assert children != null;
                    assert children.contains(attributeGrandChild1);
                    assert children.contains(attributeGrandChild2);
                    break;
            }
        }

        // assure that no top level attribute is missing in the result set
        assert result.contains(attributeParent);
        assert result.contains(attribute);
        assert result.contains(attributeWithUnknownParent);

        // assure that children are not included on top level
        assert !result.contains(attributeChild1);
        assert !result.contains(attributeChild2);
        assert !result.contains(attributeGrandChild1);
        assert !result.contains(attributeGrandChild2);
    }

    /**
     * Test for getAttributes()
     * Assure that filtering works correctly
     */
    @Test
    public void getAttributesTestFilter() {
        Long id = 0L;
        Long reqSetIDInFilter = 3L;
        Long reqSetIDNotInFilter = 9L;
        RequirementSetDTO requirementSetInFilter = new RequirementSetDTO();
        requirementSetInFilter.setId(reqSetIDInFilter);
        RequirementSetDTO requirementSetNotInFilter = new RequirementSetDTO();
        requirementSetNotInFilter.setId(reqSetIDNotInFilter);
        List<AttributeType> attributeTypeFilter = new ArrayList<>();
        attributeTypeFilter.add(AttributeType.CATEGORY);
        attributeTypeFilter.add(AttributeType.FE_TAG);
        AttributeType attributeTypeNotInFilter = AttributeType.PARAMETER;

        List<AttributeDTO> expectedResult = new ArrayList<>();

        AttributeDTO attribute1 = new AttributeDTO();
        attribute1.setId(id++);
        attribute1.setName("Attribute with active AttributeKey");
        AttributeKeyDTO attributeKey1 = new AttributeKeyDTO();
        attributeKey1.setActive(true);
        attributeKey1.setRequirementSet(requirementSetInFilter);
        attributeKey1.setType(attributeTypeFilter.get(0));
        attribute1.setAttributeKey(attributeKey1);
        expectedResult.add(attribute1);

        AttributeDTO attribute2 = new AttributeDTO();
        attribute2.setId(id++);
        attribute2.setName("Attribute with active AttributeKey but wrong RequirementSet");
        AttributeKeyDTO attributeKey2 = new AttributeKeyDTO();
        attributeKey2.setActive(true);
        attributeKey2.setRequirementSet(requirementSetNotInFilter);
        attributeKey2.setType(attributeTypeFilter.get(1));
        attribute2.setAttributeKey(attributeKey2);
        expectedResult.add(attribute2);

        AttributeDTO attribute3 = new AttributeDTO();
        attribute3.setId(id++);
        attribute3.setName("Attribute with active AttributeKey but wrong Type");
        AttributeKeyDTO attributeKeyDTO3 = new AttributeKeyDTO();
        attributeKeyDTO3.setActive(true);
        attributeKeyDTO3.setRequirementSet(requirementSetInFilter);
        attributeKeyDTO3.setType(attributeTypeNotInFilter);
        attribute3.setAttributeKey(attributeKeyDTO3);
        expectedResult.add(attribute3);

        given(requirementManagementServiceClient.getAttributesFromRequirementMangament(true))
            .willReturn(expectedResult);

        List<AttributeDTO> result = requirementManagementAPIService
            .getActiveAttributes(3L, attributeTypeFilter);

        assert result.contains(attribute1);
        assert !result.contains(attribute2);
        assert !result.contains(attribute3);
    }

    /*
    @Test
    public void getAttributesCyclicTest() {
        Long id = 0L;

        // add a tree of attributes to the test set
        List<AttributeDTO> expectedResult = new ArrayList<>();
        AttributeDTO attributeParent = new AttributeDTO();
        attributeParent.setId(id++);
        attributeParent.setName("Parent");
        expectedResult.add(attributeParent);

        AttributeDTO attributeChild1 = new AttributeDTO();
        attributeChild1.setName("Child 1");
        attributeChild1.setId(id++);
        attributeChild1.setParentId(attributeParent.getId());
        expectedResult.add(attributeChild1);

        AttributeDTO attributeGrandChild1 = new AttributeDTO();
        attributeGrandChild1.setName("Grandchild 1");
        attributeGrandChild1.setId(id++);
        attributeGrandChild1.setParentId(attributeChild1.getId());
        expectedResult.add(attributeGrandChild1);

        // create a cycle in the tree
        attributeParent.setParentId(attributeGrandChild1.getId());

        given(requirementManagementServiceClient.getAttributesFromRequirementMangament(true))
            .willReturn(expectedResult);

        List<AttributeDTO> result = requirementManagementAPIService.getActiveAttributes(0L);
    }
    */

}
