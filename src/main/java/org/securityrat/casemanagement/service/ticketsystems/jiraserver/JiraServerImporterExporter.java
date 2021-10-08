package org.securityrat.casemanagement.service.ticketsystems.jiraserver;

import org.securityrat.casemanagement.service.interfaces.RequirementImporterExporter;

public class JiraServerImporterExporter implements RequirementImporterExporter {
    @Override
    public boolean hasAuthenticationTokens() {
        return false;
    }

    @Override
    public void authenticateUserInTicketSystem() {

    }

    @Override
    public void exportRequirementSet(String requirementSet) {

    }

    @Override
    public void importRequirementSet(String ticketInfo) {

    }
}
