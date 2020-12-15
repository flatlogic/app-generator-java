
package com.flatlogic.app.generator.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Categories extends DomainObject<UUID> {

    @Column(name = "title", columnDefinition = "text")
    private String title;
    
    @Column(name = "`importHash`", unique = true)
    private String importHash;
}
