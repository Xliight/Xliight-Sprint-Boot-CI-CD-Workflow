package com.ouitrips.app.services.security.impl;

import com.ouitrips.app.config.JwtService;
import com.ouitrips.app.config.services.UserDetailsServiceImpl;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.mapper.security.SignInMapper;
import com.ouitrips.app.repositories.security.UserRepository;
import com.ouitrips.app.services.messagingsystemservice.email.EmailService;
import com.ouitrips.app.services.messagingsystemservice.sms.SmsService;
import com.ouitrips.app.utils.DateUtil;
import com.ouitrips.app.utils.UserUtils;
import com.ouitrips.app.utils.VariableProperty;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private EmailService emailService;
    @Mock
    private SmsService smsService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private UserUtils userUtils;
    @Mock
    private DateUtil dateUtil;
    @Mock
    private VariableProperty variableProperty;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }
    @Mock
    private HttpServletRequest request;


    @Test
    void testSignUp_withEmailVerification() {
        // Set up the mocked request and session
        request = Mockito.mock(HttpServletRequest.class);
        HttpSession sessionn = Mockito.mock(HttpSession.class);
        when(request.getSession()).thenReturn(sessionn);

        // Set the request attributes in the RequestContextHolder
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // Mock the variableProperty to simulate the email sending behavior
        when(variableProperty.getSendEmailLive()).thenReturn("false");
        // Arrange

        String firstName = "John";
        String lastName = "Doe";
        String password = "password123";
        String email = "john.doe@example.com";
        String phone = "1234567890";
        String type = "email";
        User user = new User();
        user.setReference("user123");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(variableProperty.getSendEmailLive()).thenReturn("false");

        // Act
        Map<String, Object> response = authService.signUp(firstName, lastName, password, email, phone, type);

        // Assert
        assertTrue(response.containsKey("security_token"));
        assertEquals("The code has been sent", response.get("message"));
        assertTrue(response.containsKey("verification_code"));

    }
    @Test
    void testSignUp_withSmsVerification() {
        request = Mockito.mock(HttpServletRequest.class);
        HttpSession sessiovn = Mockito.mock(HttpSession.class);
        when(request.getSession()).thenReturn(sessiovn);

        // Set the request attributes in the RequestContextHolder
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        // Arrange
        String firstName = "Jane";
        String lastName = "Smith";
        String password = "password456";
        String email = "jane.smith@example.com";
        String phone = "0987654321";
        String type = "gsm";
        User user = new User();
        user.setReference("user456");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(variableProperty.getSendEmailLive()).thenReturn("false"); // Add this mock to avoid NullPointerException
        when(variableProperty.getSendSmsLive()).thenReturn("true"); // Mock getSendSmsLive to avoid NullPointerException

        // Act
        Map<String, Object> response = authService.signUp(firstName, lastName, password, email, phone, type);
        // Assert
        assertTrue(response.containsKey("security_token"));
    }
    @Mock
    private SignInMapper signInMapper;

    @Test
    void testGetTokenFromDataResponse_withValidResponse() {
        Map<String, Object> dataUser = new HashMap<>();
        dataUser.put("_token_", "sampleToken123");

        Map<String, Object> data = new HashMap<>();
        data.put("data_user", dataUser);

        Map<String, Object> signInResponse = new HashMap<>();
        signInResponse.put("data", data);

        // Act: Call the method
        String token = authService.getTokenFromDataResponse(signInResponse);

        // Assert: Verify that the returned token matches the expected value
        Assertions.assertEquals("sampleToken123", token);
    }
    @Test
    void testGetTokenFromDataResponse_WithoutToken() {
        // Arrange: Prepare a map where "data_user" doesn't contain "_token_"
        Map<String, Object> dataUser = new HashMap<>();

        Map<String, Object> data = new HashMap<>();
        data.put("data_user", dataUser);

        Map<String, Object> signInResponse = new HashMap<>();
        signInResponse.put("data", data);

        // Act: Call the method
        String token = authService.getTokenFromDataResponse(signInResponse);

        // Assert: Verify that the method returns an empty string
        Assertions.assertEquals(null, token);
    }
    @Test
    void testGetTokenFromDataResponse_WithNullDataUser() {
        // Arrange: Prepare a map with "data_user" set to null
        Map<String, Object> data = new HashMap<>();
        data.put("data_user", null);

        Map<String, Object> signInResponse = new HashMap<>();
        signInResponse.put("data", data);

        // Act: Call the method
        String token = authService.getTokenFromDataResponse(signInResponse);

        // Assert: Verify that the method returns an empty string
        Assertions.assertEquals("", token);
    }
    @Test
    void testGetTokenFromDataResponse_WithNullData() {
        // Arrange: Prepare a map with "data" set to null
        Map<String, Object> signInResponse = new HashMap<>();
        signInResponse.put("data", null);

        // Act: Call the method
        String token = authService.getTokenFromDataResponse(signInResponse);

        // Assert: Verify that the method returns an empty string
        Assertions.assertEquals("", token);
    }
    @Test
    void testGetTokenFromDataResponse_WithEmptySignInResponse() {
        // Arrange: Prepare an empty map
        Map<String, Object> signInResponse = new HashMap<>();

        // Act: Call the method
        String token = authService.getTokenFromDataResponse(signInResponse);

        // Assert: Verify that the method returns an empty string
        Assertions.assertEquals("", token);
    }
    @Mock
    private HttpSession session;
    @Mock
    private Map<String, Map<String, String>> dataStore= new ConcurrentHashMap<>();



}