package com.ouitrips.app.utils.encryptutil;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa-encrypt")
public record RSAKeysConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
