package org.securityrat.casemanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.securityrat.casemanagement.domain.AccessToken;
import org.securityrat.casemanagement.repository.AccessTokenRepository;
import org.securityrat.casemanagement.repository.UserRepository;
import org.securityrat.casemanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.securityrat.casemanagement.domain.AccessToken}.
 */
@RestController
@RequestMapping("/api")
public class AccessTokenResource {

    private final Logger log = LoggerFactory.getLogger(AccessTokenResource.class);

    private static final String ENTITY_NAME = "caseManagementAccessToken";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccessTokenRepository accessTokenRepository;

    private final UserRepository userRepository;

    public AccessTokenResource(AccessTokenRepository accessTokenRepository, UserRepository userRepository) {
        this.accessTokenRepository = accessTokenRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /access-tokens} : Create a new accessToken.
     *
     * @param accessToken the accessToken to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accessToken, or with status {@code 400 (Bad Request)} if the accessToken has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/access-tokens")
    public ResponseEntity<AccessToken> createAccessToken(@Valid @RequestBody AccessToken accessToken) throws URISyntaxException {
        log.debug("REST request to save AccessToken : {}", accessToken);
        if (accessToken.getId() != null) {
            throw new BadRequestAlertException("A new accessToken cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (accessToken.getUser() != null) {
            // Save user in case it's new and only exists in gateway
            userRepository.save(accessToken.getUser());
        }
        AccessToken result = accessTokenRepository.save(accessToken);
        return ResponseEntity
            .created(new URI("/api/access-tokens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /access-tokens/:id} : Updates an existing accessToken.
     *
     * @param id the id of the accessToken to save.
     * @param accessToken the accessToken to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accessToken,
     * or with status {@code 400 (Bad Request)} if the accessToken is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accessToken couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/access-tokens/{id}")
    public ResponseEntity<AccessToken> updateAccessToken(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccessToken accessToken
    ) throws URISyntaxException {
        log.debug("REST request to update AccessToken : {}, {}", id, accessToken);
        if (accessToken.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accessToken.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accessTokenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (accessToken.getUser() != null) {
            // Save user in case it's new and only exists in gateway
            userRepository.save(accessToken.getUser());
        }
        AccessToken result = accessTokenRepository.save(accessToken);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accessToken.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /access-tokens/:id} : Partial updates given fields of an existing accessToken, field will ignore if it is null
     *
     * @param id the id of the accessToken to save.
     * @param accessToken the accessToken to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accessToken,
     * or with status {@code 400 (Bad Request)} if the accessToken is not valid,
     * or with status {@code 404 (Not Found)} if the accessToken is not found,
     * or with status {@code 500 (Internal Server Error)} if the accessToken couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/access-tokens/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AccessToken> partialUpdateAccessToken(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccessToken accessToken
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccessToken partially : {}, {}", id, accessToken);
        if (accessToken.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accessToken.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accessTokenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (accessToken.getUser() != null) {
            // Save user in case it's new and only exists in gateway
            userRepository.save(accessToken.getUser());
        }

        Optional<AccessToken> result = accessTokenRepository
            .findById(accessToken.getId())
            .map(
                existingAccessToken -> {
                    if (accessToken.getToken() != null) {
                        existingAccessToken.setToken(accessToken.getToken());
                    }
                    if (accessToken.getExpirationDate() != null) {
                        existingAccessToken.setExpirationDate(accessToken.getExpirationDate());
                    }
                    if (accessToken.getSalt() != null) {
                        existingAccessToken.setSalt(accessToken.getSalt());
                    }
                    if (accessToken.getRefreshToken() != null) {
                        existingAccessToken.setRefreshToken(accessToken.getRefreshToken());
                    }

                    return existingAccessToken;
                }
            )
            .map(accessTokenRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accessToken.getId().toString())
        );
    }

    /**
     * {@code GET  /access-tokens} : get all the accessTokens.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accessTokens in body.
     */
    @GetMapping("/access-tokens")
    public ResponseEntity<List<AccessToken>> getAllAccessTokens(Pageable pageable) {
        log.debug("REST request to get a page of AccessTokens");
        Page<AccessToken> page = accessTokenRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /access-tokens/:id} : get the "id" accessToken.
     *
     * @param id the id of the accessToken to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accessToken, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/access-tokens/{id}")
    public ResponseEntity<AccessToken> getAccessToken(@PathVariable Long id) {
        log.debug("REST request to get AccessToken : {}", id);
        Optional<AccessToken> accessToken = accessTokenRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(accessToken);
    }

    /**
     * {@code DELETE  /access-tokens/:id} : delete the "id" accessToken.
     *
     * @param id the id of the accessToken to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/access-tokens/{id}")
    public ResponseEntity<Void> deleteAccessToken(@PathVariable Long id) {
        log.debug("REST request to delete AccessToken : {}", id);
        accessTokenRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
