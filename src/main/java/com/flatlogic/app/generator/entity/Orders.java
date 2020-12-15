
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
@Table(name = "orders")
public class Orders extends DomainObject<UUID> {

    @Column(name = "order_date")
    private Date order_date;
    
    @OneToOne
    @JoinColumn(name = "`productId`")
    private Products product;
    
    @OneToOne
    @JoinColumn(name = "`userId`")
    private Users user;
    
    @Column(name = "amount")
    private Integer amount;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "`importHash`", unique = true)
    private String importHash;
}
