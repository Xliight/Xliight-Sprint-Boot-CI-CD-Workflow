package com.ouitrips.app.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.ouitrips.app.utils.VariableProperty;
import com.ouitrips.app.config.services.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity()
@Slf4j
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthTokenFilter authTokenFilter;
    private final RsakeysConfig rsakeysConfig;
    private final VariableProperty variableProperty;
    private final CustomBearerTokenEntryPoint customBearerTokenEntryPoint;
    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    private final CustomBearerTokenAccessDeniedEntryPoint customBearerTokenAccessDeniedEntryPoint;

    public SecurityConfig(RsakeysConfig rsakeysConfig, UserDetailsServiceImpl userDetailsService, @Lazy AuthTokenFilter authTokenFilter, VariableProperty variableProperty, CustomBearerTokenEntryPoint customBearerTokenEntryPoint, CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint, CustomBearerTokenAccessDeniedEntryPoint customBearerTokenAccessDeniedEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.rsakeysConfig = rsakeysConfig;
        this.authTokenFilter = authTokenFilter;
        this.variableProperty = variableProperty;
        this.customBearerTokenEntryPoint = customBearerTokenEntryPoint;
        this.customBasicAuthenticationEntryPoint = customBasicAuthenticationEntryPoint;
        this.customBearerTokenAccessDeniedEntryPoint = customBearerTokenAccessDeniedEntryPoint;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("X-Requested-With", "Authorization", "X-API-KEY", "Origin", "Content-Type", "Accept", "Accept-Encoding"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList(CorsOriginAllowed.ORIGIN_ALLOWED)); // Add allowed origins
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.addFilterBefore(authTokenFilter, BasicAuthenticationFilter.class);
        httpSecurity
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui.html")).permitAll()
//                        .requestMatchers(new AntPathRequestMatcher("/api/data")).permitAll()
//                        .requestMatchers(new AntPathRequestMatcher("/api/agencies")).authenticated()
//                        .requestMatchers(new AntPathRequestMatcher("/api/agencies/**")).authenticated()
//                        .requestMatchers(new AntPathRequestMatcher("/api/agenciescircuit/**")).authenticated()
//                        .requestMatchers(new AntPathRequestMatcher("/api/agenciescir/**/circuits/**")).authenticated()
//                        .requestMatchers(new AntPathRequestMatcher("/api/searchcircuits/**")).permitAll()
//                        .requestMatchers(new AntPathRequestMatcher("/api/geolocation/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll().requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher(variableProperty.getRestName()+"/public/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher(variableProperty.getRestName()+"/security/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher(variableProperty.getRestName()+"/status/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                        )
                        .authenticationEntryPoint(customBearerTokenEntryPoint)
                        .authenticationEntryPoint(customBasicAuthenticationEntryPoint)
                        .accessDeniedHandler(customBearerTokenAccessDeniedEntryPoint)
                );
        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsakeysConfig.publicKey()).build();
    }
    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk= new RSAKey.Builder(rsakeysConfig.publicKey()).privateKey(rsakeysConfig.privateKey()).build();
        JWKSource<SecurityContext> jwkSource= new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}