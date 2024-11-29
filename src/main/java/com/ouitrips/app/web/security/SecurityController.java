package com.ouitrips.app.web.security;

import com.ouitrips.app.constants.Response;
import com.ouitrips.app.dtos.requests.SignInRequest;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.services.security.AuthService;
import com.ouitrips.app.services.security.AuthenticationService;
import com.ouitrips.app.services.security.ResetPasswordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import static com.ouitrips.app.constants.RequestNameConstant.REQUEST_MESSAGE;

@RestController
@RequestMapping("${REST_NAME}/security")
public class SecurityController {
    private final ResetPasswordService resetPasswordService;
    private final AuthService authService;
    private final AuthenticationService authenticationService;
    public SecurityController(ResetPasswordService resetPasswordService, AuthService authService, AuthenticationService authenticationService) {
        this.resetPasswordService = resetPasswordService;
        this.authenticationService = authenticationService;
        this.authService = authService;
    }

    @PostMapping(value = "/auth/sign_in")
    public ResponseEntity<?> signIn(@RequestParam String login,
                                    @RequestParam String password) {
        ResponseEntity<?> signInResponse = authService.signIn(new SignInRequest(login,password));
        if(signInResponse==null){
            throw new ExceptionControllerAdvice.ErrorPasswordException();
        }
        return signInResponse;
    }
    @PostMapping(value = "/auth/sign_encrypted")
    public ResponseEntity<?> signInEncrypted(@RequestParam String login,
                                    @RequestParam String password) {
        ResponseEntity<?> signInResponse = authService.signEncrypted(new SignInRequest(login,password));
        if(signInResponse==null){
            throw new ExceptionControllerAdvice.ErrorPasswordException();
        }
        return signInResponse;
    }
    @PostMapping(value = "/auth/sign_up")
    public ResponseEntity<?> registerUserFormData(
            @RequestParam(name = "first_name") String firstName,
            @RequestParam(name = "last_name") String lastName,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "phone") String phone,
            @RequestParam String password) {
        return Response.responseData(authService.signUp(firstName, lastName, password, email, phone, "email"));
    }
    //Validation inscription
    @PostMapping("/validate_inscription")
    ResponseEntity<?> validateInscription(
            @RequestParam("validation_code") String validationCode,
            @RequestParam(value = "security_token",required = false) String securityToken
    ) {
        User user = this.authService.validateInscription(validationCode, securityToken);
        if(user != null) {
            return Response.successMessage("Validation success!");
//            return authService.forceAuthByUser(user);
        } else
            throw new ExceptionControllerAdvice.GeneralException("Validation error");
    }

    @RequestMapping(value = "/auth/authenticated", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> authenticated(HttpServletRequest request) {
        if(authenticationService.isAuthenticated(request)){
            return Response.responseData(
                    Map.of(
                            REQUEST_MESSAGE, "successfully",
                            "connected", true
                    )
            );
        }else{
            return Response.responseData(
                    Map.of(
                            REQUEST_MESSAGE, "Unknown",
                            "connected", false
                    )
            );
        }
    }
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpSession httpSession, HttpServletRequest request) {
        if(!this.authenticationService.isAuthenticated(request)){
            return Response.successMessage("Unknown");
        }
        httpSession.invalidate();
        return authService.signOut();
    }

    @PostMapping("/auth/force_sign_in")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> forceSignInWithObject(@RequestParam("reference_user") String referenceUser) {
        return authService.forceSignInByUser(referenceUser);
    }

    @PostMapping("/auth/social")
    public ResponseEntity<?> authenticatedWithGoogle(@RequestParam("type") String type,
                                                     @RequestParam("email") String email,
                                                     @RequestParam(value = "first_name", required = false) String givenName,
                                                     @RequestParam(value = "last_name", required = false) String familyName,
                                                     @RequestParam(value = "id_token", required = false) String idToken,
                                                     @RequestParam(value = "picture",required = false) String picture,
                                                     @RequestParam(value = "email_verified",required = false ,defaultValue = "false") boolean emailVerified,
                                                     @RequestParam(value = "name",required = false) String name,
                                                     @RequestParam(value = "google_id",required = false) String googleId,
                                                     @RequestParam(value = "facebook_id",required = false) String facebookId
    ) {
        return authService.authenticatedSocial(type, email, emailVerified, name, givenName, familyName, picture, facebookId, googleId, idToken);
    }

    //Forgot Password
    @PostMapping("/forgot_password")
    ResponseEntity<?> forgotPassword(@RequestParam("contact") String contact,
                                     @RequestParam("type") String type) {
        return Response.responseData(resetPasswordService.forgotPassword(contact, type));
    }

    @PostMapping("/verification_code")
     ResponseEntity<?> verificationCode(
             @RequestParam("verification_code") String verificationCode,
             @RequestParam(value = "security_token", required = false) String token
    )
    {
        resetPasswordService.verificationCode(verificationCode, token);
        return Response.successMessage("Verification code is correct");
    }

    @PostMapping("/reset_password")
    ResponseEntity<?> setNewPassword(
            @RequestParam("new_password") String newPassword,
            @RequestParam(value = "security_token", required = false) String token
    )  {
        resetPasswordService.resetPassword(newPassword, token);
        return Response.successMessage("Password has been reset successfully");
    }
}