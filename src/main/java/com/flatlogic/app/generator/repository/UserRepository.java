package com.flatlogic.app.generator.repository;

import com.flatlogic.app.generator.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
   
    @Query("select u from User u where u.deletedAt is null")
    List<User> findUsersByDeletedAtIsNull(Pageable pageable);

    @Query("select u from User u where u.deletedAt is null")
    List<User> findUsersByDeletedAtIsNull(Sort sort);

    @Query("select u from User u where u.email like ?1% and u.deletedAt is null")
    List<User> findUsersByEmailLikeAndDeletedAtIsNullOrderByEmailAsc(String query, Pageable pageable);

    @Query("select u from User u where u.email = :email and u.deletedAt is null")
    User findByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.deletedAt = :deletedAt where u.id = :id")
    void updateDeletedAt(@Param(value = "id") UUID id, @Param(value = "deletedAt") Date deletedAt);

}
