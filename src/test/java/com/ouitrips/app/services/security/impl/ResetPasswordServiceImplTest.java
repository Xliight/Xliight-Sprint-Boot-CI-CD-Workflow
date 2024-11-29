package com.ouitrips.app.services.security.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.repositories.security.UserRepository;
import com.ouitrips.app.services.messagingsystemservice.email.EmailService;
import com.ouitrips.app.services.messagingsystemservice.sms.SmsService;
import com.ouitrips.app.utils.DateUtil;
import com.ouitrips.app.utils.VariableProperty;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@ExtendWith(MockitoExtension.class)
 class ResetPasswordServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private SmsService smsService;
    @Mock
    private DateUtil dateUtil;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private VariableProperty variableProperty;

    @InjectMocks
    private ResetPasswordServiceImpl resetPasswordService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setReference("user-ref-123"); // Ensure reference is set
        user.setFirstName("John");
        user.setMobilePhone("1234567890");
        user.setLastName("Doe");

        // Set up mock HTTP request context

    }

    @Test
    void testForgotPassword_ValidEmail() {
        MockHttpServletRequest requestt = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(requestt));
        Map<String, Object> response = new HashMap<>();
        response.put("message", "The code has been sent");

        // Mock variableProperty to ensure the email sending path
        when(variableProperty.getSendEmailLive()).thenReturn("true");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        // Mock other necessary method calls
        when(dateUtil.getYear(any(Date.class))).thenReturn(2024);

        // Run forgotPassword method
        Object result = resetPasswordService.forgotPassword("test@example.com", "email");


        assertNotNull(result);
        assertTrue(result instanceof Map);
        assertEquals("The code has been sent", ((Map) result).get("message"));
    }
    @Test
    void testForgotPassword_EmailNotFound() {
        MockHttpServletRequest requestt = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(requestt));
        when(userRepository.existsByEmail("notfound@example.com")).thenReturn(false);

        Exception exception = assertThrows(ExceptionControllerAdvice.UserNotFoundException.class,
                () -> resetPasswordService.forgotPassword("notfound@example.com", "email"));

        assertEquals("Email not found", exception.getMessage());
    }


    @Test
    void testForgotPassword_ValidGSM() {
        MockHttpServletRequest requestt = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(requestt));
        String codeVerification = "123456";
        when(variableProperty.getSendSmsLive()).thenReturn("true");

        Object response = resetPasswordService.sendResetPasswordGSM(user, codeVerification);

        // Verify the response contains expected values
        assertNotNull(response);
        assertTrue(((Map) response).containsKey("message"));
        assertEquals("The code has been sent", ((Map) response).get("message"));

        // Verify that sendSms was called with the expected parameters
        ArgumentCaptor<String> phoneCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Map<String, Object>> templateCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> templateNameCaptor = ArgumentCaptor.forClass(String.class);

        verify(smsService, times(1)).sendSms(
                eq("OuiTrips"),  // Expected SMS sender name
                phoneCaptor.capture(),  // Capture the mobile phone number argument
                isNull(),  // Null value for 3rd parameter
                templateCaptor.capture(),  // Capture the template model argument
                templateNameCaptor.capture(),  // Capture the template name argument
                isNull()  // Null value for last argument
        );

        // Verify that the phone number is correct
        assertEquals("1234567890", phoneCaptor.getValue());

        // Verify the template model contains expected values
        Map<String, Object> templateModel = templateCaptor.getValue();
        assertEquals("John Doe", templateModel.get("name"));
        assertEquals("123456", templateModel.get("code"));

        // Verify the template name is correct
    }

    @Test
    void testForgotPassword_GSMNotFound() {
        MockHttpServletRequest requestt = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(requestt));
        String gsm = "0000000000";

        when(userRepository.findUserByMobilePhone(gsm)).thenReturn(Optional.empty());
        when(userRepository.findUserByGsm(gsm)).thenReturn(Optional.empty());
        when(userRepository.findUserByGsmPrefixe(gsm)).thenReturn(Collections.emptyList());



        ExceptionControllerAdvice.UserNotFoundException thrown = assertThrows(
                ExceptionControllerAdvice.UserNotFoundException.class,
                () -> resetPasswordService.forgotPassword(gsm, "gsm")
        );

        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void testForgotPassword_InvalidContactType() {
        MockHttpServletRequest requestt = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(requestt));
        String invalidType = "invalidType";


        ExceptionControllerAdvice.GeneralException thrown = assertThrows(
                ExceptionControllerAdvice.GeneralException.class,
                () -> resetPasswordService.forgotPassword("test@example.com", invalidType)
        );

        assertEquals("Contact Type not correct, type should be : 'email' or 'gsm'", thrown.getContent());
    }

    @Mock
    private HttpServletRequest request;

    @Test()
     void testVerificationCode_withNullToken_shouldThrowException() throws ExceptionControllerAdvice.ObjectNotFoundException {

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getSession().getAttribute("user_reference")).thenReturn(null); // Simulating missing user reference
        when(request.getSession().getAttribute("action")).thenReturn("VERIFICATION_CODE"); // Action set, but no user reference

        // Assert that the exception is thrown
        try {
            resetPasswordService.verificationCode("12345", null);
            fail("Expected ObjectNotFoundException to be thrown");  // If no exception is thrown, fail the test
        } catch (ExceptionControllerAdvice.ObjectNotFoundException e) {
            // Assert the exception message if needed
            assertEquals("Invalid or expired token", e.getContent());
        }
    }


    @Test
     void testVerificationCode_withValidVerificationCode_shouldResetPassword() throws ExceptionControllerAdvice.ObjectNotFoundException {
        // Simulate the session behavior
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user_reference")).thenReturn("user123");
        when(session.getAttribute("action")).thenReturn("VERIFICATION_CODE");
        User userr =mock(User.class);
        userr.setEmail("test@example.com");
        userr.setReference("user-ref-123"); // Ensure reference is set
        userr.setFirstName("John");
        userr.setMobilePhone("1234567890");
        userr.setLastName("Doe");
        userr.setPassword("123456");
        userr.setTokenPassword("123456");

        // Simulate the user data and behavior for the valid case
        String verificationCode = "12345";
        String tokenPassword = "12345"; // This is the tokenPassword stored for the user

        // Mock the user repository to return a valid user with the correct tokenPassword
        when(userRepository.findByReference("user123")).thenReturn(Optional.of(userr));
        when(userr.getTokenPassword()).thenReturn(tokenPassword);

        // Mock the data store (in case the token is not null)
        Map<String, String> dataUser = new HashMap<>();
        dataUser.put("created_at", "2024-11-08");
        dataUser.put("reference_user", "user123");

       when(userRepository.save(userr)).thenReturn(userr);
        resetPasswordService.verificationCode(verificationCode, null);
        // Verify that the tokenPassword was cleared
        verify(userr).setTokenPassword(null);

        // Verify that the user was saved in the repository
        verify(userRepository).save(userr);
        // Verify that the action in the session was set to "RESET_PASSWORD"
        verify(session).setAttribute("action", "RESET_PASSWORD");

        // Verify that the data store was updated with the new data
        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("action", "RESET_PASSWORD");
        updatedData.put("created_at", dataUser.get("created_at"));
        updatedData.put("reference_user", dataUser.get("reference_user"));
    }



}


