package com.ouitrips.app.utils.encryptutil;

import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

@Service
public class RSAService {

    private final RSAKeysConfig rsaKeysConfig;

    public RSAService(RSAKeysConfig rsaKeysConfig)  {
        this.rsaKeysConfig = rsaKeysConfig;
    }

    public String encrypt(String message) {
        try{
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, this.getPublicKey());
            byte[] encryptedBytes = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.GeneralException("Error encrypt data");
        }
    }

    public String decrypt(String encryptedMessage) {
       Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            cipher.init(Cipher.DECRYPT_MODE, this.getPrivateKey());
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.GeneralException("Error decrypt data");
        }
    }

    private PublicKey getPublicKey() {
        return rsaKeysConfig.publicKey();
    }

    private PrivateKey getPrivateKey() {
        return rsaKeysConfig.privateKey();
    }
}