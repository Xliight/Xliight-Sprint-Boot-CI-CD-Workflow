package com.ouitrips.app.Exception;

import com.ouitrips.app.exceptions.ErrorResponse;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.NoHandlerFoundException;

import org. springframework. security. access.AccessDeniedException;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionControllerAdviceTest {

    private ExceptionControllerAdvice exceptionControllerAdvice;

    @BeforeEach
     void setUp() {
        exceptionControllerAdvice = new ExceptionControllerAdvice(null);  // Pass null or a mock dependency if needed
    }


    @Test
     void testHandleMissingServletRequestParameter() {
        // Simulate a MissingServletRequestParameterException
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("param1", "String");

        // Call the exception handler method
        ErrorResponse errorResponse = exceptionControllerAdvice.handleMissingServletRequestParameter(ex);

        // Assertions to check the response
        assertEquals("INVALID_REQUEST", errorResponse.getStatus());
        assertEquals("Required request parameter 'param1' is missing", errorResponse.getMessage());
    }

    @Test
     void testHandleUsernameNotFoundException() {
        // Simulate a UsernameNotFoundException
        UsernameNotFoundException ex = new UsernameNotFoundException("Invalid username");

        // Call the exception handler method
        ErrorResponse errorResponse = exceptionControllerAdvice.handleMissingServletRequestParameter(ex);

        // Assertions to check the response
        assertEquals("ERROR", errorResponse.getStatus());
        assertEquals("Username or password is incorrect", errorResponse.getMessage());
    }

    @Test
     void testHandleInvalidBearerTokenException() {
        // Simulate an InvalidBearerTokenException
        InvalidBearerTokenException ex = new InvalidBearerTokenException("Invalid token");

        // Call the exception handler method
        ErrorResponse errorResponse = exceptionControllerAdvice.handleInvalidBearerTokenException(ex);

        // Assertions to check the response
        assertEquals("REQUEST_DENIED", errorResponse.getStatus());
        assertEquals("The access token provided is expired, revoked, malformed, or invalid for other reason", errorResponse.getMessage());
    }

    @Test
     void testHandleSQLException() {
        // Simulate a SQLException
        SQLException ex = new SQLException("Database error");

        // Call the exception handler method
        ErrorResponse errorResponse = exceptionControllerAdvice.handleSQLExceptionException(ex);

        // Assertions to check the response
        assertEquals("ERROR_DATABASE", errorResponse.getStatus());
        assertEquals("ERROR DATABASE", errorResponse.getMessage());
    }

    @Test
     void testHandleAccessDeniedException() {
        // Simulate an AccessDeniedException
        AccessDeniedException ex = new AccessDeniedException("Access denied");

        // Call the exception handler method
        ErrorResponse errorResponse = exceptionControllerAdvice.handleAccessDeniedException(ex);

        // Assertions to check the response
        assertEquals("REQUEST_DENIED", errorResponse.getStatus());
        assertEquals("You don't have permission", errorResponse.getMessage());
    }

    @Test
     void testHandleAuthenticationException() {
        // Simulate an AuthenticationException (using BadCredentialsException as a subclass)
        AuthenticationException ex = new BadCredentialsException("Full authentication required");

        // Call the exception handler method
        ErrorResponse errorResponse = exceptionControllerAdvice.handleAuthenticationException(ex);

        // Assertions to check the response
        assertEquals("REQUEST_DENIED", errorResponse.getStatus());
        assertEquals("Full authentication is required to access this resource", errorResponse.getMessage());
    }

    @Test
     void testHandleNoHandlerFoundException() {
        // Simulate a NoHandlerFoundException
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/resource", null);

        // Call the exception handler method
        ErrorResponse errorResponse = exceptionControllerAdvice.handleNoHandlerFoundException(ex);

        // Assertions to check the response
        assertEquals("ERROR", errorResponse.getStatus());
        assertEquals("Resource not found", errorResponse.getMessage());
    }

    @Test
     void testHandleUserNotFoundException() {
        // Simulate a UserNotFoundException
        ExceptionControllerAdvice.UserNotFoundException ex = new ExceptionControllerAdvice.UserNotFoundException("User not found");

        // Call the exception handler method
        ResponseEntity<ErrorResponse> response = exceptionControllerAdvice.handleUserNotFoundException(ex);

        // Assertions to check the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User not found", response.getBody().getMessage());
    }

    @Test
     void testHandleSignInUsernameNotFoundException() {
        // Simulate a SignInUsernameNotFoundException
        ExceptionControllerAdvice.SignInUsernameNotFoundException ex = new ExceptionControllerAdvice.SignInUsernameNotFoundException();

        // Call the exception handler method
        ResponseEntity<?> response = exceptionControllerAdvice.handleUserNotFoundException(ex);

        // Assertions to check the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ERROR", ((Map) response.getBody()).get("status"));
        assertEquals(true, ((Map) response.getBody()).get("error_username"));
    }

    @Test
     void testHandleErrorPasswordException() {
        // Simulate a ErrorPasswordException
        ExceptionControllerAdvice.ErrorPasswordException ex = new ExceptionControllerAdvice.ErrorPasswordException();

        // Call the exception handler method
        ResponseEntity<?> response = exceptionControllerAdvice.handleErrorPasswordException(ex);

        // Assertions to check the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ERROR", ((Map) response.getBody()).get("status"));
        assertEquals(true, ((Map) response.getBody()).get("error_password"));
    }

    @Test
     void testHandleRegisterException() {
        // Simulate a RegisterException
        ExceptionControllerAdvice.RegisterException ex = new ExceptionControllerAdvice.RegisterException("Registration failed");

        // Call the exception handler method
        ResponseEntity<ErrorResponse> response = exceptionControllerAdvice.handleRegisterException(ex);

        // Assertions to check the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration failed", response.getBody().getMessage());
    }

    @Test
     void testHandleSendEmailException() {
        // Simulate a SendEmailException
        ExceptionControllerAdvice.SendEmailException ex = new ExceptionControllerAdvice.SendEmailException("Email sending failed");

        // Call the exception handler method
        ResponseEntity<ErrorResponse> response = exceptionControllerAdvice.handleSendEmailException(ex);

        // Assertions to check the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email sending failed", response.getBody().getMessage());
    }

    @Test
     void testHandleObjectNotFoundException() {
        // Simulate an ObjectNotFoundException
        ExceptionControllerAdvice.ObjectNotFoundException ex = new ExceptionControllerAdvice.ObjectNotFoundException("Object not found");

        // Call the exception handler method
        ResponseEntity<ErrorResponse> response = exceptionControllerAdvice.handleGeneralException(ex);

        // Assertions to check the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Object not found", response.getBody().getMessage());
    }

    @Test
     void testHandleGeneralException() {
        // Simulate a GeneralException
        ExceptionControllerAdvice.GeneralException ex = new ExceptionControllerAdvice.GeneralException("General error");

        // Call the exception handler method
        ResponseEntity<ErrorResponse> response = exceptionControllerAdvice.handleGeneralException(ex);

        // Assertions to check the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("General error", response.getBody().getMessage());
    }

    @Test
     void testHandleSendSmsException() {
        // Simulate a SendSmsException
        ExceptionControllerAdvice.SendSmsException ex = new ExceptionControllerAdvice.SendSmsException("SMS sending failed");

        // Call the exception handler method
        ResponseEntity<ErrorResponse> response = exceptionControllerAdvice.handleSendSmsException(ex);

        // Assertions to check the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SMS sending failed", response.getBody().getMessage());
    }

    @Test
     void testHandleNotAuthenticatedException() {
        // Simulate a NotAuthenticatedException
        ExceptionControllerAdvice.NotAuthenticatedException ex = new ExceptionControllerAdvice.NotAuthenticatedException("User not authenticated");

        // Call the exception handler method
        ResponseEntity<ErrorResponse> response = exceptionControllerAdvice.handleNotAuthenticatedException(ex);

        // Assertions to check the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User not authenticated", response.getBody().getMessage());
    }

    @Test
     void testHandleNullPointerException() {
        // Simulate a NullPointerException
        ExceptionControllerAdvice.NullPointerException ex = new ExceptionControllerAdvice.NullPointerException("Null pointer error");

        // Call the exception handler method
        ResponseEntity<ErrorResponse> response = exceptionControllerAdvice.handleNullPointerException(ex);

        // Assertions to check the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Null pointer error", response.getBody().getMessage());
    }
}