package com.flatlogic.app.generator.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "files")
public class File extends DomainObject<UUID> {

    @Column(name = "`belongsTo`")
    private String belongsTo;

    @Column(name = "`belongsToId`")
    private UUID belongsToId;

    @Column(name = "`belongsToColumn`")
    private String belongsToColumn;

    @NotEmpty
    @Column(name = "name", nullable = false, length = 2083)
    private String name;

    @Column(name = "`sizeInBytes`")
    private Integer sizeInBytes;

    @Column(name = "`privateUrl`", length = 2083)
    private String privateUrl;

    @NotEmpty
    @Column(name = "`publicUrl`", nullable = false, length = 2083)
    private String publicUrl;

}
