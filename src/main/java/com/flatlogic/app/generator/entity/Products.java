
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
@Table(name = "products")
public class Products extends DomainObject<UUID> {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "`belongsToId`", foreignKey = @ForeignKey(name = "none"))
    @Where(clause = "`deletedAt` is null")
    private List<File> image = new ArrayList<>();
    
    @Column(name = "title", columnDefinition = "text")
    private String title;
    
    @Column(name = "price")
    private BigDecimal price;
    
    @Column(name = "discount")
    private BigDecimal discount;
    
    @Column(name = "description", columnDefinition = "text")
    private String description;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "`productsCategoriesCategories`",
            joinColumns = {@JoinColumn(name = "`productsId`")},
            inverseJoinColumns = {@JoinColumn(name = "`categoriesId`")}
    )
    @Where(clause = "`deletedAt` is null")
    private List<Categories> categories = new ArrayList<>();
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "`productsMore_productsProducts`",
            joinColumns = {@JoinColumn(name = "`productsId`")},
            inverseJoinColumns = {@JoinColumn(name = "`more_productsId`")}
    )
    @Where(clause = "`deletedAt` is null")
    private List<Products> more_products = new ArrayList<>();
    
    @Column(name = "rating")
    private Integer rating;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "`importHash`", unique = true)
    private String importHash;
}
