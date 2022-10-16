package org.securityrat.casemanagement.web.rest;

import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.securityrat.casemanagement.repository.TicketSystemInstanceRepository;
import org.securityrat.casemanagement.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.securityrat.casemanagement.domain.TicketSystemInstance}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TicketSystemInstanceResource {

    private final Logger log = LoggerFactory.getLogger(TicketSystemInstanceResource.class);

    private static final String ENTITY_NAME = "caseManagementTicketSystemInstance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketSystemInstanceRepository ticketSystemInstanceRepository;

    public TicketSystemInstanceResource(TicketSystemInstanceRepository ticketSystemInstanceRepository) {
        this.ticketSystemInstanceRepository = ticketSystemInstanceRepository;
    }

    /**
     * {@code POST  /ticket-system-instances} : Create a new ticketSystemInstance.
     *
     * @param ticketSystemInstance the ticketSystemInstance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketSystemInstance, or with status {@code 400 (Bad Request)} if the ticketSystemInstance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ticket-system-instances")
    public ResponseEntity<TicketSystemInstance> createTicketSystemInstance(@Valid @RequestBody TicketSystemInstance ticketSystemInstance) throws URISyntaxException {
        log.debug("REST request to save TicketSystemInstance : {}", ticketSystemInstance);
        if (ticketSystemInstance.getId() != null) {
            throw new BadRequestAlertException("A new ticketSystemInstance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TicketSystemInstance result = ticketSystemInstanceRepository.save(ticketSystemInstance);
        return ResponseEntity.created(new URI("/api/ticket-system-instances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ticket-system-instances} : Updates an existing ticketSystemInstance.
     *
     * @param ticketSystemInstance the ticketSystemInstance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketSystemInstance,
     * or with status {@code 400 (Bad Request)} if the ticketSystemInstance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketSystemInstance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ticket-system-instances")
    public ResponseEntity<TicketSystemInstance> updateTicketSystemInstance(@Valid @RequestBody TicketSystemInstance ticketSystemInstance) throws URISyntaxException {
        log.debug("REST request to update TicketSystemInstance : {}", ticketSystemInstance);
        if (ticketSystemInstance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TicketSystemInstance result = ticketSystemInstanceRepository.save(ticketSystemInstance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ticketSystemInstance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ticket-system-instances} : get all the ticketSystemInstances.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ticketSystemInstances in body.
     */
    @GetMapping("/ticket-system-instances")
    public ResponseEntity<List<TicketSystemInstance>> getAllTicketSystemInstances(Pageable pageable) {
        log.debug("REST request to get a page of TicketSystemInstances");
        Page<TicketSystemInstance> page = ticketSystemInstanceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ticket-system-instances/:id} : get the "id" ticketSystemInstance.
     *
     * @param id the id of the ticketSystemInstance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketSystemInstance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ticket-system-instances/{id}")
    public ResponseEntity<TicketSystemInstance> getTicketSystemInstance(@PathVariable Long id) {
        log.debug("REST request to get TicketSystemInstance : {}", id);
        Optional<TicketSystemInstance> ticketSystemInstance = ticketSystemInstanceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ticketSystemInstance);
    }

    /**
     * {@code DELETE  /ticket-system-instances/:id} : delete the "id" ticketSystemInstance.
     *
     * @param id the id of the ticketSystemInstance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ticket-system-instances/{id}")
    public ResponseEntity<Void> deleteTicketSystemInstance(@PathVariable Long id) {
        log.debug("REST request to delete TicketSystemInstance : {}", id);
        ticketSystemInstanceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
