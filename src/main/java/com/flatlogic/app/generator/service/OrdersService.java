
package com.flatlogic.app.generator.service;

import com.flatlogic.app.generator.controller.request.OrdersRequest;
import com.flatlogic.app.generator.entity.Orders;

import java.util.List;
import java.util.UUID;

public interface OrdersService {

    List<Orders> getOrders(Integer offset, Integer limit, String orderBy);

    List<Orders> getOrders(String query, Integer limit);

    Orders getOrdersById(UUID id);

    Orders saveOrders(OrdersRequest ordersRequest, String username);

    Orders updateOrders(UUID id, OrdersRequest ordersRequest, String username);

    void deleteOrders(UUID id);

}
