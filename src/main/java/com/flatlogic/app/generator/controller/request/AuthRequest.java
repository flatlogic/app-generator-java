package com.flatlogic.app.generator.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AuthRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

}
