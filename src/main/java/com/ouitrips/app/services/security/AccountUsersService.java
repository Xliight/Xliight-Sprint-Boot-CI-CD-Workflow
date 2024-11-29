package com.ouitrips.app.services.security;

import com.ouitrips.app.entities.security.User;

import java.util.List;
import java.util.Map;

public interface AccountUsersService {
    User saveUser(Map<String, Object> userAccountRequest, String oper);
    User addUser(Map<String, Object> userAccountRequest);
    void updateUser(Map<String, Object> userAccountRequest);
    void deleteUser(String ref);
    Object getUser(String reference);
    List<Object> getAllUsers();
}
