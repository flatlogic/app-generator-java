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

    @Query("select u from User u where u.emailVerified = false and u.emailVerificationToken is not null" +
            " and u.deletedAt is null")
    List<User> findUsersByEmailVerificationToken();

    @Query("select u from User u where u.email = :email and u.deletedAt is null")
    User findByEmail(@Param(value = "email") String email);

    @Query("select u from User u where u.email = :email and u.emailVerified = true and u.deletedAt is null")
    User findByEmailAndEmailVerifiedIsTrue(@Param(value = "email") String email);

    @Query("select u from User u where u.emailVerificationToken = :emailVerificationToken and u.deletedAt is null")
    User findByEmailVerificationToken(@Param(value = "emailVerificationToken") String emailVerificationToken);

    @Query("select u from User u where u.passwordResetToken = :passwordResetToken and u.deletedAt is null")
    User findByPasswordResetToken(@Param(value = "passwordResetToken") String resetTokenExpiresAt);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.emailVerified = true, u.emailVerificationToken = null," +
            " u.emailVerificationTokenExpiresAt = null where u.emailVerificationToken = :emailVerificationToken")
    void updateEmailVerificationToken(@Param(value = "emailVerificationToken") String emailVerificationToken);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.passwordResetToken = :passwordResetToken, u.passwordResetTokenExpiresAt = " +
            ":passwordResetTokenExpiresAt where u.email = :email")
    void updatePasswordResetToken(@Param(value = "passwordResetToken") String passwordResetToken,
                                  @Param(value = "passwordResetTokenExpiresAt") Date passwordResetTokenExpiresAt,
                                  @Param(value = "email") String email);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.deletedAt = :deletedAt where u.id = :id")
    void updateDeletedAt(@Param(value = "id") UUID id, @Param(value = "deletedAt") Date deletedAt);

    @Modifying(clearAutomatically = true)
    @Query("delete from User u where u.emailVerificationToken = :emailVerificationToken")
    void deleteUserByEmailVerificationToken(@Param(value = "emailVerificationToken") String emailVerificationToken);

}
