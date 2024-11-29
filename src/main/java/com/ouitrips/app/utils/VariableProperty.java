package com.ouitrips.app.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class VariableProperty {
    @Value("${co.rest.expirationtoken}")
    private int expirationtoken;

    @Value("${co.rest.refresh-token.expiration}")
    private int refreshExpiration;

    @Value("${co.rest.jwt.cookie.name}")
    private String jwtCookieName;

    @Value("${UPLOAD_FILE_PATH}")
    private String pathFile;

    @Value("${REST_NAME}")
    private String restName;

    @Value("${send_email_live}")
    private String sendEmailLive;

    @Value("${send_sms_live}")
    private String sendSmsLive;

    @Value("${session_expiration_time}")
    private Integer sessionExpirationTime;
    @Value("${smtp_host}")
    private String host;
    @Value("${smtp_port}")
    private Integer port;
    @Value("${smtp_username}")
    private String username;
    @Value("${smtp_password}")
    private String password;
    @Value("${smtp_from}")
    private String emailFrom;
    @Value("${smtp_from_name}")
    private String emailFromName;
    //Api key
    @Value("${api_key_google}")
    private String apiKeyGoogle;

    @Value("${cleanup.oldCircuit.duration}")
    private Integer oldCircuitDuration;
}