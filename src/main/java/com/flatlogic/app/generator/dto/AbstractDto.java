package com.flatlogic.app.generator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public abstract class AbstractDto {

    private UUID id;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private UUID createdById;

    private UUID updatedById;

}
