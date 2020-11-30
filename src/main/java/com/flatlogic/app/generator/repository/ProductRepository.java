package com.flatlogic.app.generator.repository;

import com.flatlogic.app.generator.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("select p from Product p where p.deletedAt is null")
    List<Product> findProductsByDeletedAtIsNull(Pageable pageable);

    @Query("select p from Product p where p.deletedAt is null")
    List<Product> findProductsByDeletedAtIsNull(Sort sort);

    @Query("select p from Product p where p.title like ?1% and p.deletedAt is null")
    List<Product> findProductsByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(String query, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Product p set p.deletedAt = :deletedAt where p.id = :id")
    void updateDeletedAt(@Param(value = "id") UUID id, @Param(value = "deletedAt") Date deletedAt);

}
