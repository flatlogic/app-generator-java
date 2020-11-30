package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.entity.User;
import com.flatlogic.app.generator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserDetailsService service.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    //private ConcurrentMap<String, UserDetails> concurrentMap = new ConcurrentHashMap<>();

    /**
     * UserRepository instance.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Load user by username.
     *
     * @param username User name
     * @return UserDetails
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name()).build();
    }

}
