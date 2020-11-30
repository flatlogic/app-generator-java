package com.flatlogic.app.generator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FileDto extends AbstractDto {

    private String belongsTo;

    private UUID belongsToId;

    private String belongsToColumn;

    private String name;

    private Integer sizeInBytes;

    private String privateUrl;

    private String publicUrl;

}
