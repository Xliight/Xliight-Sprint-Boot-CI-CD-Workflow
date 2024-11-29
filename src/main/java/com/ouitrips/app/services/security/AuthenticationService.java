package com.ouitrips.app.services.security;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    Boolean isAuthenticated(HttpServletRequest request);
}
