package org.securityrat.casemanagement.web.rest;

import org.securityrat.casemanagement.CaseManagementApp;
import org.securityrat.casemanagement.config.TestSecurityConfiguration;
import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.securityrat.casemanagement.repository.TicketSystemInstanceRepository;
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
import java.util.List;

import static org.securityrat.casemanagement.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.securityrat.casemanagement.domain.enumeration.TicketSystem;
/**
 * Integration tests for the {@link TicketSystemInstanceResource} REST controller.
 */
@SpringBootTest(classes = {CaseManagementApp.class, TestSecurityConfiguration.class})
public class TicketSystemInstanceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final TicketSystem DEFAULT_TYPE = TicketSystem.JIRA;
    private static final TicketSystem UPDATED_TYPE = TicketSystem.JIRA;

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CONSUMER_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONSUMER_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_SECRET = "BBBBBBBBBB";

    @Autowired
    private TicketSystemInstanceRepository ticketSystemInstanceRepository;

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

    private MockMvc restTicketSystemInstanceMockMvc;

    private TicketSystemInstance ticketSystemInstance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TicketSystemInstanceResource ticketSystemInstanceResource = new TicketSystemInstanceResource(ticketSystemInstanceRepository);
        this.restTicketSystemInstanceMockMvc = MockMvcBuilders.standaloneSetup(ticketSystemInstanceResource)
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
    public static TicketSystemInstance createEntity(EntityManager em) {
        TicketSystemInstance ticketSystemInstance = new TicketSystemInstance()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .url(DEFAULT_URL)
            .consumerKey(DEFAULT_CONSUMER_KEY)
            .clientId(DEFAULT_CLIENT_ID)
            .clientSecret(DEFAULT_CLIENT_SECRET);
        return ticketSystemInstance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketSystemInstance createUpdatedEntity(EntityManager em) {
        TicketSystemInstance ticketSystemInstance = new TicketSystemInstance()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .url(UPDATED_URL)
            .consumerKey(UPDATED_CONSUMER_KEY)
            .clientId(UPDATED_CLIENT_ID)
            .clientSecret(UPDATED_CLIENT_SECRET);
        return ticketSystemInstance;
    }

    @BeforeEach
    public void initTest() {
        ticketSystemInstance = createEntity(em);
    }

    @Test
    @Transactional
    public void createTicketSystemInstance() throws Exception {
        int databaseSizeBeforeCreate = ticketSystemInstanceRepository.findAll().size();

        // Create the TicketSystemInstance
        restTicketSystemInstanceMockMvc.perform(post("/api/ticket-system-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance)))
            .andExpect(status().isCreated());

        // Validate the TicketSystemInstance in the database
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeCreate + 1);
        TicketSystemInstance testTicketSystemInstance = ticketSystemInstanceList.get(ticketSystemInstanceList.size() - 1);
        assertThat(testTicketSystemInstance.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTicketSystemInstance.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTicketSystemInstance.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testTicketSystemInstance.getConsumerKey()).isEqualTo(DEFAULT_CONSUMER_KEY);
        assertThat(testTicketSystemInstance.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testTicketSystemInstance.getClientSecret()).isEqualTo(DEFAULT_CLIENT_SECRET);
    }

    @Test
    @Transactional
    public void createTicketSystemInstanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ticketSystemInstanceRepository.findAll().size();

        // Create the TicketSystemInstance with an existing ID
        ticketSystemInstance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketSystemInstanceMockMvc.perform(post("/api/ticket-system-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance)))
            .andExpect(status().isBadRequest());

        // Validate the TicketSystemInstance in the database
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketSystemInstanceRepository.findAll().size();
        // set the field null
        ticketSystemInstance.setType(null);

        // Create the TicketSystemInstance, which fails.

        restTicketSystemInstanceMockMvc.perform(post("/api/ticket-system-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance)))
            .andExpect(status().isBadRequest());

        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketSystemInstanceRepository.findAll().size();
        // set the field null
        ticketSystemInstance.setUrl(null);

        // Create the TicketSystemInstance, which fails.

        restTicketSystemInstanceMockMvc.perform(post("/api/ticket-system-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance)))
            .andExpect(status().isBadRequest());

        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTicketSystemInstances() throws Exception {
        // Initialize the database
        ticketSystemInstanceRepository.saveAndFlush(ticketSystemInstance);

        // Get all the ticketSystemInstanceList
        restTicketSystemInstanceMockMvc.perform(get("/api/ticket-system-instances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketSystemInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].consumerKey").value(hasItem(DEFAULT_CONSUMER_KEY)))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID)))
            .andExpect(jsonPath("$.[*].clientSecret").value(hasItem(DEFAULT_CLIENT_SECRET)));
    }
    
    @Test
    @Transactional
    public void getTicketSystemInstance() throws Exception {
        // Initialize the database
        ticketSystemInstanceRepository.saveAndFlush(ticketSystemInstance);

        // Get the ticketSystemInstance
        restTicketSystemInstanceMockMvc.perform(get("/api/ticket-system-instances/{id}", ticketSystemInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ticketSystemInstance.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.consumerKey").value(DEFAULT_CONSUMER_KEY))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID))
            .andExpect(jsonPath("$.clientSecret").value(DEFAULT_CLIENT_SECRET));
    }

    @Test
    @Transactional
    public void getNonExistingTicketSystemInstance() throws Exception {
        // Get the ticketSystemInstance
        restTicketSystemInstanceMockMvc.perform(get("/api/ticket-system-instances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTicketSystemInstance() throws Exception {
        // Initialize the database
        ticketSystemInstanceRepository.saveAndFlush(ticketSystemInstance);

        int databaseSizeBeforeUpdate = ticketSystemInstanceRepository.findAll().size();

        // Update the ticketSystemInstance
        TicketSystemInstance updatedTicketSystemInstance = ticketSystemInstanceRepository.findById(ticketSystemInstance.getId()).get();
        // Disconnect from session so that the updates on updatedTicketSystemInstance are not directly saved in db
        em.detach(updatedTicketSystemInstance);
        updatedTicketSystemInstance
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .url(UPDATED_URL)
            .consumerKey(UPDATED_CONSUMER_KEY)
            .clientId(UPDATED_CLIENT_ID)
            .clientSecret(UPDATED_CLIENT_SECRET);

        restTicketSystemInstanceMockMvc.perform(put("/api/ticket-system-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTicketSystemInstance)))
            .andExpect(status().isOk());

        // Validate the TicketSystemInstance in the database
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeUpdate);
        TicketSystemInstance testTicketSystemInstance = ticketSystemInstanceList.get(ticketSystemInstanceList.size() - 1);
        assertThat(testTicketSystemInstance.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTicketSystemInstance.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTicketSystemInstance.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testTicketSystemInstance.getConsumerKey()).isEqualTo(UPDATED_CONSUMER_KEY);
        assertThat(testTicketSystemInstance.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testTicketSystemInstance.getClientSecret()).isEqualTo(UPDATED_CLIENT_SECRET);
    }

    @Test
    @Transactional
    public void updateNonExistingTicketSystemInstance() throws Exception {
        int databaseSizeBeforeUpdate = ticketSystemInstanceRepository.findAll().size();

        // Create the TicketSystemInstance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketSystemInstanceMockMvc.perform(put("/api/ticket-system-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance)))
            .andExpect(status().isBadRequest());

        // Validate the TicketSystemInstance in the database
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTicketSystemInstance() throws Exception {
        // Initialize the database
        ticketSystemInstanceRepository.saveAndFlush(ticketSystemInstance);

        int databaseSizeBeforeDelete = ticketSystemInstanceRepository.findAll().size();

        // Delete the ticketSystemInstance
        restTicketSystemInstanceMockMvc.perform(delete("/api/ticket-system-instances/{id}", ticketSystemInstance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
