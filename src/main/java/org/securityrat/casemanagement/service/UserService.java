package org.securityrat.casemanagement.service;

import org.securityrat.casemanagement.config.Constants;
import org.securityrat.casemanagement.domain.Authority;
import org.securityrat.casemanagement.domain.User;
import org.securityrat.casemanagement.repository.AuthorityRepository;
import org.securityrat.casemanagement.repository.UserRepository;
import org.securityrat.casemanagement.security.SecurityUtils;
import org.securityrat.casemanagement.service.dto.UserDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository appUserRepository;

    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository appUserRepository, AuthorityRepository authorityRepository) {
        this.appUserRepository = appUserRepository;
        this.authorityRepository = authorityRepository;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current appUser.
     *
     * @param firstName first name of appUser.
     * @param lastName  last name of appUser.
     * @param email     email id of appUser.
     * @param langKey   language key.
     * @param imageUrl  image URL of appUser.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(appUserRepository::findOneByLogin)
            .ifPresent(appUser -> {
                appUser.setFirstName(firstName);
                appUser.setLastName(lastName);
                if (email != null) {
	                appUser.setEmail(email.toLowerCase());
                }
                appUser.setLangKey(langKey);
                appUser.setImageUrl(imageUrl);
                log.debug("Changed Information for User: {}", appUser);
            });
    }

    /**
     * Update all information for a specific appUser, and return the modified appUser.
     *
     * @param userDTO appUser to update.
     * @return updated appUser.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(appUserRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(appUser -> {
                appUser.setLogin(userDTO.getLogin().toLowerCase());
                appUser.setFirstName(userDTO.getFirstName());
                appUser.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    appUser.setEmail(userDTO.getEmail().toLowerCase());
                }
                appUser.setImageUrl(userDTO.getImageUrl());
                appUser.setActivated(userDTO.isActivated());
                appUser.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = appUser.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                log.debug("Changed Information for User: {}", appUser);
                return appUser;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        appUserRepository.findOneByLogin(login).ifPresent(appUser -> {
            appUserRepository.delete(appUser);
            log.debug("Deleted User: {}", appUser);
        });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return appUserRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return appUserRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return appUserRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(appUserRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private User syncUserWithIdP(Map<String, Object> details, User appUser) {
        // save authorities in to sync appUser roles/groups between IdP and JHipster's local database
        Collection<String> dbAuthorities = getAuthorities();
        Collection<String> userAuthorities =
            appUser.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
        for (String authority : userAuthorities) {
            if (!dbAuthorities.contains(authority)) {
                log.debug("Saving authority '{}' in local database", authority);
                Authority authorityToSave = new Authority();
                authorityToSave.setName(authority);
                authorityRepository.save(authorityToSave);
            }
        }
        // save account in to sync users between IdP and JHipster's local database
        Optional<User> existingUser = appUserRepository.findOneByLogin(appUser.getLogin());
        if (existingUser.isPresent()) {
            // if IdP sends last updated information, use it to determine if an update should happen
            if (details.get("updated_at") != null) {
                Instant dbModifiedDate = existingUser.get().getLastModifiedDate();
                Instant idpModifiedDate = new Date(Long.valueOf((Integer) details.get("updated_at"))).toInstant();
                if (idpModifiedDate.isAfter(dbModifiedDate)) {
                    log.debug("Updating appUser '{}' in local database", appUser.getLogin());
                    updateUser(appUser.getFirstName(), appUser.getLastName(), appUser.getEmail(),
                        appUser.getLangKey(), appUser.getImageUrl());
                }
                // no last updated info, blindly update
            } else {
                log.debug("Updating appUser '{}' in local database", appUser.getLogin());
                updateUser(appUser.getFirstName(), appUser.getLastName(), appUser.getEmail(),
                    appUser.getLangKey(), appUser.getImageUrl());
            }
        } else {
            log.debug("Saving appUser '{}' in local database", appUser.getLogin());
            appUserRepository.save(appUser);
        }
        return appUser;
    }

    /**
     * Returns the appUser from an OAuth 2.0 login or resource server with JWT.
     * Synchronizes the appUser in the local repository.
     *
     * @param authToken the authentication token.
     * @return the appUser from the authentication.
     */
    public UserDTO getUserFromAuthentication(AbstractAuthenticationToken authToken) {
        Map<String, Object> attributes;
        if (authToken instanceof OAuth2AuthenticationToken) {
            attributes = ((OAuth2AuthenticationToken) authToken).getPrincipal().getAttributes();
        } else if (authToken instanceof JwtAuthenticationToken) {
            attributes = ((JwtAuthenticationToken) authToken).getTokenAttributes();
        } else {
            throw new IllegalArgumentException("AuthenticationToken is not OAuth2 or JWT!");
        }
        User appUser = getUser(attributes);
        appUser.setAuthorities(authToken.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .map(authority -> {
                Authority auth = new Authority();
                auth.setName(authority);
                return auth;
            })
            .collect(Collectors.toSet()));
        return new UserDTO(syncUserWithIdP(attributes, appUser));
    }

    private static User getUser(Map<String, Object> details) {
        User appUser = new User();
        // handle resource server JWT, where sub claim is email and uid is ID
        if (details.get("uid") != null) {
            appUser.setId((String) details.get("uid"));
            appUser.setLogin((String) details.get("sub"));
        } else {
            appUser.setId((String) details.get("sub"));
        }
        if (details.get("preferred_username") != null) {
            appUser.setLogin(((String) details.get("preferred_username")).toLowerCase());
        } else if (appUser.getLogin() == null) {
            appUser.setLogin(appUser.getId());
        }
        if (details.get("given_name") != null) {
            appUser.setFirstName((String) details.get("given_name"));
        }
        if (details.get("family_name") != null) {
            appUser.setLastName((String) details.get("family_name"));
        }
        if (details.get("email_verified") != null) {
            appUser.setActivated((Boolean) details.get("email_verified"));
        }
        if (details.get("email") != null) {
            appUser.setEmail(((String) details.get("email")).toLowerCase());
        } else {
            appUser.setEmail((String) details.get("sub"));
        }
        if (details.get("langKey") != null) {
            appUser.setLangKey((String) details.get("langKey"));
        } else if (details.get("locale") != null) {
            // trim off country code if it exists
            String locale = (String) details.get("locale");
            if (locale.contains("_")) {
                locale = locale.substring(0, locale.indexOf("_"));
            } else if (locale.contains("-")) {
                locale = locale.substring(0, locale.indexOf("-"));
            }
            appUser.setLangKey(locale.toLowerCase());
        } else {
            // set langKey to default if not specified by IdP
            appUser.setLangKey(Constants.DEFAULT_LANGUAGE);
        }
        if (details.get("picture") != null) {
            appUser.setImageUrl((String) details.get("picture"));
        }
        appUser.setActivated(true);
        return appUser;
    }
}
