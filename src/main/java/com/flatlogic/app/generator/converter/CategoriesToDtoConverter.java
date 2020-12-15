
package com.flatlogic.app.generator.converter;


import com.flatlogic.app.generator.dto.ProductsDto;

import com.flatlogic.app.generator.dto.CategoriesDto;

import com.flatlogic.app.generator.dto.OrdersDto;

import com.flatlogic.app.generator.dto.UsersDto;

import com.flatlogic.app.generator.dto.FileDto;
import com.flatlogic.app.generator.entity.CategoriesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoriesToDtoConverter implements Converter<Categories, CategoriesDto> {


    @Autowired
    private ProductsToDtoConverter productsToDtoConverter;

    @Autowired
    private OrdersToDtoConverter ordersToDtoConverter;

    @Autowired
    private UsersToDtoConverter usersToDtoConverter;


    @Autowired
    private FileToDtoConverter fileToDtoConverter;

    @Override
    public CategoriesDto convert(final Categories source) {
        final CategoriesDto categoriesDto = new CategoriesDto();

        categoriesDto.setId(source.getId());
        categoriesDto.setImportHash(source.getImportHash());


        categoriesDto.setTitle(source.getTitle());
    

        categoriesDto.setCreatedAt(source.getCreatedAt());
        categoriesDto.setUpdatedAt(source.getUpdatedAt());
        categoriesDto.setDeletedAt(source.getDeletedAt());
        Optional.ofNullable(source.getCreatedBy()).ifPresent(
                user -> categoriesDto.setCreatedById(user.getId()));
        Optional.ofNullable(source.getUpdatedBy()).ifPresent(
                user -> categoriesDto.setUpdatedById(user.getId()));
        return categoriesDto;
    }

    private CategoriesDto convertCategoriesToDto (final Categories categories) {
        final CategoriesDto categoriesDto = new CategoriesDto();
        categoriesDto.setId(categories.getId());


        categoriesDto.setTitle(categories.getTitle());
    

        categoriesDto.setImportHash(categories.getImportHash());
        categoriesDto.setCreatedAt(categories.getCreatedAt());
        categoriesDto.setUpdatedAt(categories.getUpdatedAt());
        categoriesDto.setDeletedAt(categories.getDeletedAt());
        Optional.ofNullable(categories.getCreatedBy()).ifPresent(
                user -> categoriesDto.setCreatedById(user.getId()));
        Optional.ofNullable(categories.getUpdatedBy()).ifPresent(
                user -> categoriesDto.setUpdatedById(user.getId()));
        return categoriesDto;
	}
}
