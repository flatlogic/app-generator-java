
package com.flatlogic.app.generator.converter;


import com.flatlogic.app.generator.dto.ProductsDto;

import com.flatlogic.app.generator.dto.CategoriesDto;

import com.flatlogic.app.generator.dto.OrdersDto;

import com.flatlogic.app.generator.dto.UsersDto;

import com.flatlogic.app.generator.dto.FileDto;
import com.flatlogic.app.generator.entity.UsersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsersToDtoConverter implements Converter<Users, UsersDto> {


    @Autowired
    private ProductsToDtoConverter productsToDtoConverter;

    @Autowired
    private CategoriesToDtoConverter categoriesToDtoConverter;

    @Autowired
    private OrdersToDtoConverter ordersToDtoConverter;


    @Autowired
    private FileToDtoConverter fileToDtoConverter;

    @Override
    public UsersDto convert(final Users source) {
        final UsersDto usersDto = new UsersDto();

        usersDto.setId(source.getId());
        usersDto.setImportHash(source.getImportHash());


        Optional.ofNullable(source.getWishlist()).ifPresent(products -> {
            final List<ProductsDto> products = users.getWishlist();
            products.forEach(products -> products.add(ProductsToDtoConverter.convert(products)));
        });
    
        usersDto.setFirstName(source.getFirstName());
    
        usersDto.setLastName(source.getLastName());
    
        usersDto.setPhoneNumber(source.getPhoneNumber());
    
        usersDto.setEmail(source.getEmail());
    
        Optional.ofNullable(source.getRole()).ifPresent(
                role -> usersDto.setRole(role));
    
        usersDto.setDisabled(source.getDisabled());
    
        Optional.ofNullable(source.getFiles()).ifPresent(files -> {
            final List<FileDto> fileDtos = usersDto.getFileDtos();
            files.forEach(__file -> fileDtos.add(fileToDtoConverter.convert(__file)));
        });
    
        usersDto.setPassword(source.getPassword());
    
        usersDto.setEmailVerified(source.getEmailVerified());
    
        usersDto.setEmailVerificationToken(source.getEmailVerificationToken());
    
        usersDto.setEmailVerificationTokenExpiresAt(source.getEmailVerificationTokenExpiresAt());
    
        usersDto.setPasswordResetToken(source.getPasswordResetToken());
    
        usersDto.setPasswordResetTokenExpiresAt(source.getPasswordResetTokenExpiresAt());
    
        usersDto.setProvider(source.getProvider());
    

        usersDto.setCreatedAt(source.getCreatedAt());
        usersDto.setUpdatedAt(source.getUpdatedAt());
        usersDto.setDeletedAt(source.getDeletedAt());
        Optional.ofNullable(source.getCreatedBy()).ifPresent(
                user -> usersDto.setCreatedById(user.getId()));
        Optional.ofNullable(source.getUpdatedBy()).ifPresent(
                user -> usersDto.setUpdatedById(user.getId()));
        return usersDto;
    }

    private UsersDto convertUsersToDto (final Users users) {
        final UsersDto usersDto = new UsersDto();
        usersDto.setId(users.getId());


        usersDto.setFirstName(users.getFirstName());
    
        usersDto.setLastName(users.getLastName());
    
        usersDto.setPhoneNumber(users.getPhoneNumber());
    
        usersDto.setEmail(users.getEmail());
    
        Optional.ofNullable(users.getRole()).ifPresent(
                role -> usersDto.setRole(role.getRoleValue()));
    
        usersDto.setDisabled(users.getDisabled());
    
        usersDto.setPassword(users.getPassword());
    
        usersDto.setEmailVerified(users.getEmailVerified());
    
        usersDto.setEmailVerificationToken(users.getEmailVerificationToken());
    
        usersDto.setEmailVerificationTokenExpiresAt(users.getEmailVerificationTokenExpiresAt());
    
        usersDto.setPasswordResetToken(users.getPasswordResetToken());
    
        usersDto.setPasswordResetTokenExpiresAt(users.getPasswordResetTokenExpiresAt());
    
        usersDto.setProvider(users.getProvider());
    

        usersDto.setImportHash(users.getImportHash());
        usersDto.setCreatedAt(users.getCreatedAt());
        usersDto.setUpdatedAt(users.getUpdatedAt());
        usersDto.setDeletedAt(users.getDeletedAt());
        Optional.ofNullable(users.getCreatedBy()).ifPresent(
                user -> usersDto.setCreatedById(user.getId()));
        Optional.ofNullable(users.getUpdatedBy()).ifPresent(
                user -> usersDto.setUpdatedById(user.getId()));
        return usersDto;
	}
}
