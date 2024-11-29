package com.ouitrips.app.services.security.impl;

import com.ouitrips.app.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks before each test
    }

    @Test
    void testIsAuthenticatedWithBearerToken() {
        // Arrange
        String token = "validToken";
        String authorizationHeader = "Bearer " + token;
        when(request.getHeader("Authorization")).thenReturn(authorizationHeader);
        when(jwtService.isTokenValid(token)).thenReturn(true);

        // Act
        Boolean isAuthenticated = authenticationService.isAuthenticated(request);

        // Assert
        assertTrue(isAuthenticated);
        verify(jwtService, times(1)).isTokenValid(token); // Ensure isTokenValid was called once
    }

    @Test
    void testIsAuthenticatedWithBearerTokenInvalid() {
        // Arrange
        String token = "invalidToken";
        String authorizationHeader = "Bearer " + token;
        when(request.getHeader("Authorization")).thenReturn(authorizationHeader);
        when(jwtService.isTokenValid(token)).thenReturn(false);

        // Act
        Boolean isAuthenticated = authenticationService.isAuthenticated(request);

        // Assert
        assertFalse(isAuthenticated);
        verify(jwtService, times(1)).isTokenValid(token);
    }

    @Test
    void testIsAuthenticatedWithJwtTokenFromRequest() {
        // Arrange
        String jwt = "validJwtToken";
        when(request.getHeader("Authorization")).thenReturn(null); // No Bearer token in header
        when(jwtService.parseJwt(request)).thenReturn(jwt);
        when(jwtService.isTokenValid(jwt)).thenReturn(true);

        // Act
        Boolean isAuthenticated = authenticationService.isAuthenticated(request);

        // Assert
        assertTrue(isAuthenticated);
        verify(jwtService, times(1)).parseJwt(request);
        verify(jwtService, times(1)).isTokenValid(jwt);
    }

    @Test
    void testIsAuthenticatedWithJwtTokenFromRequestInvalid() {
        // Arrange
        String jwt = "invalidJwtToken";
        when(request.getHeader("Authorization")).thenReturn(null);
        when(jwtService.parseJwt(request)).thenReturn(jwt);
        when(jwtService.isTokenValid(jwt)).thenReturn(false);

        // Act
        Boolean isAuthenticated = authenticationService.isAuthenticated(request);

        // Assert
        assertFalse(isAuthenticated);
        verify(jwtService, times(1)).parseJwt(request);
        verify(jwtService, times(1)).isTokenValid(jwt);
    }

    @Test
    void testIsAuthenticatedWhenNoAuthorizationHeaderAndNoJwtToken() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);
        when(jwtService.parseJwt(request)).thenReturn(null);

        // Act
        Boolean isAuthenticated = authenticationService.isAuthenticated(request);

        // Assert
        assertFalse(isAuthenticated);
        verify(jwtService, times(1)).parseJwt(request);
    }








}