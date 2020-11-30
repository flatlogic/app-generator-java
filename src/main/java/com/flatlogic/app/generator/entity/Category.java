package com.flatlogic.app.generator.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends DomainObject<UUID> {

    @Column(name = "title", columnDefinition = "text")
    private String title;

    @Column(name = "`importHash`", unique = true)
    private String importHash;

}
