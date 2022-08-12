package org.securityrat.casemanagement.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.securityrat.casemanagement.config.Constants;
import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.securityrat.casemanagement.repository.TicketSystemInstanceRepository;
import org.securityrat.casemanagement.service.AccessTokenService;
import org.securityrat.casemanagement.service.AbstractTemporaryTokenProperties;
import org.securityrat.casemanagement.service.Utils;
import org.securityrat.casemanagement.service.dto.TemporaryOAuthTokenDTO;
import org.securityrat.casemanagement.service.interfaces.OAuthClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api/ticket-system-instance")
@Slf4j
public class TicketSystemAuthenticationResource {

    private final AccessTokenService accessTokenService;
    private final TicketSystemInstanceRepository ticketSystemInstanceRepository;

    public TicketSystemAuthenticationResource(AccessTokenService accessTokenService, TicketSystemInstanceRepository ticketSystemInstanceRepository) {
        this.accessTokenService = accessTokenService;
        this.ticketSystemInstanceRepository = ticketSystemInstanceRepository;
    }

    @GetMapping("/auth/{id}/check-access-token")
    public ResponseEntity<String> checkAuthenticationStatus(@PathVariable Long id) {

        return ResponseEntity.ok("ok");
    }

    @GetMapping(value = "/oauth/{id}/request-token/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TemporaryOAuthTokenDTO> getAuthorizationUrl(@PathVariable Long id) {

        log.debug("REST request to create a request-token for the ticket system {}", id);
        Optional<TicketSystemInstance> ticketSystemInstance = this.ticketSystemInstanceRepository.findById(id);
        if (ticketSystemInstance.isPresent()) {
            OAuthClient oAuthClient = this.accessTokenService.createOAuthClient(ticketSystemInstance.get());
            Optional<AbstractTemporaryTokenProperties> requestToken = this.accessTokenService.createTempToken(oAuthClient);
            return requestToken.map(reqToken -> {
                TemporaryOAuthTokenDTO responseBody = new TemporaryOAuthTokenDTO(reqToken.getAuthorizationUrl());
                return ResponseEntity.ok().body(responseBody);
            }).orElseThrow(() -> {
                log.warn("Request token could not created for ticket system {}", id);
                return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            });
        }

        log.info("Ticket system instance not found");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/oauth/access-token")
    public ResponseEntity<String> createAccessTokenWithCallback(@RequestHeader("referer") String referrer, @RequestParam(name = "oauth_token") String requestToken, @RequestParam(name = "oauth_verifier") String verificationCode) {
        try {
            URI url = new URI(referrer);
            String ticketInstanceUrl = url.toString().split(Constants.JIRASERVERAUTHORIZEPATH)[0];
            ticketInstanceUrl = Utils.removeTrailingSlashInUrl(ticketInstanceUrl);
            Optional<TicketSystemInstance> ticketSystemInstance = this.ticketSystemInstanceRepository.findTicketSystemInstanceByUrl(ticketInstanceUrl);
            if (ticketSystemInstance.isPresent()) {
                OAuthClient oAuthClient = this.accessTokenService.createOAuthClient(ticketSystemInstance.get());
                boolean accessTokenCreated = this.accessTokenService.createAccessToken(oAuthClient, ticketSystemInstance.get(), requestToken, verificationCode);
                if (accessTokenCreated) {
                    return ResponseEntity.status(HttpStatus.CREATED).body("Access token created");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Access token was not created");
                }
            }
            return ResponseEntity.badRequest().body("Invalid ticket system URL provided in referer");


        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Invalid ticket system URL provided in referer");
        }

    }
}
