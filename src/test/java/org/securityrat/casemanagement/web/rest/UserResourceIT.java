package org.securityrat.casemanagement.web.rest;

import org.securityrat.casemanagement.CaseManagementApp;
import org.securityrat.casemanagement.config.TestSecurityConfiguration;
import org.securityrat.casemanagement.domain.Authority;
import org.securityrat.casemanagement.domain.User;
import org.securityrat.casemanagement.repository.UserRepository;
import org.securityrat.casemanagement.security.AuthoritiesConstants;

import org.securityrat.casemanagement.service.UserService;
import org.securityrat.casemanagement.service.dto.UserDTO;
import org.securityrat.casemanagement.service.mapper.UserMapper;
import org.securityrat.casemanagement.web.rest.errors.ExceptionTranslator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@SpringBootTest(classes = {CaseManagementApp.class, TestSecurityConfiguration.class})
public class UserResourceIT {

    private static final String DEFAULT_LOGIN = "johndoe";

    private static final String DEFAULT_ID = "id1";

    private static final String DEFAULT_PASSWORD = "passjohndoe";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";

    private static final String DEFAULT_LASTNAME = "doe";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";

    private static final String DEFAULT_LANGKEY = "en";

    @Autowired
    private UserRepository appUserRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserMockMvc;

    private User appUser;

    @BeforeEach
    public void setup() {
        UserResource appUserResource = new UserResource(userService);

        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(appUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter)
            .build();
    }

    /**
     * Create a User.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a required relationship to the User entity.
     */
    public static User createEntity(EntityManager em) {
        User appUser = new User();
        appUser.setId(UUID.randomUUID().toString());
        appUser.setLogin(DEFAULT_LOGIN + RandomStringUtils.randomAlphabetic(5));
        appUser.setActivated(true);
        appUser.setEmail(RandomStringUtils.randomAlphabetic(5) + DEFAULT_EMAIL);
        appUser.setFirstName(DEFAULT_FIRSTNAME);
        appUser.setLastName(DEFAULT_LASTNAME);
        appUser.setImageUrl(DEFAULT_IMAGEURL);
        appUser.setLangKey(DEFAULT_LANGKEY);
        return appUser;
    }

    @BeforeEach
    public void initTest() {
        appUser = createEntity(em);
        appUser.setLogin(DEFAULT_LOGIN);
        appUser.setEmail(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void getAllUsers() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the users
        restUserMockMvc.perform(get("/api/users?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGEURL)))
            .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANGKEY)));
    }

    @Test
    @Transactional
    public void getUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get the appUser
        restUserMockMvc.perform(get("/api/users/{login}", appUser.getLogin()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.login").value(appUser.getLogin()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGEURL))
            .andExpect(jsonPath("$.langKey").value(DEFAULT_LANGKEY));

    }

    @Test
    @Transactional
    public void getNonExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/unknown"))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testUserEquals() throws Exception {
        TestUtil.equalsVerifier(User.class);
        User appUser1 = new User();
        appUser1.setId("id1");
        User appUser2 = new User();
        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);
        appUser2.setId("id2");
        assertThat(appUser1).isNotEqualTo(appUser2);
        appUser1.setId(null);
        assertThat(appUser1).isNotEqualTo(appUser2);
    }

    @Test
    public void testUserDTOtoUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(DEFAULT_ID);
        userDTO.setLogin(DEFAULT_LOGIN);
        userDTO.setFirstName(DEFAULT_FIRSTNAME);
        userDTO.setLastName(DEFAULT_LASTNAME);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setActivated(true);
        userDTO.setImageUrl(DEFAULT_IMAGEURL);
        userDTO.setLangKey(DEFAULT_LANGKEY);
        userDTO.setCreatedBy(DEFAULT_LOGIN);
        userDTO.setLastModifiedBy(DEFAULT_LOGIN);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        User appUser = userMapper.userDTOToUser(userDTO);
        assertThat(appUser.getId()).isEqualTo(DEFAULT_ID);
        assertThat(appUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(appUser.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(appUser.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(appUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(appUser.getActivated()).isEqualTo(true);
        assertThat(appUser.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(appUser.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(appUser.getCreatedBy()).isNull();
        assertThat(appUser.getCreatedDate()).isNotNull();
        assertThat(appUser.getLastModifiedBy()).isNull();
        assertThat(appUser.getLastModifiedDate()).isNotNull();
        assertThat(appUser.getAuthorities()).extracting("name").containsExactly(AuthoritiesConstants.USER);
    }

    @Test
    public void testUserToUserDTO() {
        appUser.setId(DEFAULT_ID);
        appUser.setCreatedBy(DEFAULT_LOGIN);
        appUser.setCreatedDate(Instant.now());
        appUser.setLastModifiedBy(DEFAULT_LOGIN);
        appUser.setLastModifiedDate(Instant.now());
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authorities.add(authority);
        appUser.setAuthorities(authorities);

        UserDTO userDTO = userMapper.userToUserDTO(appUser);

        assertThat(userDTO.getId()).isEqualTo(DEFAULT_ID);
        assertThat(userDTO.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(userDTO.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(userDTO.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(userDTO.isActivated()).isEqualTo(true);
        assertThat(userDTO.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(userDTO.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(userDTO.getCreatedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getCreatedDate()).isEqualTo(appUser.getCreatedDate());
        assertThat(userDTO.getLastModifiedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getLastModifiedDate()).isEqualTo(appUser.getLastModifiedDate());
        assertThat(userDTO.getAuthorities()).containsExactly(AuthoritiesConstants.USER);
        assertThat(userDTO.toString()).isNotNull();
    }

    @Test
    public void testAuthorityEquals() {
        Authority authorityA = new Authority();
        assertThat(authorityA).isEqualTo(authorityA);
        assertThat(authorityA).isNotEqualTo(null);
        assertThat(authorityA).isNotEqualTo(new Object());
        assertThat(authorityA.hashCode()).isEqualTo(0);
        assertThat(authorityA.toString()).isNotNull();

        Authority authorityB = new Authority();
        assertThat(authorityA).isEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.ADMIN);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityA.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isEqualTo(authorityB);
        assertThat(authorityA.hashCode()).isEqualTo(authorityB.hashCode());
    }
}
