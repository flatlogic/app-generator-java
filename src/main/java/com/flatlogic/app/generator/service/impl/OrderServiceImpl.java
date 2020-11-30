package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.controller.request.OrderRequest;
import com.flatlogic.app.generator.entity.Order;
import com.flatlogic.app.generator.exception.NoSuchEntityException;
import com.flatlogic.app.generator.repository.OrderRepository;
import com.flatlogic.app.generator.repository.ProductRepository;
import com.flatlogic.app.generator.repository.UserRepository;
import com.flatlogic.app.generator.service.OrderService;
import com.flatlogic.app.generator.type.OrderStatusType;
import com.flatlogic.app.generator.type.OrderType;
import com.flatlogic.app.generator.util.Constants;
import com.flatlogic.app.generator.util.MessageCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * OrderService service.
 */
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    /**
     * OrderRepository instance.
     */
    @Autowired
    private OrderRepository orderRepository;

    /**
     * ProductRepository instance.
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * UserRepository instance.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * MessageCodeUtil instance.
     */
    @Autowired
    private MessageCodeUtil messageCodeUtil;

    /**
     * Get orders.
     *
     * @param offset  Page offset
     * @param limit   Limit of Records
     * @param orderBy Order by default createdAt
     * @return List of Orders
     */
    @Override
    public List<Order> getOrders(final Integer offset, final Integer limit, final String orderBy) {
        String[] orderParam = !ObjectUtils.isEmpty(orderBy) ? orderBy.split("_") : null;
        if (limit != null && offset != null) {
            Sort sort = Sort.by(Constants.CREATED_AT).descending();
            if (orderParam != null) {
                if (Objects.equals(OrderType.ASC.name(), orderParam[1])) {
                    sort = Sort.by(orderParam[0]).ascending();
                } else {
                    sort = Sort.by(orderParam[0]).descending();
                }
            }
            Pageable sortedByCreatedAtDesc = PageRequest.of(offset, limit, sort);
            return orderRepository.findOrdersByDeletedAtIsNull(sortedByCreatedAtDesc);
        } else {
            return orderRepository.findOrdersByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());
        }
    }

    /**
     * Get order by id.
     *
     * @param id Order Id
     * @return Order
     */
    @Override
    public Order getOrderById(final UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

    /**
     * Save order.
     *
     * @param orderRequest Order data
     * @param username     User name
     * @return Order
     */
    @Override
    @Transactional
    public Order saveOrder(final OrderRequest orderRequest, final String username) {
        Order order = new Order();
        setFieldsData(orderRequest, order);
        order.setCreatedAt(new Date());
        order.setCreatedBy(userRepository.findByEmail(username));
        return orderRepository.save(order);
    }

    /**
     * Update order.
     *
     * @param id           Order Id
     * @param orderRequest Order data
     * @param username     User name
     * @return Order
     */
    @Override
    @Transactional
    public Order updateOrder(final UUID id, final OrderRequest orderRequest, final String username) {
        Order order = getOrderById(id);
        if (order == null) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_ORDER_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        setFieldsData(orderRequest, order);
        order.setUpdatedAt(new Date());
        order.setUpdatedBy(userRepository.findByEmail(username));
        return order;
    }

    /**
     * Delete order.
     *
     * @param id Order Id
     */
    @Override
    @Transactional
    public void deleteOrder(final UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_ORDER_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        orderRepository.updateDeletedAt(id, new Date());
    }

    private void setFieldsData(final OrderRequest orderRequest, final Order order) {
        order.setOrderDate(orderRequest.getOrderDate());
        order.setAmount(orderRequest.getAmount());
        order.setStatus(OrderStatusType.valueOfStatus(orderRequest.getStatus()));
        order.setImportHash(orderRequest.getImportHash());
        Optional.ofNullable(orderRequest.getProductId()).ifPresent(productId ->
                order.setProduct(productRepository.getOne(productId)));
        Optional.ofNullable(orderRequest.getUserId()).ifPresent(userId ->
                order.setUser(userRepository.getOne(userId)));
    }

}
