package com.ouitrips.app.config;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfig implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(SessionConfig.class);
    @Value("${session_expiration_time}")
    Integer sessionExpirationTime;//minutes
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setMaxInactiveInterval(sessionExpirationTime * 60);
        se.getSession().setAttribute("SameSite", "None");
        se.getSession().setAttribute("Secure", "true");

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("Session destroyed");
    }
}
