package com.ouitrips.app.mapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.mapper.security.SignInMapper;
import com.ouitrips.app.mapper.security.UserAccountMapper;
import com.ouitrips.app.utils.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

class UserAccountMapperTest {

    @Mock
    private DateUtil dateUtil;

    @Mock
    private User user;

    @InjectMocks
    private UserAccountMapper userAccountMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userAccountMapper = new UserAccountMapper(dateUtil);

    }

    @Test
    void testApply() {
        // Arrange
        User users = new User();
        users.setFirstName("John");
        users.setLastName("Doe");
        users.setUsername("johndoe");
        users.setBirthDate(LocalDate.parse("1990-01-01"));
        users.setBirthPlace("New York");
        users.setEmail("john.doe@example.com");
        users.setCity("New York");
        users.setPhone("123456789");
        users.setCountry("USA");
        users.setCivility("Mr.");
        users.setZipCode("10001");
        users.setAddress("123 Main St");
        users.setAdditionalAddress("Apt 4B");
        users.setPicture("profile.jpg");
        users.setEnabled(true);
        users.setPrefixe("Mr.");
        users.setGoogleId("google-id");
        users.setFacebookId("facebook-id");
        users.setGsm("987654321");
        users.setIso2("US");
        users.setMobilePhone("555-1234");
        users.setProfession("Software Developer");
        users.setReference("ref123");
        users.setRoles("[\"USER\", \"ADMIN\"]");

        // Mock the DateUtil behavior
        when(dateUtil.parseString(Instant.parse("2012-02-22T02:06:58.147Z"), "YYYY-MM-dd")).thenReturn("2012-02-22T02:06:58.147Z");

        // Act
        Map<String, Object> result = (Map<String, Object>) userAccountMapper.apply(users);

        // Assert
        assertEquals("John", result.get("first_name"));
        assertEquals("Doe", result.get("last_name"));
        assertEquals("johndoe", result.get("username"));
        assertEquals("New York", result.get("birth_place"));
        assertEquals("john.doe@example.com", result.get("email"));
        assertEquals("New York", result.get("city"));
        assertEquals("123456789", result.get("phone"));
        assertEquals("USA", result.get("country"));
        assertEquals("Mr.", result.get("civility"));
        assertEquals("10001", result.get("zip_code"));
        assertEquals("123 Main St", result.get("address"));
        assertEquals("Apt 4B", result.get("complement_address"));
        assertEquals("profile.jpg", result.get("picture"));
        assertTrue((Boolean) result.get("enabled"));
        assertEquals("Mr.", result.get("prefix"));
        assertEquals("google-id", result.get("google_id"));
        assertEquals("facebook-id", result.get("facebook_id"));
        assertEquals("987654321", result.get("gsm"));
        assertEquals("US", result.get("iso2"));
        assertEquals("555-1234", result.get("mobile_phone"));
        assertEquals("Software Developer", result.get("profession"));
        assertEquals("ref123", result.get("reference"));

        List<String> roles = (List<String>) result.get("roles");
        assertNotNull(roles);
        assertTrue(roles.contains("USER"));
        assertTrue(roles.contains("ADMIN"));
    }


    @Test
    void testApplyy() {      SignInMapper signInMapper = new SignInMapper();

        // Arrange: Prepare the mock User object
        User usere = new User();
        usere.setReference("ref123");
        usere.setEmail("john.doe@example.com");
        usere.setPicture("profile.jpg");
        usere.setUsername("johndoe");
        usere.setFirstName("John");
        usere.setLastName("Doe");
        usere.setCivility("Mr.");
        usere.setRoles("[\"USER\", \"ADMIN\"]");

        // Act: Call the apply() method to map the user to the response
        Map<String, Object> result = signInMapper.apply(usere);

        // Assert: Verify the structure and content of the response map

        // Check the top-level "status"
        assertEquals("OK", result.get("status"));

        // Check "data" and its nested keys
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        assertNotNull(data);

        // Check the "data_user" map inside "data"
        Map<String, Object> dataUser = (Map<String, Object>) data.get("data_user");
        assertNotNull(dataUser);
        assertEquals("ref123", dataUser.get("reference"));
        assertEquals("john.doe@example.com", dataUser.get("email"));
        assertEquals("profile.jpg", dataUser.get("avatar"));
        assertEquals("johndoe", dataUser.get("username"));
        assertEquals("John", dataUser.get("first_name"));
        assertEquals("Doe", dataUser.get("last_name"));
        assertEquals("Mr.", dataUser.get("civility"));
        assertEquals("", dataUser.get("_token_"));  // Empty token value
        List<String> roles = (List<String>) dataUser.get("roles");
        assertNotNull(roles);
        assertTrue(roles.contains("USER"));
        assertTrue(roles.contains("ADMIN"));

        // Check the "info" map inside "data"
        Map<String, Object> info = (Map<String, Object>) data.get("info");
        assertNotNull(info);
        assertTrue((Boolean) info.get("active"));
        assertTrue((Boolean) info.get("connected"));
        assertEquals(50000, info.get("lifetime"));
        assertTrue((Boolean) info.get("exist"));
        assertFalse((Boolean) info.get("error_password"));
        assertFalse((Boolean) info.get("locked"));
    }

    @Test
    void testApplyWithNullValues() {SignInMapper signInMapper = new SignInMapper();
        // Arrange: Prepare the mock User object with null values
        User userr = new User();
        userr.setReference(null);
        userr.setEmail(null);
        userr.setPicture(null);
        userr.setUsername(null);
        userr.setFirstName(null);
        userr.setLastName(null);
        userr.setCivility(null);
        userr.setRoles("[]");

        // Act: Call the apply() method
        Map<String, Object> result = signInMapper.apply(userr);

        // Assert: Verify that the fields are properly handled even when null
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        Map<String, Object> dataUser = (Map<String, Object>) data.get("data_user");

        assertEquals(null, dataUser.get("reference"));
        assertEquals(null, dataUser.get("email"));
        assertEquals("Default.png", dataUser.get("avatar"));  // Default value for avatar
        assertEquals(null, dataUser.get("username"));
        assertEquals("", dataUser.get("first_name"));
        assertEquals("", dataUser.get("last_name"));
        assertEquals("", dataUser.get("civility"));
        assertEquals("", dataUser.get("_token_"));  // Empty token value
        List<String> roles = (List<String>) dataUser.get("roles");
        assertTrue(roles.isEmpty());  // Empty roles list
    }




}
