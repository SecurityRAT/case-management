package org.securityrat.casemanagement.service.ticketsystems.jira;

import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.securityrat.casemanagement.service.interfaces.RequirementImporterExporter;

public class JiraServerRequirementImporterExporter extends AbstractJiraRestClient {

    private final TicketSystemInstance ticketSystemInstance;
    public JiraServerRequirementImporterExporter(TicketSystemInstance ticketSystemInstance) {
        this.ticketSystemInstance = ticketSystemInstance;
    }
    @Override
    public void exportRequirementSet(String requirementSet) {
        // No implementation requirement for now.
    }

    @Override
    public void importRequirementSet(String ticketInfo) {
        // No implementation requirement for now.
    }

    public String createTicket() {

        return "ok";
    }
}
