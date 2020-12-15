
package com.flatlogic.app.generator.repository;

import com.flatlogic.app.generator.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {
    @Query("select p from Users p where p.deletedAt is null")
    List<Users> findUsersByDeletedAtIsNull(Pageable pageable);

    @Query("select p from Users p where p.deletedAt is null")
    List<Users> findUsersByDeletedAtIsNull(Sort sort);

    @Query("select p from Users p where p.title like ?1% and p.deletedAt is null")
    List<Users> findUsersByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(String query, Pageable pageable);



    @Modifying(clearAutomatically = true)
    @Query("update Users p set p.deletedAt = :deletedAt where p.id = :id")
    void updateDeletedAt(@Param(value = "id") UUID id, @Param(value = "deletedAt") Date deletedAt);


    @Query("select u from Users u where u.email = :email and u.deletedAt is null")
    Users findByEmail(String email);

}
