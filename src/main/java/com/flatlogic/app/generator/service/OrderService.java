package com.flatlogic.app.generator.service;

import com.flatlogic.app.generator.controller.request.OrderRequest;
import com.flatlogic.app.generator.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<Order> getOrders(Integer offset, Integer limit, String orderBy);

    Order getOrderById(UUID id);

    Order saveOrder(OrderRequest orderRequest, String username);

    Order updateOrder(UUID id, OrderRequest orderRequest, String username);

    void deleteOrder(UUID id);

}
