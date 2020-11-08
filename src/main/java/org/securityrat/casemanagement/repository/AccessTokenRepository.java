package org.securityrat.casemanagement.repository;

import org.securityrat.casemanagement.domain.AccessToken;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AccessToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

}
