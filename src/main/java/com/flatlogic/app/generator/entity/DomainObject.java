package com.flatlogic.app.generator.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class DomainObject<I extends Serializable> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private I id;

    @CreationTimestamp
    @Column(name = "`createdAt`", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "`updatedAt`", insertable = false)
    private Date updatedAt;

    @Column(name = "`deletedAt`", insertable = false)
    private Date deletedAt;

    @OneToOne
    @JoinColumn(name = "`createdById`", updatable = false)
    private User createdBy;

    @OneToOne
    @JoinColumn(name = "`updatedById`", insertable = false)
    private User updatedBy;

}
