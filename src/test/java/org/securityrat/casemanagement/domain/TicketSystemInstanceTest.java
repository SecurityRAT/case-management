package org.securityrat.casemanagement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.securityrat.casemanagement.web.rest.TestUtil;

public class TicketSystemInstanceTest {

    @Test
    public void equalsVerifier() throws Exception {
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
