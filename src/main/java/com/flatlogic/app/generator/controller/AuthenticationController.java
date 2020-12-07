package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.request.AuthRequest;
import com.flatlogic.app.generator.controller.request.UpdatePasswordRequest;
import com.flatlogic.app.generator.dto.UserDto;
import com.flatlogic.app.generator.entity.User;
import com.flatlogic.app.generator.exception.UsernameNotFoundException;
import com.flatlogic.app.generator.jwt.JwtTokenUtil;
import com.flatlogic.app.generator.service.UserService;
import com.flatlogic.app.generator.util.Constants;
import com.flatlogic.app.generator.util.MessageCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.NoSuchElementException;

/**
 * AuthenticationController REST controller.
 */
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    /**
     * Logger constant.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    /**
     * AuthenticationManager instance.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * UserService instance.
     */
    @Autowired
    private UserService userService;

    /**
     * DefaultConversionService instance.
     */
    @Autowired
    private DefaultConversionService defaultConversionService;

    /**
     * JwtUtil instance.
     */
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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
     * Get current user.
     *
     * @param userDetails UserDto
     * @return UserDetails
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Get current user.");
        User user = userService.getUserByEmail(userDetails.getUsername());
        UserDto userDto = defaultConversionService.convert(user, UserDto.class);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Local login method.
     *
     * @param authRequest AuthRequest
     * @return JWT token
     */
    @PostMapping("signin/local")
    public ResponseEntity<String> localLogin(@Valid @RequestBody AuthRequest authRequest) {
        LOGGER.info("Login method.");
        userCache.removeUserFromCache(authRequest.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()));
        return new ResponseEntity<>(jwtTokenUtil.generateToken(authRequest.getEmail()), HttpStatus.OK);
    }

    /**
     * Google sign in.
     *
     * @return RedirectView
     */
    @GetMapping("signin/google")
    public RedirectView signInGoogle() {
        return new RedirectView("/api/oauth2/authorization/google");
    }

    /**
     * Update user password.
     *
     * @param passwordRequest UpdatePasswordRequest
     * @param userDetails     UserDetails
     * @return Void
     */
    @PutMapping("/password-update")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordRequest passwordRequest,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Update user password.");
        userService.updateUserPassword(userDetails.getUsername(), passwordRequest.getCurrentPassword(),
                passwordRequest.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * BadCredentialsException handler.
     *
     * @param e BadCredentialsException
     * @return Error message
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleInvalidLoginException(BadCredentialsException e) {
        LOGGER.error("BadCredentialsException handler.", e);
        return new ResponseEntity<>(messageCodeUtil.getFullErrorMessageByBundleCode(
                Constants.ERROR_MSG_AUTH_INVALID_CREDENTIALS), HttpStatus.BAD_REQUEST);
    }

    /**
     * UsernameNotFoundException handler.
     *
     * @param e UsernameNotFoundException
     * @return Error message
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException e) {
        LOGGER.error("UsernameNotFoundException handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * NoSuchElementException handler.
     *
     * @param e NoSuchElementException
     * @return Error message
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        LOGGER.error("NoSuchElementException handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
