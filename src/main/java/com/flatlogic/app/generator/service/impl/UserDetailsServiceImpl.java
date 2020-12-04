package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.exception.UsernameNotFoundException;
import com.flatlogic.app.generator.repository.UserRepository;
import com.flatlogic.app.generator.util.Constants;
import com.flatlogic.app.generator.util.MessageCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * UserDetailsService service.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * UserRepository instance.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * UserCache instance.
     */
    @Autowired
    private UserCache userCache;

    /**
     * MessageCodeUtil instance.
     */
    @Autowired
    private MessageCodeUtil messageCodeUtil;

    /**
     * Load user by username.
     *
     * @param username User name
     * @return UserDetails
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        UserDetails userDetails = userCache.getUserFromCache(username);
        if (userDetails == null) {
            userDetails = Optional.ofNullable(userRepository.findByEmail(username)).map(user ->
                    User.builder()
                            .username(user.getEmail())
                            .password(user.getPassword())
                            .roles(user.getRole().name()).build())
                    .orElseThrow(() -> new UsernameNotFoundException(messageCodeUtil
                            .getFullErrorMessageByBundleCode(Constants.ERROR_MSG_AUTH_INVALID_CREDENTIALS)));
            userCache.putUserInCache(userDetails);
        }
        return userDetails;
    }

}
