package com.ouitrips.app.services.security.impl;

import com.ouitrips.app.config.JwtService;
import com.ouitrips.app.services.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtService jwtService;
    @Override
    public Boolean isAuthenticated(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if(jwtService.isTokenValid(token)){
                return true;
            }
        }else{
            String jwt = jwtService.parseJwt(request);
            if (jwt != null && !jwt.isEmpty() && jwtService.isTokenValid(jwt)) {
                return true;
            }
        }
        return false;
    }
}
