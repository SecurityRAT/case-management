package org.securityrat.casemanagement.service.mapper;

import org.securityrat.casemanagement.domain.User;
import org.securityrat.casemanagement.service.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UserMapper}.
 */
public class UserMapperTest {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String DEFAULT_ID = "id1";

    private UserMapper userMapper;
    private User appUser;
    private UserDTO userDto;

    @BeforeEach
    public void init() {
        userMapper = new UserMapper();
        appUser = new User();
        appUser.setLogin(DEFAULT_LOGIN);
        appUser.setActivated(true);
        appUser.setEmail("johndoe@localhost");
        appUser.setFirstName("john");
        appUser.setLastName("doe");
        appUser.setImageUrl("image_url");
        appUser.setLangKey("en");

        userDto = new UserDTO(appUser);
    }

    @Test
    public void usersToUserDTOsShouldMapOnlyNonNullUsers() {
        List<User> users = new ArrayList<>();
        users.add(appUser);
        users.add(null);

        List<UserDTO> userDTOS = userMapper.usersToUserDTOs(users);

        assertThat(userDTOS).isNotEmpty();
        assertThat(userDTOS).size().isEqualTo(1);
    }

    @Test
    public void userDTOsToUsersShouldMapOnlyNonNullUsers() {
        List<UserDTO> usersDto = new ArrayList<>();
        usersDto.add(userDto);
        usersDto.add(null);

        List<User> users = userMapper.userDTOsToUsers(usersDto);

        assertThat(users).isNotEmpty();
        assertThat(users).size().isEqualTo(1);
    }

    @Test
    public void userDTOsToUsersWithAuthoritiesStringShouldMapToUsersWithAuthoritiesDomain() {
        Set<String> authoritiesAsString = new HashSet<>();
        authoritiesAsString.add("ADMIN");
        userDto.setAuthorities(authoritiesAsString);

        List<UserDTO> usersDto = new ArrayList<>();
        usersDto.add(userDto);

        List<User> users = userMapper.userDTOsToUsers(usersDto);

        assertThat(users).isNotEmpty();
        assertThat(users).size().isEqualTo(1);
        assertThat(users.get(0).getAuthorities()).isNotNull();
        assertThat(users.get(0).getAuthorities()).isNotEmpty();
        assertThat(users.get(0).getAuthorities().iterator().next().getName()).isEqualTo("ADMIN");
    }

    @Test
    public void userDTOsToUsersMapWithNullAuthoritiesStringShouldReturnUserWithEmptyAuthorities() {
        userDto.setAuthorities(null);

        List<UserDTO> usersDto = new ArrayList<>();
        usersDto.add(userDto);

        List<User> users = userMapper.userDTOsToUsers(usersDto);

        assertThat(users).isNotEmpty();
        assertThat(users).size().isEqualTo(1);
        assertThat(users.get(0).getAuthorities()).isNotNull();
        assertThat(users.get(0).getAuthorities()).isEmpty();
    }

    @Test
    public void userDTOToUserMapWithAuthoritiesStringShouldReturnUserWithAuthorities() {
        Set<String> authoritiesAsString = new HashSet<>();
        authoritiesAsString.add("ADMIN");
        userDto.setAuthorities(authoritiesAsString);

        User appUser = userMapper.userDTOToUser(userDto);

        assertThat(appUser).isNotNull();
        assertThat(appUser.getAuthorities()).isNotNull();
        assertThat(appUser.getAuthorities()).isNotEmpty();
        assertThat(appUser.getAuthorities().iterator().next().getName()).isEqualTo("ADMIN");
    }

    @Test
    public void userDTOToUserMapWithNullAuthoritiesStringShouldReturnUserWithEmptyAuthorities() {
        userDto.setAuthorities(null);

        User appUser = userMapper.userDTOToUser(userDto);

        assertThat(appUser).isNotNull();
        assertThat(appUser.getAuthorities()).isNotNull();
        assertThat(appUser.getAuthorities()).isEmpty();
    }

    @Test
    public void userDTOToUserMapWithNullUserShouldReturnNull() {
        assertThat(userMapper.userDTOToUser(null)).isNull();
    }

    @Test
    public void testUserFromId() {
        assertThat(userMapper.userFromId(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
        assertThat(userMapper.userFromId(null)).isNull();
    }
}
