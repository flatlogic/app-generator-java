
package com.flatlogic.app.generator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UsersDto extends AbstractDto {


    private List<ProductsDto> wishlist = new ArrayList<>();
    
    private String firstName;
    
    private String lastName;
    
    private String phoneNumber;
    
    private String email;
    
    private String role;
    
    private boolean disabled;
    
    private List<FileDto> avatarDtos = new ArrayList<>();
    
    private String password;
    
    private boolean emailVerified;
    
    private String emailVerificationToken;
    
    private Date emailVerificationTokenExpiresAt;
    
    private String passwordResetToken;
    
    private Date passwordResetTokenExpiresAt;
    
    private String provider;
    

    private String importHash;
}
