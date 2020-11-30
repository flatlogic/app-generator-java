package com.flatlogic.app.generator.converter;

import com.flatlogic.app.generator.dto.FileDto;
import com.flatlogic.app.generator.entity.File;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FileToDtoConverter implements Converter<File, FileDto> {

    @Override
    public FileDto convert(final File source) {
        final FileDto fileDto = new FileDto();
        fileDto.setId(source.getId());
        fileDto.setBelongsTo(source.getBelongsTo());
        fileDto.setBelongsToId(source.getBelongsToId());
        fileDto.setBelongsToColumn(source.getBelongsToColumn());
        fileDto.setName(source.getName());
        fileDto.setSizeInBytes(source.getSizeInBytes());
        fileDto.setPrivateUrl(source.getPrivateUrl());
        fileDto.setPublicUrl(source.getPublicUrl());
        fileDto.setCreatedAt(source.getCreatedAt());
        fileDto.setUpdatedAt(source.getUpdatedAt());
        fileDto.setDeletedAt(source.getDeletedAt());
        Optional.ofNullable(source.getCreatedBy()).ifPresent(
                user -> fileDto.setCreatedById(user.getId()));
        Optional.ofNullable(source.getUpdatedBy()).ifPresent(
                user -> fileDto.setUpdatedById(user.getId()));
        return fileDto;
    }

}
