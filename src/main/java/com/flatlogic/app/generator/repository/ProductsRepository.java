
package com.flatlogic.app.generator.repository;

import com.flatlogic.app.generator.entity.Products;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ProductsRepository extends JpaRepository<Products, UUID> {
    @Query("select p from Products p where p.deletedAt is null")
    List<Products> findProductsByDeletedAtIsNull(Pageable pageable);

    @Query("select p from Products p where p.deletedAt is null")
    List<Products> findProductsByDeletedAtIsNull(Sort sort);

    @Query("select p from Products p where p.title like ?1% and p.deletedAt is null")
    List<Products> findProductsByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(String query, Pageable pageable);



    @Modifying(clearAutomatically = true)
    @Query("update Products p set p.deletedAt = :deletedAt where p.id = :id")
    void updateDeletedAt(@Param(value = "id") UUID id, @Param(value = "deletedAt") Date deletedAt);


}
