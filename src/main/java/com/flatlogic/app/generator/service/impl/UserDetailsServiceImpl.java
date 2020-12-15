package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.entity.Users;
import com.flatlogic.app.generator.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserDetailsService service.
 */
@Service
public class UsersDetailsServiceImpl implements UsersDetailsService {

    //private ConcurrentMap<String, UserDetails> concurrentMap = new ConcurrentHashMap<>();

    /**
     * UserRepository instance.
     */
    @Autowired
    private UsersRepository usersRepository;

    /**
     * Load user by username.
     *
     * @param username User name
     * @return UserDetails
     */
    @Override
    @Transactional(readOnly = true)
    public UsersDetails loadUsersByUsername(String username) {
        User user = usersRepository.findByEmail(username);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name()).build();
    }

}
