package org.securityrat.casemanagement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.securityrat.casemanagement.web.rest.TestUtil;

public class AccessTokenTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccessToken.class);
        AccessToken accessToken1 = new AccessToken();
        accessToken1.setId(1L);
        AccessToken accessToken2 = new AccessToken();
        accessToken2.setId(accessToken1.getId());
        assertThat(accessToken1).isEqualTo(accessToken2);
        accessToken2.setId(2L);
        assertThat(accessToken1).isNotEqualTo(accessToken2);
        accessToken1.setId(null);
        assertThat(accessToken1).isNotEqualTo(accessToken2);
    }
}
