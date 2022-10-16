package org.securityrat.casemanagement.repository;

import java.util.List;
import org.securityrat.casemanagement.domain.AccessToken;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AccessToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    @Query("select accessToken from AccessToken accessToken where accessToken.user.login = ?#{principal.preferredUsername}")
    List<AccessToken> findByUserIsCurrentUser();
}
