package com.ouitrips.app.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ouitrips.app.repositories.security.UserRepository;
import com.ouitrips.app.utils.VariableProperty;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jwt.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

class JwtServiceTest {

    @Mock private VariableProperty variableProperty;
    @Mock private JwtDecoder jwtDecoder;
    @Mock private JwtEncoder jwtEncoder;
    @Mock private UserRepository userRepository;
    @Mock private Jwt jwtMock;

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        jwtService = new JwtService(variableProperty, jwtDecoder, jwtEncoder, userRepository);
    }

    @Test
    void testExtractSubjectFromToken_validToken() {
        // Given
        String token = "valid.token.here";
        String expectedSubject = "user123";

        // Mocking the behavior of JwtDecoder
        when(jwtDecoder.decode(token)).thenReturn(jwtMock);
        when(jwtMock.getSubject()).thenReturn(expectedSubject);

        // When
        String subject = jwtService.extractSubjectFromToken(token);

        // Then
        assertNotNull(subject);
        assertEquals(expectedSubject, subject);
    }

    @Test
    void testExtractSubjectFromToken_nullSubject() {
        // Given
        String token = "valid.token.here";

        // Mocking the behavior of JwtDecoder
        when(jwtDecoder.decode(token)).thenReturn(jwtMock);
        when(jwtMock.getSubject()).thenReturn(null);

        // When
        String subject = jwtService.extractSubjectFromToken(token);

        // Then
        assertNull(subject);
    }

    @Test
    void testExtractSubjectFromToken_exception() {
        // Given
        String token = "valid.token.here";

        // Mocking the behavior of JwtDecoder to throw an exception
        when(jwtDecoder.decode(token)).thenThrow(new RuntimeException("Invalid token"));

        // When
        String subject = jwtService.extractSubjectFromToken(token);

        // Then
        assertNull(subject);
    }

    @Test
    void testGenerateToken() {
        jwtService = new JwtService(variableProperty, jwtDecoder, jwtEncoder, userRepository);

        // Arrange
        String username = "testUser";
        String scope = "read:write";

        // Mocking JwtEncoder's behavior
        Jwt mockJwt = mock(Jwt.class);
        String tokenValue = "mockedTokenValue";
        when(mockJwt.getTokenValue()).thenReturn(tokenValue);

        // Mock the encode method to return the mocked Jwt object
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        // Mock the variableProperty to return a valid expiration value
        when(variableProperty.getExpirationtoken()).thenReturn(3600000);  // 1 hour in milliseconds

        // Act
        String token = jwtService.generateToken(username, scope);

        // Assert
        assertNotNull(token);
        assertEquals("mockedTokenValue", token);
    }

    @Test
    void testGenerateTokenForgotPassword() {
        jwtService = new JwtService(variableProperty, null, jwtEncoder, null); // Only mock what's needed

        // Arrange
        String subject = "testUser";

        // Mock the variableProperty to return a valid session expiration time
        when(variableProperty.getSessionExpirationTime()).thenReturn(30);  // 30 minutes for example

        // Mock the behavior of JwtEncoder
        Jwt mockJwt = mock(Jwt.class);
        String tokenValue = "mockedForgotPasswordTokenValue";
        when(mockJwt.getTokenValue()).thenReturn(tokenValue);

        // Mock jwtEncoder.encode() to return the mocked Jwt object
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        // Act
        String token = jwtService.generateTokenForgotPassword(subject);

        // Assert
        assertNotNull(token);
        assertEquals("mockedForgotPasswordTokenValue", token);
    }


    @Test
    void testIsTokenValid_whenTokenIsNull() {
        // Act
        boolean result = jwtService.isTokenValid(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsTokenValid_whenTokenIsEmpty() {
        // Act
        boolean result = jwtService.isTokenValid("");

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsTokenValid_whenTokenIsExpired() {
        // Arrange
        String token = "expiredToken";
        JwtDecoder jwtDecoderMock = mock(JwtDecoder.class);
        Jwt jwtMock = mock(Jwt.class);

        // Mock the behavior of the JWT decoder
        when(jwtDecoderMock.decode(token)).thenReturn(jwtMock);
        when(jwtMock.getExpiresAt()).thenReturn(Instant.now().minusSeconds(60));  // Simulate an expired token

        JwtService jwtServicec = new JwtService(variableProperty, jwtDecoderMock, jwtEncoder, userRepository);

        // Act
        boolean result = jwtServicec.isTokenValid(token);

        // Assert
        assertFalse(result); // Since the token is expired, it should return false
    }

    @Test
    void testExtractExpiration_withValidExpiration() {
        String token = "expiredToken";
        JwtDecoder jwtDecoderr = mock(JwtDecoder.class);
        Jwt jwtMock = mock(Jwt.class);

        when(jwtDecoderr.decode(token)).thenReturn(jwtMock);
        when(jwtMock.getExpiresAt()).thenReturn(Instant.now().minusSeconds(60));
        JwtService jwtServicec = new JwtService(variableProperty, jwtDecoderr, jwtEncoder, userRepository);

        Date s= jwtServicec.extractExpiration(token);
        System.out.println(s);

    }
    @Test
    void testExtractExpiration_withInvalidToken() {
        String token = "invalid-token";
        when(jwtDecoder.decode(token)).thenThrow(new IllegalArgumentException("Invalid token"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            jwtService.extractExpiration(token);
        });

        assertEquals("Invalid token", exception.getMessage());
    }

    @Mock
    private Jwt jwt;
    @Test
    public void testExtractRoles_withValidToken() {
        // Prepare mock JWT claims
        Map<String, Object> claims = Map.of("scope", "ROLE_USER");

        // Mock the Jwt object
        when(jwt.getClaims()).thenReturn(claims);
        when(jwtDecoder.decode("validToken")).thenReturn(jwt);
        when(jwt.getExpiresAt()).thenReturn(Instant.now().minusSeconds(60));

        // Call the method under test
        String roles = jwtService.extractRoles("validToken");
        // Assert the result
        assertEquals("ROLE_USER", roles);
    }

    @Mock
    private HttpServletRequest request;

    @Mock
    private Cookie cookie;
    @Test
    public void testGetJwtFromCookies_withValidCookie() {
        // Mock the behavior of WebUtils.getCookie and VariableProperty
        when(variableProperty.getJwtCookieName()).thenReturn("JWT_COOKIE_NAME");

        // Create a mock cookie and mock the getCookies method
        Cookie[] cookies = new Cookie[]{new Cookie("JWT_COOKIE_NAME", "valid-jwt-token")};
        when(request.getCookies()).thenReturn(cookies);

        // Call the method under test
        String jwt = jwtService.getJwtFromCookies(request);

        // Assert the result
        assertEquals("valid-jwt-token", jwt);
    }
    @Test
    public void testGetJwtFromCookies_noCookie() {
        // Mock the behavior of WebUtils.getCookie and VariableProperty
        when(variableProperty.getJwtCookieName()).thenReturn("JWT_COOKIE_NAME");

        // Return null when there are no cookies
        when(request.getCookies()).thenReturn(null);

        // Call the method under test
        String jwt = jwtService.getJwtFromCookies(request);

        // Assert the result
        assertNull(jwt);
    }


    @Test
    public void testGetCleanJwtCookie() {
        // Mock the behavior of variableProperty
        when(variableProperty.getJwtCookieName()).thenReturn("JWT_COOKIE_NAME");
        when(variableProperty.getRestName()).thenReturn("/api");

        // Call the method under test
        ResponseCookie cookie = jwtService.getCleanJwtCookie();

        // Assert the cookie's properties
        assertNotNull(cookie); // Ensure the cookie is not null
        assertEquals("JWT_COOKIE_NAME", cookie.getName()); // Ensure the cookie's name is correct
        assertEquals("/api", cookie.getPath()); // Ensure the path is correct

        // Updated to assert Duration instead of 0
        assertEquals(Duration.ZERO, cookie.getMaxAge()); // Ensure the max age is Duration.ZERO (equivalent to 0 seconds)

        assertTrue(cookie.isHttpOnly()); // Ensure the HttpOnly flag is true
        assertTrue(cookie.isSecure()); // Ensure the Secure flag is true
    }

    @Test
    public void testParseJwt_withValidCookie() {
        // Arrange: Create a mock cookie and mock the request behavior
        String jwtToken = "test-jwt-token";
        Cookie mockCookie = new Cookie("JWT_COOKIE_NAME", jwtToken);
        when(variableProperty.getJwtCookieName()).thenReturn("JWT_COOKIE_NAME");
        when(request.getCookies()).thenReturn(new Cookie[] { mockCookie });

        // Act: Call the method under test
        String result = jwtService.parseJwt(request);

        // Assert: Verify the result is correct
        assertEquals(jwtToken, result); // Ensure the returned JWT token is correct
    }

    @Test
    public void testParseJwt_withNoCookie() {
        // Arrange: Mock no cookies being present
        when(variableProperty.getJwtCookieName()).thenReturn("JWT_COOKIE_NAME");
        when(request.getCookies()).thenReturn(new Cookie[] {});

        // Act: Call the method under test
        String result = jwtService.parseJwt(request);

        // Assert: Verify the result is null as no JWT cookie is present
        assertNull(result); // Ensure the result is null as no cookie was found
    }

}