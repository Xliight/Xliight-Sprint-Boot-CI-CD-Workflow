package com.ouitrips.app.config;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.ouitrips.app.utils.VariableProperty;
import com.ouitrips.app.repositories.security.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class JwtService {
    private final VariableProperty variableProperty;
    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;

    public JwtService(VariableProperty variableProperty, JwtDecoder jwtDecoder, JwtEncoder jwtEncoder, UserRepository userRepository) {
        this.variableProperty = variableProperty;
        this.jwtDecoder = jwtDecoder;
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
    }


    public String extractSubjectFromToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            if (jwt.getSubject() == null) {
                return null;
            }
            return jwt.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public String generateToken(String username, String scope) {
        Instant instant=Instant.now();
        JwtClaimsSet jwtClaimsSet=JwtClaimsSet.builder()
                .subject(username)
                .issuedAt(instant)
                .expiresAt(instant.plus(variableProperty.getExpirationtoken(), ChronoUnit.MILLIS))
                .issuer("co-rest-security-oauth2")
                .claim("scope",scope)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    public String generateTokenForgotPassword(String subject) {
        Instant instant=Instant.now();
        Instant expireAt = instant.plus(variableProperty.getSessionExpirationTime(), ChronoUnit.MINUTES);
        JwtClaimsSet jwtClaimsSet=JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(instant)
                .expiresAt(expireAt)
                .issuer("co-rest-security-forgot-password")
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    public boolean isTokenValid(String token) {
        if(token==null || token.isEmpty() || isTokenExpired(token)){
            return false;

        }
        final String username = extractSubjectFromToken(token);
        if(username==null){
            return false;}
        return (userRepository.existsByUsername(username) || userRepository.existsByReference(username));
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = extractExpiration(token);
        if (expirationDate == null) {

            return true;
        }
        return expirationDate.before(new Date());
    }

    public Date extractExpiration(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        if (jwt.getExpiresAt() == null) {
            return null;
        }
        return Date.from(jwt.getExpiresAt());
    }
    public String extractRoles(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        String authorityNames = (String) jwt.getClaims().get("scope");
        if (jwt.getExpiresAt() == null) {
            return null;
        }

        return authorityNames;
    }

    public String getJwtFromCookies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, variableProperty.getJwtCookieName());
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(variableProperty.getJwtCookieName(), "")
                .path(variableProperty.getRestName())
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .build();
    }
    public String parseJwt(HttpServletRequest request) {
        return getJwtFromCookies(request);
    }

}
