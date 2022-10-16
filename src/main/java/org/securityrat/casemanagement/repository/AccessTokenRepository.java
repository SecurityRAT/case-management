package org.securityrat.casemanagement.repository;

import org.securityrat.casemanagement.domain.AccessToken;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the AccessToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    @Query("select accessToken from AccessToken accessToken where accessToken.user.login = ?#{principal.preferredUsername}")
    List<AccessToken> findByUserIsCurrentUser();

}
