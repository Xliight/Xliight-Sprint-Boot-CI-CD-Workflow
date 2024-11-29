package com.ouitrips.app.config.services;

import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.repositories.security.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ExceptionControllerAdvice.UserNotFoundException("User not found");
        }
        return UserDetailsImpl.build(user);
    }
}