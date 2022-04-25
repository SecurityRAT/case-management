package org.securityrat.casemanagement.web.rest;

import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.securityrat.casemanagement.repository.TicketSystemInstanceRepository;
import org.securityrat.casemanagement.service.AccessTokenService;
import org.securityrat.casemanagement.service.TemporaryTokenProperties;
import org.securityrat.casemanagement.service.dto.TemporaryOAuthTokenDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Slf4j
public class TicketSystemAuthenticationResource {

    private AccessTokenService accessTokenService;
    private TicketSystemInstanceRepository ticketSystemInstanceRepository;

    public TicketSystemAuthenticationResource(AccessTokenService accessTokenService, TicketSystemInstanceRepository ticketSystemInstanceRepository) {
        this.accessTokenService = accessTokenService;
        this.ticketSystemInstanceRepository = ticketSystemInstanceRepository;
    }

    @GetMapping("/ticket-system-instance/{id}/request-token/")
    public ResponseEntity<TemporaryOAuthTokenDTO> getAuthorizationUrl(@PathVariable Long id) {

        log.debug("REST request to create a request-token for the ticket system {}", id);
        Optional<TicketSystemInstance> ticketSystemInstance = this.ticketSystemInstanceRepository.findById(id);
        if (ticketSystemInstance.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        this.accessTokenService.setOAuthClient(ticketSystemInstance.get());
        Optional<TemporaryTokenProperties> requestToken = this.accessTokenService.createTempToken();
        return requestToken.map(reqToken -> {
            TemporaryOAuthTokenDTO response = new TemporaryOAuthTokenDTO(reqToken.getAuthorizationUrl());
            try {
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, createRequestTokenCookie(reqToken.toString()).toString()).body(response);
            } catch (URISyntaxException e) {
                log.warn("callback URL not parseable");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).orElseThrow(() -> {
            log.warn("Request token could not created for ticket system {}", id);
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });

    }

    private ResponseCookie createRequestTokenCookie(String cookieValue) throws URISyntaxException {
        URI url = new URI(this.accessTokenService.getCallbackUrl());
        return ResponseCookie.from("secrat-ticketsystem", Base64Utils.encodeToString(cookieValue.getBytes(StandardCharsets.UTF_8)))
            .httpOnly(true)
            .path(url.getPath())
            .maxAge(600)
            .sameSite("Strict")
            .build();
    }
}
