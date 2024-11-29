package com.ouitrips.app.services.security;

import com.ouitrips.app.dtos.requests.SignInRequest;
import com.ouitrips.app.entities.security.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthService {
    ResponseEntity<?> signIn(SignInRequest signInRequest);
    ResponseEntity<?> signEncrypted(SignInRequest signInRequest);
    ResponseEntity<?> forceSignInByUser(String reference);
    ResponseEntity<?> signOut();
    ResponseEntity<?> authenticatedSocial(String type, String email, boolean emailVerified, String name, String givenName, String familyName, String picture, String facebookId, String googleId, String idToken);
    Map<String, Object> signUp(String firstName, String lastName, String password, String email, String phone, String type);
    User validateInscription(String validationCode, String securityToken);
}
