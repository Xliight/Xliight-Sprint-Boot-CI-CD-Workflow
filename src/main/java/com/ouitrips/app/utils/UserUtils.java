package com.ouitrips.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouitrips.app.config.JwtService;
import com.ouitrips.app.config.services.UserDetailsImpl;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.repositories.security.UserRepository;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@AllArgsConstructor
public class UserUtils {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    public static boolean isRoleValid(String role) {
        try {
            Role.valueOf(role);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public User getUserAuthenticated(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String userName = "";
        User user = new User();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if(jwtService.isTokenValid(token)){
                userName = jwtService.extractSubjectFromToken(token);
                user = userRepository.findByUsername(userName);
            }
        }else{
            String token = jwtService.parseJwt(request);
            if (token != null && !token.isEmpty()) {
                if(jwtService.isTokenValid(token)){
                    userName = jwtService.extractSubjectFromToken(token);
                    user = userRepository.findByUsername(userName);
                }
            }
        }
        return user;
    }
    public User userAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            return userRepository.findByUsername(userDetails.getUsername());
        }
        throw new ExceptionControllerAdvice.UserNotFoundException("User not found");
    }
    public Boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            User user = userRepository.findByUsername(userDetails.getUsername());
            return user != null;
        }
        return false;
    }
    public Boolean hasRole(Role role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetailsImpl userDetails) {
            Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
            List<String> authorityStrings = roles.stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(authority -> Arrays.asList(authority.split(", ")))
                    .flatMap(List::stream)
                    .toList();
            return authorityStrings.contains(role.name());
        }
        throw new ExceptionControllerAdvice.UserNotFoundException("User not authenticated");
    }
    public Boolean hasRole(User user, Role role){
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> roleList = new ArrayList<>();
        try {
            roleList = objectMapper.readValue((String) user.getRoles(), new TypeReference<List<String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return roleList.contains(role.name());
    }
    public void checkRoles(List<String> permissionRoles) {
        for (String role : permissionRoles) {
            switch (role) {
                case "ROLE_PRATICIEN", "ROLE_PATIENT", "ROLE_SECRETARY", "ROLE_SECRETARYSHIP", "ROLE_ADMIN", "ROLE_GERANT" -> {
                }
                default ->
                        throw new ExceptionControllerAdvice.GeneralException(Map.of("error_role", true, "role", role, "message", "Role not found"));
            }
        }

    }
    public String generateUsername(String byCreteria){
        boolean b = true;
        int i = 0;
        String newUsername = byCreteria;
        do {
            if(!this.userRepository.existsByUsername(newUsername)){
                b = false;
                return newUsername;
            }
            i++;
            newUsername = byCreteria+i;
        }while (b);
        return newUsername;
    }
    public void addRoleToUser(User user, String role){
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> roleList = new ArrayList<>();
        try {
            roleList = objectMapper.readValue((String) user.getRoles(), new TypeReference<List<String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        roleList.add(role);
        try {
            String rolesJson = objectMapper.writeValueAsString(roleList);
            user.setRoles(rolesJson);
            user = userRepository.save(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

