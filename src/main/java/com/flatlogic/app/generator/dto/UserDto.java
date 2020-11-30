package com.flatlogic.app.generator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserDto extends AbstractDto {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private String role;

    private Boolean disabled;

    private String password;

    private Boolean emailVerified;

    private String emailVerificationToken;

    private Date emailVerificationTokenExpiresAt;

    private String passwordResetToken;

    private Date passwordResetTokenExpiresAt;

    private String provider;

    private String importHash;

    @JsonProperty("wishlist")
    private List<ProductDto> productDtos = new ArrayList<>();

    @JsonProperty("avatar")
    private List<FileDto> fileDtos = new ArrayList<>();

}
