package org.securityrat.casemanagement.web.rest;

import org.securityrat.casemanagement.CaseManagementApp;
import org.securityrat.casemanagement.config.TestSecurityConfiguration;
import org.securityrat.casemanagement.domain.AccessToken;
import org.securityrat.casemanagement.repository.UserRepository;
import org.securityrat.casemanagement.repository.AccessTokenRepository;
import org.securityrat.casemanagement.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.securityrat.casemanagement.web.rest.TestUtil.sameInstant;
import static org.securityrat.casemanagement.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AccessTokenResource} REST controller.
 */
@SpringBootTest(classes = {CaseManagementApp.class, TestSecurityConfiguration.class})
public class AccessTokenResourceIT {

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private UserRepository appUserRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restAccessTokenMockMvc;

    private AccessToken accessToken;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AccessTokenResource accessTokenResource = new AccessTokenResource(accessTokenRepository, appUserRepository);
        this.restAccessTokenMockMvc = MockMvcBuilders.standaloneSetup(accessTokenResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccessToken createEntity(EntityManager em) {
        AccessToken accessToken = new AccessToken()
            .token(DEFAULT_TOKEN)
            .expirationDate(DEFAULT_EXPIRATION_DATE);
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
            .expirationDate(UPDATED_EXPIRATION_DATE);
        return accessToken;
    }

    @BeforeEach
    public void initTest() {
        accessToken = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccessToken() throws Exception {
        int databaseSizeBeforeCreate = accessTokenRepository.findAll().size();

        // Create the AccessToken
        restAccessTokenMockMvc.perform(post("/api/access-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accessToken)))
            .andExpect(status().isCreated());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeCreate + 1);
        AccessToken testAccessToken = accessTokenList.get(accessTokenList.size() - 1);
        assertThat(testAccessToken.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testAccessToken.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void createAccessTokenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accessTokenRepository.findAll().size();

        // Create the AccessToken with an existing ID
        accessToken.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccessTokenMockMvc.perform(post("/api/access-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accessToken)))
            .andExpect(status().isBadRequest());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = accessTokenRepository.findAll().size();
        // set the field null
        accessToken.setToken(null);

        // Create the AccessToken, which fails.

        restAccessTokenMockMvc.perform(post("/api/access-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accessToken)))
            .andExpect(status().isBadRequest());

        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccessTokens() throws Exception {
        // Initialize the database
        accessTokenRepository.saveAndFlush(accessToken);

        // Get all the accessTokenList
        restAccessTokenMockMvc.perform(get("/api/access-tokens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accessToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(sameInstant(DEFAULT_EXPIRATION_DATE))));
    }
    
    @Test
    @Transactional
    public void getAccessToken() throws Exception {
        // Initialize the database
        accessTokenRepository.saveAndFlush(accessToken);

        // Get the accessToken
        restAccessTokenMockMvc.perform(get("/api/access-tokens/{id}", accessToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accessToken.getId().intValue()))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN))
            .andExpect(jsonPath("$.expirationDate").value(sameInstant(DEFAULT_EXPIRATION_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingAccessToken() throws Exception {
        // Get the accessToken
        restAccessTokenMockMvc.perform(get("/api/access-tokens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccessToken() throws Exception {
        // Initialize the database
        accessTokenRepository.saveAndFlush(accessToken);

        int databaseSizeBeforeUpdate = accessTokenRepository.findAll().size();

        // Update the accessToken
        AccessToken updatedAccessToken = accessTokenRepository.findById(accessToken.getId()).get();
        // Disconnect from session so that the updates on updatedAccessToken are not directly saved in db
        em.detach(updatedAccessToken);
        updatedAccessToken
            .token(UPDATED_TOKEN)
            .expirationDate(UPDATED_EXPIRATION_DATE);

        restAccessTokenMockMvc.perform(put("/api/access-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccessToken)))
            .andExpect(status().isOk());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeUpdate);
        AccessToken testAccessToken = accessTokenList.get(accessTokenList.size() - 1);
        assertThat(testAccessToken.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testAccessToken.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingAccessToken() throws Exception {
        int databaseSizeBeforeUpdate = accessTokenRepository.findAll().size();

        // Create the AccessToken

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccessTokenMockMvc.perform(put("/api/access-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accessToken)))
            .andExpect(status().isBadRequest());

        // Validate the AccessToken in the database
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAccessToken() throws Exception {
        // Initialize the database
        accessTokenRepository.saveAndFlush(accessToken);

        int databaseSizeBeforeDelete = accessTokenRepository.findAll().size();

        // Delete the accessToken
        restAccessTokenMockMvc.perform(delete("/api/access-tokens/{id}", accessToken.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccessToken> accessTokenList = accessTokenRepository.findAll();
        assertThat(accessTokenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
