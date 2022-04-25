package org.securityrat.casemanagement.repository;

import org.securityrat.casemanagement.domain.AccessToken;
import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.securityrat.casemanagement.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


/**
 * Spring Data  repository for the AccessToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    @Query("SELECT distinct accessToken FROM AccessToken " +
        "WHERE accessToken.ticketInstance = :ticketSystemInstance" +
        "AND accessToken.user = :user")
    Set<AccessToken> findAccessTokenByTicketSystemInstanceAndUser(
        @Param("ticketSystemInstance") TicketSystemInstance ticketSystemInstance,
        @Param("user") User user);
}
