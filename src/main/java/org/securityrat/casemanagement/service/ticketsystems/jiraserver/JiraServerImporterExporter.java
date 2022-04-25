package org.securityrat.casemanagement.service.ticketsystems.jiraserver;

import org.securityrat.casemanagement.service.interfaces.RequirementImporterExporter;

public class JiraServerImporterExporter implements RequirementImporterExporter {
    @Override
    public boolean hasAuthenticationToken() {
        return false;
    }

    @Override
    public void authenticateUser() {

    }

    @Override
    public boolean isTicketInstanceValid() {
        return false;
    }

    @Override
    public void exportRequirementSet(String requirementSet) {

    }

    @Override
    public void importRequirementSet(String ticketInfo) {

    }
}
