package org.securityrat.casemanagement.service.interfaces;

// todo Review all methods and check whether all parameters and return types are correct
public interface RequirementImporterExporter {

    // todo: Change the return Type once DTO is available
    void exportRequirementSet(String requirementSet);

    // todo: update argument and return type once DTO is available
    void importRequirementSet(String ticketInfo);
}
