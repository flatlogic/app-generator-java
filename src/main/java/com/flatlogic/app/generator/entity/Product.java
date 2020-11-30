package com.flatlogic.app.generator.entity;

import com.flatlogic.app.generator.type.ProductStatusType;
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
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends DomainObject<UUID> {

    @Column(name = "title", columnDefinition = "text")
    private String title;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "rating")
    private Integer rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatusType status;

    @Column(name = "`importHash`", unique = true)
    private String importHash;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "`productsCategoriesCategories`",
            joinColumns = {@JoinColumn(name = "`productId`")},
            inverseJoinColumns = {@JoinColumn(name = "`categoryId`")}
    )
    @Where(clause = "`deletedAt` is null")
    private List<Category> categories = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "`productsMore_productsProducts`",
            joinColumns = {@JoinColumn(name = "`productId`")},
            inverseJoinColumns = {@JoinColumn(name = "`moreProductId`")}
    )
    @Where(clause = "`deletedAt` is null")
    private List<Product> products = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "`belongsToId`", foreignKey = @ForeignKey(name = "none"))
    @Where(clause = "`deletedAt` is null")
    private List<File> files = new ArrayList<>();

}
