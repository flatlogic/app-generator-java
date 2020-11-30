package com.flatlogic.app.generator.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {

    private String currentPassword;

    private String newPassword;

}
