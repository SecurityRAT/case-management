package org.securityrat.casemanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.securityrat.casemanagement.web.rest.TestUtil;

class TicketSystemInstanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketSystemInstance.class);
        TicketSystemInstance ticketSystemInstance1 = new TicketSystemInstance();
        ticketSystemInstance1.setId(1L);
        TicketSystemInstance ticketSystemInstance2 = new TicketSystemInstance();
        ticketSystemInstance2.setId(ticketSystemInstance1.getId());
        assertThat(ticketSystemInstance1).isEqualTo(ticketSystemInstance2);
        ticketSystemInstance2.setId(2L);
        assertThat(ticketSystemInstance1).isNotEqualTo(ticketSystemInstance2);
        ticketSystemInstance1.setId(null);
        assertThat(ticketSystemInstance1).isNotEqualTo(ticketSystemInstance2);
    }
}
