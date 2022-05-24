package org.securityrat.casemanagement.service;


import lombok.extern.slf4j.Slf4j;
import org.securityrat.casemanagement.config.ApplicationProperties;
import org.securityrat.casemanagement.domain.AccessToken;
import org.securityrat.casemanagement.domain.TicketSystemInstance;
import org.securityrat.casemanagement.domain.User;
import org.securityrat.casemanagement.domain.enumeration.TicketSystem;
import org.securityrat.casemanagement.repository.AccessTokenRepository;
import org.securityrat.casemanagement.security.SecurityUtils;
import org.securityrat.casemanagement.service.interfaces.OAuthClient;
import org.securityrat.casemanagement.service.ticketsystems.jiraserver.JiraOAuthClient;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class AccessTokenService {

    private final ApplicationProperties applicationProperties;

    private final AccessTokenRepository accessTokenRepository;

    private final TokenEncryptorService tokenEncryptorService;

    private final UserService userService;

    public AccessTokenService(ApplicationProperties applicationProperties, AccessTokenRepository accessTokenRepository,
                              TokenEncryptorService tokenEncryptorService, UserService userService) {
        this.tokenEncryptorService = tokenEncryptorService;
        this.accessTokenRepository = accessTokenRepository;
        this.applicationProperties = applicationProperties;
        this.userService = userService;
    }

    public OAuthClient createOAuthClient(TicketSystemInstance ticketSystemInstance) {
        return getOauthClient(ticketSystemInstance.getType().toString(), ticketSystemInstance);
    }

    public Optional<TemporaryTokenProperties> createTempToken(OAuthClient oauthClient) {

        return Optional.of(oauthClient.getAndAuthorizeTemporaryToken());
    }

    @Transactional
    public boolean createAccessToken(OAuthClient oauthClient, TicketSystemInstance ticketSystemInstance, String tempToken, String authorizationCode) {
        String retrievedAccessToken = oauthClient.getAccessToken(tempToken, authorizationCode);
        if (retrievedAccessToken != null) {

            Optional<User> user = this.userService.getUserWithAuthorities();
            User currentUser = null;

            if (user.isEmpty()) {
                Optional<AbstractAuthenticationToken> authToken = SecurityUtils.getCurrentUserAuthToken();
                if (authToken.isEmpty()) {
                    log.warn("Invalid user {}", user);
                    return false;
                }
                currentUser = this.userService.getUserFromAuthdentication(authToken.get());
            } else {
                currentUser = user.get();
            }
            log.debug("Access token was created for user {} and for ticket system instance {}", currentUser.getId(), ticketSystemInstance.getId());
            Set<AccessToken> accessTokens = this.accessTokenRepository.findAccessTokenByTicketSystemInstanceAndUser(ticketSystemInstance, currentUser);
            for (AccessToken accessToken : accessTokens) {
                this.accessTokenRepository.delete(accessToken);
            }
            return this.storeAccessToken(oauthClient, currentUser, ticketSystemInstance, retrievedAccessToken);

        }
        log.warn("Access Token was not retrieved from ticket system");
        return false;
    }


    private boolean storeAccessToken(OAuthClient oauthClient, User user, TicketSystemInstance ticketSystemInstance, String createdAccessToken) {
        AccessToken accessToken = new AccessToken();
        byte[] decryptedToken = this.tokenEncryptorService.encrypt(createdAccessToken);
        accessToken.setToken(new String(decryptedToken));
        accessToken.setUser(user);
        accessToken.setTicketInstance(ticketSystemInstance);
        accessToken.setSalt(this.tokenEncryptorService.generateSalt());
        accessToken.setExpirationDate(oauthClient.getDefaultExpirationDate());
        this.accessTokenRepository.save(accessToken);
        log.info("Access token to ticket system with id {} and for user {} was created.", user.getId(), ticketSystemInstance.getId());
        return true;
    }

    /**
     * Returns the access token of a given the user for the ticket system if it exists and null otherwise
     *
     * @param ticketSystemInstance the ticket system instance
     * @return the access token or null if it doesn't exist
     */
    @Transactional(readOnly = true)
    public Optional<String> getExistingAccessToken(TicketSystemInstance ticketSystemInstance) {
        Optional<User> user = this.userService.getUserWithAuthorities();
        return user.flatMap(value -> this.accessTokenRepository.findAccessTokenByTicketSystemInstanceAndUser(ticketSystemInstance, value)
            .stream()
            .findFirst()
            .map(encryptedAccessToken -> {
                byte[] decryptedToken = this.tokenEncryptorService.decrypt(encryptedAccessToken.getToken(), encryptedAccessToken.getSalt());
                return new String(decryptedToken);
            }));
    }

    @Transactional
    public void deleteAccessTokenEntry(Long id) {
        this.accessTokenRepository.findById(id).ifPresent(this.accessTokenRepository::delete);
    }

    private OAuthClient getOauthClient(String ticketSystemType, TicketSystemInstance ticketSystemInstance) {

        TicketSystem defaultTicketSystemType = TicketSystem.valueOf(this.applicationProperties.getTicketSystem().getType());
        if (ticketSystemType != null) {
            // todo add IllegalArgumentException
            defaultTicketSystemType = TicketSystem.valueOf(ticketSystemType);
        }

        if (TicketSystem.JIRASERVER.equals(defaultTicketSystemType)) {
            log.info("OAuthClient created for ticket system instance {}", ticketSystemInstance.getId());
            return new JiraOAuthClient(ticketSystemInstance, this.applicationProperties);
        }

        return null;
    }
}
