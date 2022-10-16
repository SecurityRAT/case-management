package org.securityrat.casemanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.securityrat.casemanagement.web.rest.TestUtil.sameInstant;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.securityrat.casemanagement.IntegrationTest;
import org.securityrat.casemanagement.domain.AccessToken;
import org.securityrat.casemanagement.repository.AccessTokenRepository;
import org.securityrat.casemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AccessTokenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccessTokenResourceIT {

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SALT = "AAAAAAAAAA";
    private static final String UPDATED_SALT = "BBBBBBBBBB";

    private static final String DEFAULT_REFRESH_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_REFRESH_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/access-tokens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccessTokenMockMvc;

    private AccessToken accessToken;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccessToken createEntity(EntityManager em) {
        AccessToken accessToken = new AccessToken()
            .token(DEFAULT_TOKEN)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .salt(DEFAULT_SALT)
            .refreshToken(DEFAULT_REFRESH_TOKEN);
        return accessToken;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccessToken createUpdatedEntity(EntityManager em) {
        AccessToken accessToken = new AccessToken()
            .token(UPDATED_TOKEN)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .salt(UPDATED_SALT)
            .refreshToken(UPDATED_REFRESH_TOKEN);
        return accessToken;
    }

    @BeforeEach
    public void initTest() {
        accessToken = createEntity(em);
    }

    @Test
    @Transactional
    void createAccessToken() throws Exception {
        int databaseSizeBeforeCreate = accessTokenRepository.findAll().size();
        // Create the AccessToken
        restAccessTokenMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accessToken))
            )
            .andExpect(status().isCreated());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeCreate + 1);
        AccessToken testAccessToken = accessTokenList.get(accessTokenList.size() - 1);
        assertThat(testAccessToken.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testAccessToken.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testAccessToken.getSalt()).isEqualTo(DEFAULT_SALT);
        assertThat(testAccessToken.getRefreshToken()).isEqualTo(DEFAULT_REFRESH_TOKEN);
    }

    @Test
    @Transactional
    void createAccessTokenWithExistingId() throws Exception {
        // Create the AccessToken with an existing ID
        accessToken.setId(1L);

        int databaseSizeBeforeCreate = accessTokenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccessTokenMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accessToken))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = accessTokenRepository.findAll().size();
        // set the field null
        accessToken.setToken(null);

        // Create the AccessToken, which fails.

        restAccessTokenMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accessToken))
            )
            .andExpect(status().isBadRequest());

        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSaltIsRequired() throws Exception {
        int databaseSizeBeforeTest = accessTokenRepository.findAll().size();
        // set the field null
        accessToken.setSalt(null);

        // Create the AccessToken, which fails.

        restAccessTokenMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accessToken))
            )
            .andExpect(status().isBadRequest());

        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccessTokens() throws Exception {
        // Initialize the database
        accessTokenRepository.saveAndFlush(accessToken);

        // Get all the accessTokenList
        restAccessTokenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accessToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(sameInstant(DEFAULT_EXPIRATION_DATE))))
            .andExpect(jsonPath("$.[*].salt").value(hasItem(DEFAULT_SALT)))
            .andExpect(jsonPath("$.[*].refreshToken").value(hasItem(DEFAULT_REFRESH_TOKEN)));
    }

    @Test
    @Transactional
    void getAccessToken() throws Exception {
        // Initialize the database
        accessTokenRepository.saveAndFlush(accessToken);

        // Get the accessToken
        restAccessTokenMockMvc
            .perform(get(ENTITY_API_URL_ID, accessToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accessToken.getId().intValue()))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN))
            .andExpect(jsonPath("$.expirationDate").value(sameInstant(DEFAULT_EXPIRATION_DATE)))
            .andExpect(jsonPath("$.salt").value(DEFAULT_SALT))
            .andExpect(jsonPath("$.refreshToken").value(DEFAULT_REFRESH_TOKEN));
    }

    @Test
    @Transactional
    void getNonExistingAccessToken() throws Exception {
        // Get the accessToken
        restAccessTokenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccessToken() throws Exception {
        // Initialize the database
        accessTokenRepository.saveAndFlush(accessToken);

        int databaseSizeBeforeUpdate = accessTokenRepository.findAll().size();

        // Update the accessToken
        AccessToken updatedAccessToken = accessTokenRepository.findById(accessToken.getId()).get();
        // Disconnect from session so that the updates on updatedAccessToken are not directly saved in db
        em.detach(updatedAccessToken);
        updatedAccessToken
            .token(UPDATED_TOKEN)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .salt(UPDATED_SALT)
            .refreshToken(UPDATED_REFRESH_TOKEN);

        restAccessTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccessToken.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccessToken))
            )
            .andExpect(status().isOk());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeUpdate);
        AccessToken testAccessToken = accessTokenList.get(accessTokenList.size() - 1);
        assertThat(testAccessToken.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testAccessToken.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testAccessToken.getSalt()).isEqualTo(UPDATED_SALT);
        assertThat(testAccessToken.getRefreshToken()).isEqualTo(UPDATED_REFRESH_TOKEN);
    }

    @Test
    @Transactional
    void putNonExistingAccessToken() throws Exception {
        int databaseSizeBeforeUpdate = accessTokenRepository.findAll().size();
        accessToken.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccessTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accessToken.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accessToken))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccessToken() throws Exception {
        int databaseSizeBeforeUpdate = accessTokenRepository.findAll().size();
        accessToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accessToken))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccessToken() throws Exception {
        int databaseSizeBeforeUpdate = accessTokenRepository.findAll().size();
        accessToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessTokenMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accessToken))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccessTokenWithPatch() throws Exception {
        // Initialize the database
        accessTokenRepository.saveAndFlush(accessToken);

        int databaseSizeBeforeUpdate = accessTokenRepository.findAll().size();

        // Update the accessToken using partial update
        AccessToken partialUpdatedAccessToken = new AccessToken();
        partialUpdatedAccessToken.setId(accessToken.getId());

        partialUpdatedAccessToken.expirationDate(UPDATED_EXPIRATION_DATE).salt(UPDATED_SALT).refreshToken(UPDATED_REFRESH_TOKEN);

        restAccessTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccessToken.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccessToken))
            )
            .andExpect(status().isOk());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeUpdate);
        AccessToken testAccessToken = accessTokenList.get(accessTokenList.size() - 1);
        assertThat(testAccessToken.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testAccessToken.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testAccessToken.getSalt()).isEqualTo(UPDATED_SALT);
        assertThat(testAccessToken.getRefreshToken()).isEqualTo(UPDATED_REFRESH_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdateAccessTokenWithPatch() throws Exception {
        // Initialize the database
        accessTokenRepository.saveAndFlush(accessToken);

        int databaseSizeBeforeUpdate = accessTokenRepository.findAll().size();

        // Update the accessToken using partial update
        AccessToken partialUpdatedAccessToken = new AccessToken();
        partialUpdatedAccessToken.setId(accessToken.getId());

        partialUpdatedAccessToken
            .token(UPDATED_TOKEN)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .salt(UPDATED_SALT)
            .refreshToken(UPDATED_REFRESH_TOKEN);

        restAccessTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccessToken.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccessToken))
            )
            .andExpect(status().isOk());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeUpdate);
        AccessToken testAccessToken = accessTokenList.get(accessTokenList.size() - 1);
        assertThat(testAccessToken.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testAccessToken.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testAccessToken.getSalt()).isEqualTo(UPDATED_SALT);
        assertThat(testAccessToken.getRefreshToken()).isEqualTo(UPDATED_REFRESH_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingAccessToken() throws Exception {
        int databaseSizeBeforeUpdate = accessTokenRepository.findAll().size();
        accessToken.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccessTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accessToken.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accessToken))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccessToken() throws Exception {
        int databaseSizeBeforeUpdate = accessTokenRepository.findAll().size();
        accessToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accessToken))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccessToken() throws Exception {
        int databaseSizeBeforeUpdate = accessTokenRepository.findAll().size();
        accessToken.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessTokenMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accessToken))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccessToken() throws Exception {
        // Initialize the database
        accessTokenRepository.saveAndFlush(accessToken);

        int databaseSizeBeforeDelete = accessTokenRepository.findAll().size();

        // Delete the accessToken
        restAccessTokenMockMvc
            .perform(delete(ENTITY_API_URL_ID, accessToken.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
