package com.ouitrips.app.services.security.impl;

import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.mapper.security.UserAccountMapper;
import com.ouitrips.app.repositories.security.UserRepository;
import com.ouitrips.app.services.security.AccountUsersService;
import com.ouitrips.app.utils.DateUtil;
import com.ouitrips.app.utils.UserUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class AccountUsersImpl implements AccountUsersService {
    //Repositories
    private  final UserRepository userRepository;
    //Mappers
    private final UserAccountMapper userAccountMapper;
    //Properties & Utils
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DateUtil dateUtil;
    private final UserUtils userUtils;
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String USER_NOT_FOUND = "user not found";
    public static final String BIRTH_DATE = "birthDate";
    public static final String PASSWORD = "password";
    @Override
    @Transactional
    public User saveUser(Map<String, Object> userAccountRequest, String oper) {
        if(Objects.equals(oper, "update")){
            return updatingUser(userAccountRequest);
        } else if (Objects.equals(oper, "add")) {
            return savingUser(userAccountRequest);
        }
        return null;
    }

    @Override
    public User addUser(Map<String, Object> userAccountRequest) {
        return savingUser(userAccountRequest);
    }

    public User savingUser(Map<String, Object> userAccountRequest){
        boolean usernameExists = userRepository.existsByUsername((String) userAccountRequest.get(USERNAME));
        boolean emailExists = userRepository.existsByEmail((String) userAccountRequest.get(EMAIL));

        if (usernameExists) {
            throw new ExceptionControllerAdvice.GeneralException(Map.of("username_error", true));
        }
        if (emailExists) {
            throw new ExceptionControllerAdvice.GeneralException(Map.of("email_error", true));
        }
        User user = new User();
        user.setCreatedDate(Instant.now());
        user.setEnabled(true);
        user.setGsmVerified(false);
        user.setEmailVerified(false);
        user.setReference(UUID.randomUUID().toString());
        requestToUser(user, userAccountRequest);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(Map<String, Object>  userAccountRequest) {
        updatingUser(userAccountRequest);
    }

    public User updatingUser(Map<String, Object>  userAccountRequest) {
        User user = userRepository.findByReference((String) userAccountRequest.get("userReference")).orElseThrow(()->new ExceptionControllerAdvice.ObjectNotFoundException(USER_NOT_FOUND));
        String username = (String)userAccountRequest.get(USERNAME);
        String email = (String)userAccountRequest.get(EMAIL);
        boolean usernameExists = !username.equals(user.getUsername()) && userRepository.existsByUsername(username);
        boolean emailExists = !user.getEmail().equals(email) && userRepository.existsByEmail(email);

        if (usernameExists) {
            throw new ExceptionControllerAdvice.GeneralException("username duplicated");
        }
        if (emailExists) {
            throw new ExceptionControllerAdvice.GeneralException("email duplicated");
        }
        requestToUser(user, userAccountRequest);
        return userRepository.save(user);
    }
    @Transactional
    @Override
    public void deleteUser(String ref) {
        User user = userRepository.findByReference(ref).orElseThrow(()->new ExceptionControllerAdvice.ObjectNotFoundException(USER_NOT_FOUND));
        userRepository.delete(user);
    }
   @Transactional
    @Override
    public Object getUser(String reference) {
       User user = userRepository.findByReference(reference).orElseThrow(()->new ExceptionControllerAdvice.ObjectNotFoundException(USER_NOT_FOUND));
       return userAccountMapper.apply(user);
    }
   @Transactional
    @Override
    public List<Object> getAllUsers() {
       List<User> users = userRepository.findAll();
       return users.stream().map(userAccountMapper).toList();
    }
    public void requestToUser(User user, Map<String,Object> userAccountRequest) {
        if(!UserUtils.isRoleValid((String) userAccountRequest.get("roles"))){
            throw new ExceptionControllerAdvice.GeneralException("role not correct");
        }
        String rolesJson = "[\""+ userAccountRequest.get("roles") +"\"]";
        user.setRoles(rolesJson);
        user.setRegion((String) userAccountRequest.get("region"));
        user.setLastName((String) userAccountRequest.get("lastName"));
        user.setFirstName((String) userAccountRequest.get("firstName"));
        user.setCity((String) userAccountRequest.get("city"));
        user.setAddress((String) userAccountRequest.get("address"));
        user.setCivility((String)userAccountRequest.get("civility"));
        user.setCountry((String) userAccountRequest.get("country"));
        user.setBirthDate(
                userAccountRequest.get(BIRTH_DATE)==null? (LocalDate) userAccountRequest.get(BIRTH_DATE) :
                        dateUtil.parseLocalDate((String) userAccountRequest.get(BIRTH_DATE), "yyyy-MM-dd")
        );
        user.setAdditionalAddress((String) userAccountRequest.get("additionalAddress"));
        user.setEmail((String) userAccountRequest.get(EMAIL));
        user.setUsername((String) userAccountRequest.get(USERNAME));
        user.setBirthPlace((String) userAccountRequest.get("birthPlace"));
        user.setCompanyName((String) userAccountRequest.get("companyName"));
        if(userAccountRequest.get(PASSWORD)!=null && userAccountRequest.get(PASSWORD) != ""){
            user.setPassword(bCryptPasswordEncoder.encode(userAccountRequest.get(PASSWORD).toString()));
        }
        user.setGsm((String) userAccountRequest.get("gsm"));
        user.setMobilePhone((String) userAccountRequest.get("mobilePhone"));
        user.setProfession((String) userAccountRequest.get("profession"));
        user.setIso2((String) userAccountRequest.get("iso2"));
        user.setPrefixe((String) userAccountRequest.get("prefixe"));
        user.setPicture((String) userAccountRequest.get("picture"));
        user.setPhone((String) userAccountRequest.get("phone") );
        user.setZipCode((String) userAccountRequest.get("zipCode"));
    }




}
