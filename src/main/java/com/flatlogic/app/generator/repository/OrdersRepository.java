
package com.flatlogic.app.generator.repository;

import com.flatlogic.app.generator.entity.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface OrdersRepository extends JpaRepository<Orders, UUID> {
    @Query("select p from Orders p where p.deletedAt is null")
    List<Orders> findOrdersByDeletedAtIsNull(Pageable pageable);

    @Query("select p from Orders p where p.deletedAt is null")
    List<Orders> findOrdersByDeletedAtIsNull(Sort sort);

    @Query("select p from Orders p where p.title like ?1% and p.deletedAt is null")
    List<Orders> findOrdersByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(String query, Pageable pageable);


    @Modifying(clearAutomatically = true)
    @Query("update Orders o set o.product.id = null where o.product.id = :productId")
    void setProductIdAtNull(@Param(value = "productId") UUID productId);

    @Modifying(clearAutomatically = true)
    @Query("update Orders o set o.user.id = null where o.user.id = :userId")
    void setUserIdAtNull(@Param(value = "userId") UUID userId);


    @Modifying(clearAutomatically = true)
    @Query("update Orders p set p.deletedAt = :deletedAt where p.id = :id")
    void updateDeletedAt(@Param(value = "id") UUID id, @Param(value = "deletedAt") Date deletedAt);


}
