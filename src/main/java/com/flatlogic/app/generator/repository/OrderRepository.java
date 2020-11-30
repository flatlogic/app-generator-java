package com.flatlogic.app.generator.repository;

import com.flatlogic.app.generator.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("select o from Order o where o.deletedAt is null")
    List<Order> findOrdersByDeletedAtIsNull(Pageable pageable);

    @Query("select o from Order o where o.deletedAt is null")
    List<Order> findOrdersByDeletedAtIsNull(Sort sort);

    @Modifying(clearAutomatically = true)
    @Query("update Order o set o.deletedAt = :deletedAt where o.id = :id")
    void updateDeletedAt(@Param(value = "id") UUID id, @Param(value = "deletedAt") Date deletedAt);

}
