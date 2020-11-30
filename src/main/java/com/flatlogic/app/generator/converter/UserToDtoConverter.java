package com.flatlogic.app.generator.converter;

import com.flatlogic.app.generator.dto.FileDto;
import com.flatlogic.app.generator.dto.ProductDto;
import com.flatlogic.app.generator.dto.UserDto;
import com.flatlogic.app.generator.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserToDtoConverter implements Converter<User, UserDto> {

    @Autowired
    private ProductToDtoConverter productToDtoConverter;

    @Autowired
    private FileToDtoConverter fileToDtoConverter;

    @Override
    public UserDto convert(final User source) {
        final UserDto userDto = new UserDto();
        userDto.setId(source.getId());
        userDto.setFirstName(source.getFirstName());
        userDto.setLastName(source.getLastName());
        userDto.setPhoneNumber(source.getPhoneNumber());
        userDto.setEmail(source.getEmail());
        Optional.ofNullable(source.getRole()).ifPresent(role -> userDto.setRole(role.getRoleValue()));
        userDto.setDisabled(source.getDisabled());
        userDto.setPassword(source.getPassword());
        userDto.setEmailVerified(source.getEmailVerified());
        userDto.setEmailVerificationToken(source.getEmailVerificationToken());
        userDto.setEmailVerificationTokenExpiresAt(source.getEmailVerificationTokenExpiresAt());
        userDto.setPasswordResetToken(source.getPasswordResetToken());
        userDto.setPasswordResetTokenExpiresAt(source.getPasswordResetTokenExpiresAt());
        userDto.setProvider(source.getProvider());
        userDto.setImportHash(source.getImportHash());
        Optional.ofNullable(source.getProducts()).ifPresent(products -> {
            final List<ProductDto> productDtos = userDto.getProductDtos();
            products.forEach(product -> productDtos.add(productToDtoConverter.convert(product)));
        });
        Optional.ofNullable(source.getFiles()).ifPresent(files -> {
            final List<FileDto> fileDtos = userDto.getFileDtos();
            files.forEach(file -> fileDtos.add(fileToDtoConverter.convert(file)));
        });
        userDto.setCreatedAt(source.getCreatedAt());
        userDto.setUpdatedAt(source.getUpdatedAt());
        userDto.setDeletedAt(source.getDeletedAt());
        Optional.ofNullable(source.getCreatedBy()).ifPresent(
                user -> userDto.setCreatedById(user.getId()));
        Optional.ofNullable(source.getUpdatedBy()).ifPresent(
                user -> userDto.setUpdatedById(user.getId()));
        return userDto;
    }

}
