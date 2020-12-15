
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
@Table(name = "users")
public class Users extends DomainObject<UUID> {

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "`usersWishlistProducts`",
            joinColumns = {@JoinColumn(name = "`usersId`")},
            inverseJoinColumns = {@JoinColumn(name = "`wishlistId`")}
    )
    @Where(clause = "`deletedAt` is null")
    private List<Products> wishlist = new ArrayList<>();
    
    @Column(name = "firstName", columnDefinition = "text")
    private String firstName;
    
    @Column(name = "lastName", columnDefinition = "text")
    private String lastName;
    
    @Column(name = "phoneNumber", columnDefinition = "text")
    private String phoneNumber;
    
    @Column(name = "email", columnDefinition = "text")
    private String email;
    
    @Column(name = "role")
    private String role;
    
    @Column(name = "disabled", nullable = false, columnDefinition = "boolean default false")
    private Boolean disabled;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "`belongsToId`", foreignKey = @ForeignKey(name = "none"))
    @Where(clause = "`deletedAt` is null")
    private List<File> avatar = new ArrayList<>();
    
    @Column(name = "password", columnDefinition = "text")
    private String password;
    
    @Column(name = "emailVerified", nullable = false, columnDefinition = "boolean default false")
    private Boolean emailVerified;
    
    @Column(name = "emailVerificationToken", columnDefinition = "text")
    private String emailVerificationToken;
    
    @Column(name = "emailVerificationTokenExpiresAt")
    private Date emailVerificationTokenExpiresAt;
    
    @Column(name = "passwordResetToken", columnDefinition = "text")
    private String passwordResetToken;
    
    @Column(name = "passwordResetTokenExpiresAt")
    private Date passwordResetTokenExpiresAt;
    
    @Column(name = "provider", columnDefinition = "text")
    private String provider;
    
    @Column(name = "`importHash`", unique = true)
    private String importHash;
}
