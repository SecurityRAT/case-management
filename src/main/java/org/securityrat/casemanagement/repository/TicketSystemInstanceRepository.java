package org.securityrat.casemanagement.repository;

import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


/**
 * Spring Data  repository for the TicketSystemInstance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketSystemInstanceRepository extends JpaRepository<TicketSystemInstance, Long> {

    @Query("SELECT distinct ts FROM TicketSystemInstance ts WHERE ts.url LIKE :url%")
    Optional<TicketSystemInstance> findTicketSystemInstanceByUrl(@Param("url") String url);
}
