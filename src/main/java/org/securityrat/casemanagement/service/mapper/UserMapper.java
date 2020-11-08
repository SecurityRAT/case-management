package org.securityrat.casemanagement.service.mapper;

import org.securityrat.casemanagement.domain.Authority;
import org.securityrat.casemanagement.domain.User;
import org.securityrat.casemanagement.service.dto.UserDTO;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link User} and its DTO called {@link UserDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UserMapper {

    public List<UserDTO> usersToUserDTOs(List<User> users) {
        return users.stream()
            .filter(Objects::nonNull)
            .map(this::userToUserDTO)
            .collect(Collectors.toList());
    }

    public UserDTO userToUserDTO(User appUser) {
        return new UserDTO(appUser);
    }

    public List<User> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream()
            .filter(Objects::nonNull)
            .map(this::userDTOToUser)
            .collect(Collectors.toList());
    }

    public User userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            User appUser = new User();
            appUser.setId(userDTO.getId());
            appUser.setLogin(userDTO.getLogin());
            appUser.setFirstName(userDTO.getFirstName());
            appUser.setLastName(userDTO.getLastName());
            appUser.setEmail(userDTO.getEmail());
            appUser.setImageUrl(userDTO.getImageUrl());
            appUser.setActivated(userDTO.isActivated());
            appUser.setLangKey(userDTO.getLangKey());
            Set<Authority> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            appUser.setAuthorities(authorities);
            return appUser;
        }
    }


    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if(authoritiesAsString != null){
            authorities = authoritiesAsString.stream().map(string -> {
                Authority auth = new Authority();
                auth.setName(string);
                return auth;
            }).collect(Collectors.toSet());
        }

        return authorities;
    }

    public User userFromId(String id) {
        if (id == null) {
            return null;
        }
        User appUser = new User();
        appUser.setId(id);
        return appUser;
    }
}
