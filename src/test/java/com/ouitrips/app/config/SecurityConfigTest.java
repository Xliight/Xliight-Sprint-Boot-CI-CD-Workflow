package com.ouitrips.app.config;

import static org.junit.jupiter.api.Assertions.*;

import com.ouitrips.app.config.services.UserDetailsServiceImpl;
import com.ouitrips.app.utils.VariableProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class SecurityConfigTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private AuthTokenFilter authTokenFilter;
    @Mock
    private RsakeysConfig rsakeysConfig;
    @Mock
    private VariableProperty variableProperty;
    @Mock
    private CustomBearerTokenEntryPoint customBearerTokenEntryPoint;
    @Mock
    private CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    @Mock
    private CustomBearerTokenAccessDeniedEntryPoint customBearerTokenAccessDeniedEntryPoint;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        // Initialize the SecurityConfig with mocked dependencies
        securityConfig = new SecurityConfig(rsakeysConfig, userDetailsService, authTokenFilter,
                variableProperty, customBearerTokenEntryPoint, customBasicAuthenticationEntryPoint,
                customBearerTokenAccessDeniedEntryPoint);
    }

    @Test
    void testConstructorInitialization() throws NoSuchFieldException {
        assertNotNull(securityConfig); // Assert that the object is created
        assertNotNull(securityConfig.getClass().getDeclaredField("userDetailsService"));
        assertNotNull(securityConfig.getClass().getDeclaredField("rsakeysConfig"));
        assertNotNull(securityConfig.getClass().getDeclaredField("authTokenFilter"));
        assertNotNull(securityConfig.getClass().getDeclaredField("variableProperty"));
        assertNotNull(securityConfig.getClass().getDeclaredField("customBearerTokenEntryPoint"));
        assertNotNull(securityConfig.getClass().getDeclaredField("customBasicAuthenticationEntryPoint"));
        assertNotNull(securityConfig.getClass().getDeclaredField("customBearerTokenAccessDeniedEntryPoint"));
    }


}
