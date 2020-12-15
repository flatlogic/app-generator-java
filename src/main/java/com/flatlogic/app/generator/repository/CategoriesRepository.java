
package com.flatlogic.app.generator.repository;

import com.flatlogic.app.generator.entity.Categories;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CategoriesRepository extends JpaRepository<Categories, UUID> {
    @Query("select p from Categories p where p.deletedAt is null")
    List<Categories> findCategoriesByDeletedAtIsNull(Pageable pageable);

    @Query("select p from Categories p where p.deletedAt is null")
    List<Categories> findCategoriesByDeletedAtIsNull(Sort sort);

    @Query("select p from Categories p where p.title like ?1% and p.deletedAt is null")
    List<Categories> findCategoriesByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(String query, Pageable pageable);



    @Modifying(clearAutomatically = true)
    @Query("update Categories p set p.deletedAt = :deletedAt where p.id = :id")
    void updateDeletedAt(@Param(value = "id") UUID id, @Param(value = "deletedAt") Date deletedAt);


}
