package com.flatlogic.app.generator.converter;

import com.flatlogic.app.generator.dto.CategoryDto;
import com.flatlogic.app.generator.entity.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryToDtoConverter implements Converter<Category, CategoryDto> {

    @Override
    public CategoryDto convert(final Category source) {
        final CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(source.getId());
        categoryDto.setTitle(source.getTitle());
        categoryDto.setImportHash(source.getImportHash());
        categoryDto.setCreatedAt(source.getCreatedAt());
        categoryDto.setUpdatedAt(source.getUpdatedAt());
        categoryDto.setDeletedAt(source.getDeletedAt());
        Optional.ofNullable(source.getCreatedBy()).ifPresent(
                user -> categoryDto.setCreatedById(user.getId()));
        Optional.ofNullable(source.getUpdatedBy()).ifPresent(
                user -> categoryDto.setUpdatedById(user.getId()));
        return categoryDto;
    }

}

