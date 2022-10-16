package org.securityrat.casemanagement.repository;

import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TicketSystemInstance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketSystemInstanceRepository extends JpaRepository<TicketSystemInstance, Long> {}
