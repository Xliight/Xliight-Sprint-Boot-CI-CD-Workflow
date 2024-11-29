package com.ouitrips.app.services.security.impl;
import com.ouitrips.app.constants.TemplateConstants;
import com.ouitrips.app.dtos.requests.SignInRequest;
import com.ouitrips.app.enums.Role;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.services.messagingsystemservice.email.EmailService;
import com.ouitrips.app.services.messagingsystemservice.sms.SmsService;
import com.ouitrips.app.services.security.AuthService;
import com.ouitrips.app.config.JwtService;
import com.ouitrips.app.config.services.UserDetailsServiceImpl;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.mapper.security.SignInMapper;
import com.ouitrips.app.repositories.security.UserRepository;
import com.ouitrips.app.utils.DateUtil;
import com.ouitrips.app.utils.RandomStringUtils;
import com.ouitrips.app.utils.UserUtils;
import com.ouitrips.app.utils.VariableProperty;
import com.ouitrips.app.utils.encryptutil.RSAService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.ouitrips.app.utils.RandomStringUtils.generateRandomSixDigits;


import static com.ouitrips.app.constants.RequestNameConstant.REQUEST_MESSAGE;
import static com.ouitrips.app.constants.RequestNameConstant.REQUEST_STATUS;
import static com.ouitrips.app.constants.StatusCodeConstant.STATUS_OK;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService
{
    public static final String EMAIL = "email";
    public static final String USER_NOT_FOUND = "user not found";
    public static final String ACTION = "action";
    public static final String VERIFICATION_CODE = "verification_code";
    public static final String REFERENCE_USER = "reference_user";
    public static final String CREATED_AT = "created_at";
    public static final String USER_REFERENCE = "user_reference";
    public static final String VALIDATION_CODE = "validation_code";
    public static final String MESSAGE = "message";
    public static final String INVALID_OR_EXPIRED_TOKEN = "invalid or expired token";
    private final UserRepository userRepository;
    //Mapper
    private final SignInMapper signInMapper;
    //Services
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final EmailService emailService;
    private final SmsService smsService;
    private final RSAService rsaService;
    //Properties & Utils
    private final VariableProperty variableProperty;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserUtils userUtils;
    private final DateUtil dateUtil;
    //Store data
    private final Map<String, Map<String, String>> dataStore = new ConcurrentHashMap<>();


    @Override
    @Transactional
    public Map<String, Object> signUp(String firstName, String lastName, String password, String email, String phone, String type) {
        Map<String, Object> response = new HashMap<>();
        User user = register(getUsername(firstName, lastName), password, email, "", null, null, firstName, lastName, null, null, phone);
        String fullName = (firstName != null ? firstName + " " : "") + (lastName != null ? lastName : "");
        if (type != null) {
            if(type.equals(EMAIL))
                this.sendEmailVerification(user, email, fullName,response);
            else if(type.equals("gsm"))
                this.sendSmsVerification(user, fullName, phone, response);
        }else this.sendEmailVerification(user, email, fullName,response);
        return response;
    }

    public String getUsername(String firstName, String lastName){
        String fullName = (firstName != null ? firstName + "_" : "") + (lastName != null ? lastName : "");
        return this.userUtils.generateUsername(fullName);
    }
    public User register(String username, String password, String email, String company,
                         String googleId,
                         String facebookId,
                         String firstName,
                         String lastName,
                         String picture,
                         String idToken,
                         String phone) {
        if (userRepository.existsByUsername(username)) {
            throw new ExceptionControllerAdvice.RegisterException("Username already exist!");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ExceptionControllerAdvice.RegisterException("Email already exist!");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));//todo change this with random password or default password for google or facebook auth
        user.setEnabled(true);
        user.setGoogleId(googleId);//for google
        user.setFacebookId(facebookId);//for facebook
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLastLogin(Instant.now());
        /*if(role!=null){
            String rolesJson = "[\""+role+"\"]";
            user.setRoles(rolesJson);
        }*/
        String rolesJson = "[\""+ Role.ROLE_USER.name() +"\"]";
        user.setRoles(rolesJson);
        user.setPhone(phone);
        user.setCreatedDate(Instant.now());
        user.setPicture(picture);//todo should passed from params
        user.setReference(UUID.randomUUID().toString());//todo what we should do in reference: generate unique reference or what?
        user.setUsername(username);//todo we should put a username or generate a new unique one
        user.setCompanyName(company);
//        user.setIdTokenGoogle(idToken);todo check this the id token not exist on the user table
        return userRepository.save(user);
    }

    @Override
    public ResponseEntity<?> signIn(SignInRequest request) {
        User user = userRepository.findByUsername(request.username());//findByUsername
        if(user == null){//findByEmail
            user = userRepository.findByEmail(request.username());//findByEmail
        }
        if(user == null){//findByEmail
            user = userRepository.findUserByGsm(request.username()).orElse(null);//findByGsm
        }
        if(user == null){
            throw new ExceptionControllerAdvice.SignInUsernameNotFoundException();
        }
        SignInRequest signInRequest = request.withUsername(user.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.username(), signInRequest.password())
            );
            if (!authentication.isAuthenticated()) {
                throw new ExceptionControllerAdvice.ErrorPasswordException();
            }
            return getResponseWithJwtAndWithCredentials(getSignInResponse(authentication, signInRequest.username()));
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.ErrorPasswordException();
        }
    }

    @Override
    public ResponseEntity<?> signEncrypted(SignInRequest signInRequest) {
        String decryptedPassword = rsaService.decrypt(signInRequest.password());
        SignInRequest signInRequest2 = new SignInRequest(signInRequest.username(), decryptedPassword);
        return this.signIn(signInRequest2);
    }

    @Override
    public ResponseEntity<?> forceSignInByUser(String reference) {
        User user = userRepository.findByReference(reference).orElseThrow(
                ()->new ExceptionControllerAdvice.ObjectNotFoundException(USER_NOT_FOUND)
        );
        return forceAuthByUser(user);
    }

    public ResponseEntity<?> forceAuthByUser(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        UsernamePasswordAuthenticationToken authenticatedToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        eventPublisher.publishEvent(new AuthenticationSuccessEvent(authenticatedToken));
        return getResponseWithJwtAndWithCredentials(getSignInResponse(authenticatedToken, user.getUsername()));
    }

    @Override
    public ResponseEntity<?> signOut() {
        ResponseCookie cookie = jwtService.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of(REQUEST_STATUS, STATUS_OK,
                        REQUEST_MESSAGE,"Logout out successfully"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> authenticatedSocial(String type, String email, boolean emailVerified, String name, String givenName, String familyName, String picture, String facebookId, String googleId, String idToken) {
        switch (type) {
            case "facebook" -> {
                return authenticatedWithFacebook(email,
                        emailVerified,
                        name,
                        givenName,
                        familyName,
                        facebookId,
                        picture,
                        idToken);
            }
            case "google" -> {
                return authenticatedGoogleId(email,
                        emailVerified,
                        name,
                        givenName,
                        familyName,
                        googleId,
                        picture,
                        idToken);
            }
            case "default" -> {
                return null;
            }
        }
        return null;
    }

    private ResponseEntity<?> authenticatedGoogleId(String email,
                                                    boolean emailVerified,
                                                    String name,
                                                    String givenName,
                                                    String familyName,
                                                    String googleId,
                                                    String picture,
                                                    String idToken) {
        User user = userRepository.findByEmail(email);
        if (user == null) {//new user
            user = register(getUsername(givenName, familyName), UUID.randomUUID().toString(), email, "", googleId, null, givenName, familyName, picture, idToken, null);
        }
        return forceAuthByUser(user);
    }
    private ResponseEntity<?> authenticatedWithFacebook(String email,
                                                        boolean emailVerified,
                                                        String name,
                                                        String givenName,
                                                        String familyName,
                                                        String facebookId,
                                                        String picture,
                                                        String idToken) {
        User user = userRepository.findByEmail(email);
        if (user == null) {//new user
            user = register(getUsername(givenName, familyName), UUID.randomUUID().toString(), email, "", null, facebookId, givenName, familyName, picture, idToken, null);
        }
        return forceAuthByUser(user);
    }

    public Map<String, Object> getSignInResponse(Authentication authentication, String username){
        String scope=authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        String jwtToken = jwtService.generateToken(username, scope);
        Map<String, Object> signInResponse = signInMapper.apply(userRepository.findByUsername(username));
        Map<String, Object> data = (Map<String, Object>) signInResponse.get("data");
        if (data != null) {
            Map<String, Object> dataUser = (Map<String, Object>) data.get("data_user");
            if (dataUser != null) {
                dataUser.put("_token_", jwtToken);
            }
        }
        return signInResponse;
    }

    String getTokenFromDataResponse(Map<String, Object> signInResponse){
        Map<String, Object> data = (Map<String, Object>) signInResponse.get("data");
        if (data != null) {
            Map<String, Object> dataUser = (Map<String, Object>) data.get("data_user");
            if (dataUser != null) {
                return (String) dataUser.get("_token_");
            }
        }
        return "";
    }

    public ResponseEntity<?> getResponseWithJwtAndWithCredentials(Map<String, Object> signInResponse){
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        ResponseCookie.from(variableProperty.getJwtCookieName(),
                                        getTokenFromDataResponse(signInResponse))
                                .sameSite("None")
                                .secure(true)
                                .path(variableProperty.getRestName())
                                .maxAge(variableProperty.getExpirationtoken()/1000)
                                .httpOnly(true).build().toString()
                )
                .body(signInResponse);
    }

    public void sendSmsVerification(User user, String phoneNumber, String fullName, Map<String, Object> response){
        String codeVerification = generateRandomSixDigits();
        if(user != null) {
            user.setTokenPassword(codeVerification);
            userRepository.save(user);
        }
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("name", fullName);
        templateModel.put("code", codeVerification);
        String token = generateSecretKey();
        response.put("security_token", token);
        this.dataStore.put(token, Map.of(ACTION ,VERIFICATION_CODE,
                REFERENCE_USER, user != null?user.getReference():"",
                "type", "gsm",
                CREATED_AT, Instant.now().toString()));
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute(USER_REFERENCE, user != null ? user.getReference() : null);
        request.getSession().setAttribute(ACTION , VERIFICATION_CODE);
        request.getSession().setAttribute(VALIDATION_CODE, codeVerification);
        request.getSession().setAttribute("type", "gsm");
        if(variableProperty.getSendSmsLive().equals("true")) {
            try {
                smsService.sendSms("OuiTrips",
                        phoneNumber,
                        null,
                        templateModel,
                        TemplateConstants.SMS_RESET_PASSWORD_CODE_VALIDATION,
                        null);
            }catch(Exception e) {
                throw new ExceptionControllerAdvice.SendSmsException("Error sending sms");
            }
        }else {
            response.put(VERIFICATION_CODE, codeVerification);
        }
    }
    private String generateSecretKey(){
        String secretKey;
        do {
            secretKey = RandomStringUtils.generateRandomStringSecurity();
        }while (this.dataStore.get(secretKey)!=null);
        return secretKey;
    }
    public void sendEmailVerification(User user, String email, String fullName, Map<String, Object> response){
        String codeVerification = generateRandomSixDigits();
        if(user != null) {
            user.setTokenPassword(codeVerification);
            userRepository.save(user);
        }
        String subject = "Verification code";
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("code", codeVerification);
        templateModel.put("fullName", fullName);
        templateModel.put("year", dateUtil.getYear(new Date()));
        String token = generateSecretKey();
        response.put("security_token", token);
        this.dataStore.put(token, Map.of(ACTION ,VERIFICATION_CODE,
                REFERENCE_USER, user != null? user.getReference(): "",
                "type", EMAIL,
                CREATED_AT, Instant.now().toString()));
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute(USER_REFERENCE, user != null ? user.getReference() : null);
        request.getSession().setAttribute(ACTION, VERIFICATION_CODE);
        request.getSession().setAttribute(VALIDATION_CODE, codeVerification);
        request.getSession().setAttribute("type", EMAIL);
        if(variableProperty.getSendEmailLive().equals("true")){
            try {
                emailService.sendEmail(templateModel,
                        TemplateConstants.EMAIL_SIGN_UP_CODE_VALIDATION,
                        user!=null?user.getEmail():email,
                        subject
                );
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExceptionControllerAdvice.SendEmailException("Error sending email!");
            }
        }else{
            response.put(VERIFICATION_CODE, codeVerification);
        }
        response.put(MESSAGE , "The code has been sent");
    }

    @Override
    public User validateInscription(String validationCode, String securityToken) {
        User user;
        String type = null;
        if(securityToken == null) {
            user = (User) getUserBySession("VERIFICATION_CODE", validationCode);//Check with sessions
        }else {
            user = getUserByToken(securityToken, "VERIFICATION_CODE");
            if(Objects.equals(user.getTokenPassword(), validationCode)) {
                Map<String, String> dataUser = dataStore.get(securityToken);
                if(dataUser != null)
                    type = dataUser.get("type");
                this.dataStore.remove(securityToken);
            }else {
                throw new ExceptionControllerAdvice.GeneralException(Map.of("code_error", true, MESSAGE, "Invalid code"));
            }
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().removeAttribute(USER_REFERENCE);
        request.getSession().removeAttribute(ACTION );
        request.getSession().removeAttribute(VALIDATION_CODE);
        if(type == null) {
            type = (String) request.getSession().getAttribute("type");
        }
        user.setTokenPassword(null);
        if(type != null) {
            if ("gsm".equals(type)) {
                user.setGsmVerified(true);
            } else if (EMAIL.equals(type)) {
                user.setEmailVerified(true);
            }
        }
        userRepository.save(user);
        return user;
    }
    public Object getUserBySession(String action, String validationCode){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String actionSession = (String) request.getSession().getAttribute(ACTION );
        if(!Objects.equals(actionSession, action)) {
            throw new ExceptionControllerAdvice.ObjectNotFoundException(INVALID_OR_EXPIRED_TOKEN);
        }
        String referenceUser = (String) request.getSession().getAttribute(USER_REFERENCE);
        String validationCodeSession = (String) request.getSession().getAttribute(VALIDATION_CODE);
        User user;
        if(referenceUser == null) {
            throw new ExceptionControllerAdvice.ObjectNotFoundException(INVALID_OR_EXPIRED_TOKEN);
        }
        else{
            user = userRepository.findByReference(referenceUser).orElseThrow(
                    ()->new ExceptionControllerAdvice.ObjectNotFoundException("User not found")
            );
        }
        //todo check by db or by sessions
        if(!Objects.equals(validationCodeSession, validationCode)) {
            throw new ExceptionControllerAdvice.GeneralException(Map.of("code_error", true, MESSAGE, "Invalid code"));
        }
        return user;
    }
    public User getUserByToken(String token, String action){
        String reference;
        Map<String, String> dataUser = dataStore.get(token);
        if(dataUser == null){
            throw new ExceptionControllerAdvice.ObjectNotFoundException(INVALID_OR_EXPIRED_TOKEN);
        }
        Instant createdDate = Instant.parse(dataUser.get(CREATED_AT));
        Instant currentInstant = Instant.now();
        long minutesDifference = ChronoUnit.MINUTES.between(createdDate, currentInstant);
        if (minutesDifference >= variableProperty.getSessionExpirationTime()) {
            throw new ExceptionControllerAdvice.ObjectNotFoundException(INVALID_OR_EXPIRED_TOKEN);
        }
        reference = dataUser.get(REFERENCE_USER);
        if(reference == null){
            this.dataStore.remove(token);
            throw new ExceptionControllerAdvice.ObjectNotFoundException("Invalid or expired token");
        }
        if(!Objects.equals(action, dataUser.get(ACTION))) {
            throw new ExceptionControllerAdvice.ObjectNotFoundException("Invalid or expired token");
        }
        return userRepository.findByReference(reference).orElseThrow(
                ()->new ExceptionControllerAdvice.ObjectNotFoundException("User not found")
        );
    }
}