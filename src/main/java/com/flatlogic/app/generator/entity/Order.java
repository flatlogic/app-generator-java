package com.flatlogic.app.generator.entity;

import com.flatlogic.app.generator.type.OrderStatusType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends DomainObject<UUID> {

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "amount")
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatusType status;

    @Column(name = "`importHash`", unique = true)
    private String importHash;

    @OneToOne
    @JoinColumn(name = "`productId`")
    private Product product;

    @OneToOne
    @JoinColumn(name = "`userId`")
    private User user;

}
