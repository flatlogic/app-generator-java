package com.flatlogic.app.generator.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FileRequest {

    private UUID id;

    private String name;

    @JsonProperty("new")
    private boolean isNew;

    private String privateUrl;

    private String publicUrl;

    private Integer sizeInBytes;

}
