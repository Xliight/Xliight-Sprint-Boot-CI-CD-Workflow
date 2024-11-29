package com.ouitrips.app.services.security.impl;

import com.ouitrips.app.constants.TemplateConstants;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.services.messagingsystemservice.email.EmailService;
import com.ouitrips.app.services.messagingsystemservice.sms.SmsService;
import com.ouitrips.app.services.security.ResetPasswordService;
import com.ouitrips.app.utils.DateUtil;
import com.ouitrips.app.utils.RandomStringUtils;
import com.ouitrips.app.utils.VariableProperty;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.repositories.security.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final SmsService smsService;
    private final DateUtil dateUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final VariableProperty variableProperty;
    private final Map<String, Map<String, String>> dataStore = new ConcurrentHashMap<>();

    @Override
    public Object forgotPassword(String contact, String type) {
        if(type.equals("email")){
            if (!userRepository.existsByEmail(contact)) {
                throw new ExceptionControllerAdvice.UserNotFoundException("Email not found");
            } else {
                User user = userRepository.findByEmail(contact);
                String codeVerification = RandomStringUtils.generateRandomSixDigits();
                user.setTokenPassword(codeVerification);
                userRepository.save(user);
                return this.sendResetPasswordEmail(user, codeVerification);
            }
        }else if (type.equals("gsm")){
            User user = userRepository.findUserByMobilePhone(contact).orElse(null);
            if(user == null){
                user = userRepository.findUserByGsm(contact).orElse(null);
            }
            if(user == null){
                List<User> users = userRepository.findUserByGsmPrefixe(contact);
                if(users.isEmpty()){
                    throw new  ExceptionControllerAdvice.UserNotFoundException("User not found");
                }
                user = users.get(0);
            }
            String codeVerification = RandomStringUtils.generateRandomSixDigits();
            user.setTokenPassword(codeVerification);
            userRepository.save(user);
            return this.sendResetPasswordGSM(user, codeVerification);
        }else{
            throw new ExceptionControllerAdvice.GeneralException("Contact Type not correct, type should be : 'email' or 'gsm'");
        }
    }
    @Override
    public void verificationCode(String verificationCode, String token) {
        User user;
        if(token==null){
            user = getUserBySession("VERIFICATION_CODE");
        }else {
            user = getUserByToken(token, "VERIFICATION_CODE");
        }
        if(Objects.equals(user.getTokenPassword(), verificationCode)){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            user.setTokenPassword(null);
            userRepository.save(user);
            if(token !=null){
                Map<String, String> dataUser = dataStore.get(token);
                Map<String, String> newDataUser = new HashMap<>();
                newDataUser.put("action", "RESET_PASSWORD");
                newDataUser.put("created_at", dataUser.get("created_at"));
                newDataUser.put("reference_user", dataUser.get("reference_user"));
                this.dataStore.put(token,newDataUser);
            }
            request.getSession().setAttribute("action", "RESET_PASSWORD");
        }else{
            throw new ExceptionControllerAdvice.ObjectNotFoundException("Verification code incorrect!");
        }
    }
    @Override
    public void resetPassword(String newPassword, String token) {
        User user;
        if(token==null){
            user = getUserBySession("RESET_PASSWORD");
        }else {
            user = getUserByToken(token, "RESET_PASSWORD");
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
        String subject = "Changement de mot de passe confirmer";
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("fullName", getFullName(user.getFirstName(), user.getLastName()));
        templateModel.put("year", dateUtil.getYear(new Date()));
        try {//todo
            emailService.sendEmail(templateModel,
                    TemplateConstants.EMAIL_CONFIRM_RESET_PASSWORD,
                    user.getEmail(),
                    subject
            );
        } catch (Exception e) {
            throw new ExceptionControllerAdvice.SendEmailException("Error sending email");//todo remove this: If the email not sent no need to bother the front end with this
        }
        //Destroy token and session
        if(token != null){
            this.dataStore.remove(token);
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().invalidate();
    }

    //Get user by session
    public User getUserBySession(String action){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String reference = (String) request.getSession().getAttribute("user_reference");
        String actionSession = (String) request.getSession().getAttribute("action");

        if(reference == null) {
            throw new ExceptionControllerAdvice.ObjectNotFoundException("Invalid or expired token");
        }
        if(!Objects.equals(actionSession, action)) {
            throw new ExceptionControllerAdvice.ObjectNotFoundException("Invalid or expired token");
        }
        return userRepository.findByReference(reference).orElseThrow(
                ()->new ExceptionControllerAdvice.ObjectNotFoundException("User not found")
        );
    }
    public User getUserByToken(String token, String action){
        String reference;
        Map<String, String> dataUser = dataStore.get(token);
        if(dataUser == null){
            throw new ExceptionControllerAdvice.ObjectNotFoundException("Invalid or expired token");
        }
        Instant createdDate = Instant.parse(dataUser.get("created_at"));
        Instant currentInstant = Instant.now();
        long minutesDifference = ChronoUnit.MINUTES.between(createdDate, currentInstant);
        if (minutesDifference >= variableProperty.getSessionExpirationTime()) {
            throw new ExceptionControllerAdvice.ObjectNotFoundException("Invalid or expired token");
        }
        reference = dataUser.get("reference_user");
        if(reference == null){
            this.dataStore.remove(token);
            throw new ExceptionControllerAdvice.ObjectNotFoundException("Invalid or expired token");
        }
        if(!Objects.equals(action, dataUser.get("action"))) {
            throw new ExceptionControllerAdvice.ObjectNotFoundException("Invalid or expired token");
        }
        return userRepository.findByReference(reference).orElseThrow(
                ()->new ExceptionControllerAdvice.ObjectNotFoundException("User not found")
        );
    }
    public String getFullName(String firstName, String lastName){
        return (firstName != null ? firstName + " " : "") + (lastName != null ? lastName : "");
    }
    //Send Email
    public Object sendResetPasswordEmail(User user, String codeVerification) {
        Map<String, Object> response = new HashMap<>();
        String subject = "Reset your password";
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("code", codeVerification);
        templateModel.put("fullName", getFullName(user.getFirstName(), user.getLastName()));
        templateModel.put("year", dateUtil.getYear(new Date()));
        String token = generateSecretKey();
        response.put("security_token", token);
        this.dataStore.put(token, Map.of("action","VERIFICATION_CODE",
                "reference_user",user.getReference(),
                "created_at", Instant.now().toString()));
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute("user_reference", user.getReference());
        request.getSession().setAttribute("action", "VERIFICATION_CODE");
        if(variableProperty.getSendEmailLive().equals("true")){
            try {
                emailService.sendEmail(templateModel,
                        TemplateConstants.EMAIL_RESET_PASSWORD_CODE_VALIDATION,
                        user.getEmail(),
                        subject
                );
            } catch (Exception e) {
                throw new ExceptionControllerAdvice.SendEmailException("Error sending email");
            }
        }else{
            response.put("verification_code", codeVerification);
        }
        response.put("message", "The code has been sent");
        return response;
    }
    //Send SMS
    public Object sendResetPasswordGSM(User user, String codeVerification) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("name", user.getFirstName()+" "+user.getLastName());
        templateModel.put("code", codeVerification);
        String token = generateSecretKey();
        response.put("security_token", token);
        this.dataStore.put(token, Map.of("action","VERIFICATION_CODE",
                "reference_user",user.getReference(),
                "created_at", Instant.now().toString()));
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute("user_reference", user.getReference());
        request.getSession().setAttribute("action", "VERIFICATION_CODE");
        if(variableProperty.getSendSmsLive().equals("true")){
            try{//todo
                smsService.sendSms("OuiTrips",
                        user.getMobilePhone(),
                        null,
                        templateModel,
                        TemplateConstants.SMS_RESET_PASSWORD_CODE_VALIDATION,
                        null);
            }catch(Exception e){
                throw new ExceptionControllerAdvice.SendSmsException("Error sending sms");
            }
        }else{
            response.put("verification_code", codeVerification);
        }
        response.put("message", "The code has been sent");
        return response;
    }

    public String generateSecretKey(){
        String secretKey;
        do {
            secretKey = RandomStringUtils.generateRandomStringSecurity();
        }while (this.dataStore.get(secretKey)!=null);
        return secretKey;
    }
}