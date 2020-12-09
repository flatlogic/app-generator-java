package com.flatlogic.app.generator.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ResetPasswordRequest {

    @NotBlank
    private String password;

    @NotBlank
    private String token;

}
