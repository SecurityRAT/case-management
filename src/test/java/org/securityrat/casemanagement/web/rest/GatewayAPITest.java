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
import org.securityrat.casemanagement.web.rest.errors.IDNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaseManagementApp.class)
public class GatewayAPITest {

    @MockBean
    private RequirementManagementServiceClient requirementManagementServiceClient;

    @Autowired
    private RequirementManagementAPIService requirementManagementAPIService;

    /**
     * Test for /requirementSets
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
        assertTrue(allActive);

        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    /**
     * Test for /attributes
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

        given(requirementManagementServiceClient.getAttributesFromRequirementManagement(true))
            .willReturn(expectedResult);

        List<AttributeDTO> result = requirementManagementAPIService.getActiveAttributes(0L);

        // assure that 'children' members contain the correct elements
        for(AttributeDTO attributeDTO : result) {
            List<AttributeDTO> children = attributeDTO.getChildren();
            switch(attributeDTO.getName()) {
                case "Parent":
                    assertTrue(children != null);
                    assertTrue(children.contains(attributeChild1));
                    assertTrue(children.contains(attributeChild2));
                    break;

                case "Child 1":
                case "Grandchild 1":
                case "Grandchild 2":
                case "attribute":
                case "Attribute with unknown parent":
                    assertTrue(children == null || children.isEmpty());
                    break;

                case "Child 2":
                    assertTrue(children != null);
                    assertTrue(children.contains(attributeGrandChild1));
                    assertTrue(children.contains(attributeGrandChild2));
                    break;
            }
        }

        // assure that no top level attribute is missing in the result set
        assertTrue(result.contains(attributeParent));
        assertTrue(result.contains(attribute));
        assertTrue(result.contains(attributeWithUnknownParent));

        // assure that children are not included on top level
        assertFalse(result.contains(attributeChild1));
        assertFalse(result.contains(attributeChild2));
        assertFalse(result.contains(attributeGrandChild1));
        assertFalse(result.contains(attributeGrandChild2));
    }

    /**
     * Test for /attributes
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

        given(requirementManagementServiceClient.getAttributesFromRequirementManagement(true))
            .willReturn(expectedResult);

        List<AttributeDTO> result = requirementManagementAPIService
            .getActiveAttributes(3L, attributeTypeFilter);

        assertTrue(result.contains(attribute1));
        assertFalse(result.contains(attribute2));
        assertFalse(result.contains(attribute3));
    }

    @Test
    public void getAttributeTest() {
        Long nextId = 0L;
        List<AttributeDTO> resultFromRequirementManagement = new LinkedList<>();
        List<Long> requestedIds = new LinkedList<>();

        // this attribute should be in the result
        AttributeDTO parent = new AttributeDTO();
        parent.setId(nextId++);
        parent.setActive(true);
        resultFromRequirementManagement.add(parent);
        requestedIds.add(parent.getId());

        // this attribute should'nt be in the result (id not requested)
        AttributeDTO idNotRequested = new AttributeDTO();
        idNotRequested.setId(nextId++);
        idNotRequested.setActive(true);
        resultFromRequirementManagement.add(idNotRequested);

        // this attribute should not be in the result (not active)
        AttributeDTO inactiveAttributeDTO = new AttributeDTO();
        inactiveAttributeDTO.setId(nextId++);
        inactiveAttributeDTO.setActive(false);
        resultFromRequirementManagement.add(inactiveAttributeDTO);
        requestedIds.add(inactiveAttributeDTO.getId());

        // add some children to check if unflattening works
        AttributeDTO child1 = new AttributeDTO();
        child1.setActive(true);
        child1.setId(nextId++);
        child1.setParent(parent);
        resultFromRequirementManagement.add(child1);
        requestedIds.add(child1.getId());

        AttributeDTO child2 = new AttributeDTO();
        child2.setActive(true);
        child2.setId(nextId++);
        child2.setParent(parent);
        resultFromRequirementManagement.add(child2);
        requestedIds.add(child2.getId());

        AttributeDTO child1child = new AttributeDTO();
        child1child.setActive(true);
        child1child.setId(nextId++);
        child1child.setParent(child1);
        resultFromRequirementManagement.add(child1child);
        requestedIds.add(child1child.getId());

        given(requirementManagementServiceClient.getAllAttributesFromRequirementManagement())
            .willReturn(resultFromRequirementManagement);

        List<AttributeDTO> result = requirementManagementAPIService
            .getAttributesByIds(requestedIds);

        // assert filtering
        assertTrue(result.contains(parent));
        assertFalse(result.contains(idNotRequested));
        assertFalse(result.contains(inactiveAttributeDTO));

        // assert hierarchy
        assertFalse(result.contains(child1)); // should instead be in parent.children
        assertFalse(result.contains(child2));
        assertFalse(result.contains(child1child));

        for(AttributeDTO attributeDTO : result) {
            if(attributeDTO.getId().equals(parent.getId())) {
                assertTrue(attributeDTO.getChildren().size() == 2);
                AttributeDTO expectedChild1 = attributeDTO.getChildren().get(0);
                AttributeDTO expectedChild2 = attributeDTO.getChildren().get(1);
                assertTrue(expectedChild1 != null && expectedChild2 != null);
                assertTrue(expectedChild1.getChildren().size() == 1);
                AttributeDTO expectedChild1Child = expectedChild1.getChildren().get(0);
                assertTrue(expectedChild1Child != null);
                break;
            }
        }
    }

    @Test(expected = IDNotFoundException.class)
    public void getAttributeExceptionTest() {
        Long nextId = 0L;
        List<AttributeDTO> resultFromRequirementManagement = new LinkedList<>();
        List<Long> requestedIds = new LinkedList<>();

        AttributeDTO attributeDTO1 = new AttributeDTO();
        attributeDTO1.setActive(true);
        attributeDTO1.setId(nextId++);
        resultFromRequirementManagement.add(attributeDTO1);

        AttributeDTO attributeDTO2 = new AttributeDTO();
        attributeDTO2.setActive(true);
        attributeDTO2.setId(nextId++);
        resultFromRequirementManagement.add(attributeDTO2);

        given(requirementManagementServiceClient.getAllAttributesFromRequirementManagement())
            .willReturn(resultFromRequirementManagement);

        requestedIds.add(1L);
        requestedIds.add(123L); // id 123 does not exist

        requirementManagementAPIService.getAttributesByIds(requestedIds);
    }
}
