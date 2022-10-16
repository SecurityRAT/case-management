package org.securityrat.casemanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.securityrat.casemanagement.IntegrationTest;
import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.securityrat.casemanagement.domain.enumeration.TicketSystem;
import org.securityrat.casemanagement.repository.TicketSystemInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TicketSystemInstanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TicketSystemInstanceResourceIT {

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

    private static final String ENTITY_API_URL = "/api/ticket-system-instances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TicketSystemInstanceRepository ticketSystemInstanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketSystemInstanceMockMvc;

    private TicketSystemInstance ticketSystemInstance;

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
    void createTicketSystemInstance() throws Exception {
        int databaseSizeBeforeCreate = ticketSystemInstanceRepository.findAll().size();
        // Create the TicketSystemInstance
        restTicketSystemInstanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance))
            )
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
    void createTicketSystemInstanceWithExistingId() throws Exception {
        // Create the TicketSystemInstance with an existing ID
        ticketSystemInstance.setId(1L);

        int databaseSizeBeforeCreate = ticketSystemInstanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketSystemInstanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketSystemInstance in the database
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketSystemInstanceRepository.findAll().size();
        // set the field null
        ticketSystemInstance.setType(null);

        // Create the TicketSystemInstance, which fails.

        restTicketSystemInstanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance))
            )
            .andExpect(status().isBadRequest());

        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketSystemInstanceRepository.findAll().size();
        // set the field null
        ticketSystemInstance.setUrl(null);

        // Create the TicketSystemInstance, which fails.

        restTicketSystemInstanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance))
            )
            .andExpect(status().isBadRequest());

        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTicketSystemInstances() throws Exception {
        // Initialize the database
        ticketSystemInstanceRepository.saveAndFlush(ticketSystemInstance);

        // Get all the ticketSystemInstanceList
        restTicketSystemInstanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
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
    void getTicketSystemInstance() throws Exception {
        // Initialize the database
        ticketSystemInstanceRepository.saveAndFlush(ticketSystemInstance);

        // Get the ticketSystemInstance
        restTicketSystemInstanceMockMvc
            .perform(get(ENTITY_API_URL_ID, ticketSystemInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
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
    void getNonExistingTicketSystemInstance() throws Exception {
        // Get the ticketSystemInstance
        restTicketSystemInstanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTicketSystemInstance() throws Exception {
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

        restTicketSystemInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTicketSystemInstance.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTicketSystemInstance))
            )
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
    void putNonExistingTicketSystemInstance() throws Exception {
        int databaseSizeBeforeUpdate = ticketSystemInstanceRepository.findAll().size();
        ticketSystemInstance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketSystemInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticketSystemInstance.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketSystemInstance in the database
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTicketSystemInstance() throws Exception {
        int databaseSizeBeforeUpdate = ticketSystemInstanceRepository.findAll().size();
        ticketSystemInstance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketSystemInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketSystemInstance in the database
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTicketSystemInstance() throws Exception {
        int databaseSizeBeforeUpdate = ticketSystemInstanceRepository.findAll().size();
        ticketSystemInstance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketSystemInstanceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TicketSystemInstance in the database
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTicketSystemInstanceWithPatch() throws Exception {
        // Initialize the database
        ticketSystemInstanceRepository.saveAndFlush(ticketSystemInstance);

        int databaseSizeBeforeUpdate = ticketSystemInstanceRepository.findAll().size();

        // Update the ticketSystemInstance using partial update
        TicketSystemInstance partialUpdatedTicketSystemInstance = new TicketSystemInstance();
        partialUpdatedTicketSystemInstance.setId(ticketSystemInstance.getId());

        partialUpdatedTicketSystemInstance.type(UPDATED_TYPE).clientSecret(UPDATED_CLIENT_SECRET);

        restTicketSystemInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicketSystemInstance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketSystemInstance))
            )
            .andExpect(status().isOk());

        // Validate the TicketSystemInstance in the database
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeUpdate);
        TicketSystemInstance testTicketSystemInstance = ticketSystemInstanceList.get(ticketSystemInstanceList.size() - 1);
        assertThat(testTicketSystemInstance.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTicketSystemInstance.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTicketSystemInstance.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testTicketSystemInstance.getConsumerKey()).isEqualTo(DEFAULT_CONSUMER_KEY);
        assertThat(testTicketSystemInstance.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testTicketSystemInstance.getClientSecret()).isEqualTo(UPDATED_CLIENT_SECRET);
    }

    @Test
    @Transactional
    void fullUpdateTicketSystemInstanceWithPatch() throws Exception {
        // Initialize the database
        ticketSystemInstanceRepository.saveAndFlush(ticketSystemInstance);

        int databaseSizeBeforeUpdate = ticketSystemInstanceRepository.findAll().size();

        // Update the ticketSystemInstance using partial update
        TicketSystemInstance partialUpdatedTicketSystemInstance = new TicketSystemInstance();
        partialUpdatedTicketSystemInstance.setId(ticketSystemInstance.getId());

        partialUpdatedTicketSystemInstance
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .url(UPDATED_URL)
            .consumerKey(UPDATED_CONSUMER_KEY)
            .clientId(UPDATED_CLIENT_ID)
            .clientSecret(UPDATED_CLIENT_SECRET);

        restTicketSystemInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicketSystemInstance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketSystemInstance))
            )
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
    void patchNonExistingTicketSystemInstance() throws Exception {
        int databaseSizeBeforeUpdate = ticketSystemInstanceRepository.findAll().size();
        ticketSystemInstance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketSystemInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ticketSystemInstance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketSystemInstance in the database
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTicketSystemInstance() throws Exception {
        int databaseSizeBeforeUpdate = ticketSystemInstanceRepository.findAll().size();
        ticketSystemInstance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketSystemInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketSystemInstance in the database
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTicketSystemInstance() throws Exception {
        int databaseSizeBeforeUpdate = ticketSystemInstanceRepository.findAll().size();
        ticketSystemInstance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketSystemInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketSystemInstance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TicketSystemInstance in the database
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTicketSystemInstance() throws Exception {
        // Initialize the database
        ticketSystemInstanceRepository.saveAndFlush(ticketSystemInstance);

        int databaseSizeBeforeDelete = ticketSystemInstanceRepository.findAll().size();

        // Delete the ticketSystemInstance
        restTicketSystemInstanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, ticketSystemInstance.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TicketSystemInstance> ticketSystemInstanceList = ticketSystemInstanceRepository.findAll();
        assertThat(ticketSystemInstanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
