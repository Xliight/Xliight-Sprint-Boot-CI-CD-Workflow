package com.ouitrips.app.services.security;

public interface ResetPasswordService {
    void verificationCode(String verificationCode, String token);
    void resetPassword(String newPassword, String token);
    Object forgotPassword(String contact, String type);
}
