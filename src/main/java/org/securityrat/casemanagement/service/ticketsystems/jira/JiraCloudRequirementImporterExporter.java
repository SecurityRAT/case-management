package org.securityrat.casemanagement.service.ticketsystems.jira;

import org.securityrat.casemanagement.service.interfaces.RequirementImporterExporter;

public class JiraCloudRequirementImporterExporter extends AbstractJiraRestClient {
    @Override
    public void exportRequirementSet(String requirementSet) {
        // No implementation requirement for now.
    }

    @Override
    public void importRequirementSet(String ticketInfo) {
        // No implementation requirement for now.
    }
}
