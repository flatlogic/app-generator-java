package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.request.AuthRequest;
import com.flatlogic.app.generator.controller.request.UpdatePasswordRequest;
import com.flatlogic.app.generator.dto.UsersDto;
import com.flatlogic.app.generator.entity.Users;
import com.flatlogic.app.generator.exception.InvalidLoginException;
import com.flatlogic.app.generator.jwt.JwtUtil;
import com.flatlogic.app.generator.service.UsersService;
import com.flatlogic.app.generator.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    private UsersService usersService;

    /**
     * DefaultConversionService instance.
     */
    @Autowired
    private DefaultConversionService defaultConversionService;

    /**
     * JwtUtil instance.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Get current user.
     *
     * @param userDetails UserDto
     * @return UserDetails
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Get current user.");
        Users users = usersService.getUserByEmail(userDetails.getUsername());
        UsersDto usersDto = defaultConversionService.convert(users, UsersDto.class);
        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }

    /**
     * Login method.
     *
     * @param authRequest AuthRequest
     * @return JWT token
     */
    @PostMapping("signin/local")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        LOGGER.info("Login method.");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(), authRequest.getPassword()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new InvalidLoginException(Constants.INVALID_EMAIL_PASSWORD);
        }
        return new ResponseEntity<>(jwtUtil.generateToken(authRequest.getEmail()), HttpStatus.OK);
    }

    @PutMapping("/password-reset")
    public void resetPassword() {
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
        usersService.updateUserPassword(userDetails.getUsername(), passwordRequest.getCurrentPassword(),
                passwordRequest.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * InvalidLoginException handler.
     *
     * @param e InvalidLoginException
     */
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = Constants.INVALID_EMAIL_PASSWORD)
    @ExceptionHandler(InvalidLoginException.class)
    public void handleInvalidLoginException(InvalidLoginException e) {
        LOGGER.error("InvalidLoginException handler.", e);
    }

}
