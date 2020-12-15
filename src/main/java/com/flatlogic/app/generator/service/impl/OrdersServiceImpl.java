
package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.controller.request.OrdersRequest;

import com.flatlogic.app.generator.entity.Products;

import com.flatlogic.app.generator.entity.Categories;

import com.flatlogic.app.generator.entity.Orders;

import com.flatlogic.app.generator.entity.Users;

import com.flatlogic.app.generator.entity.File;
import com.flatlogic.app.generator.exception.NoSuchEntityException;

import com.flatlogic.app.generator.repository.ProductsRepository;

import com.flatlogic.app.generator.repository.CategoriesRepository;

import com.flatlogic.app.generator.repository.OrdersRepository;

import com.flatlogic.app.generator.repository.UsersRepository;

import com.flatlogic.app.generator.service.OrdersService;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * OrdersService service.
 */
@Service
@Transactional(readOnly = true)
public class OrdersServiceImpl implements OrdersService {


    /**
     * ProductsRepository instance.
     */
    @Autowired
    private ProductsRepository productsRepository;

    /**
     * CategoriesRepository instance.
     */
    @Autowired
    private CategoriesRepository categoriesRepository;

    /**
     * OrdersRepository instance.
     */
    @Autowired
    private OrdersRepository ordersRepository;

    /**
     * UsersRepository instance.
     */
    @Autowired
    private UsersRepository usersRepository;


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
    public List<Orders> getOrders(final Integer offset, final Integer limit, final String orderBy) {
        String[] orderParam = !ObjectUtils.isEmpty(orderBy) ? orderBy.split("_") : null;
        if (limit != null && offset != null) {
            Sort sort = Sort.by(Constants.CREATED_AT).descending();
            if (orderParam != null) {
                if (Objects.equals("ASC", orderParam[1])) {
                    sort = Sort.by(orderParam[0]).ascending();
                } else {
                    sort = Sort.by(orderParam[0]).descending();
                }
            }
            Pageable sortedByCreatedAtDesc = PageRequest.of(offset, limit, sort);
            return ordersRepository.findOrdersByDeletedAtIsNull(sortedByCreatedAtDesc);
        } else {
            return ordersRepository.findOrdersByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());
        }
    }

    /**
     * Get orders.
     *
     * @param query String for search
     * @param limit Limit of Records
     * @return List of Orders
     */
    @Override
    public List<Orders> getOrders(final String query, final Integer limit) {
        if (!ObjectUtils.isEmpty(query)) {
            return ordersRepository.findOrdersByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(query,
                    PageRequest.of(0, limit, Sort.by(Constants.TITLE).ascending()));
        } else {
            return ordersRepository.findOrdersByDeletedAtIsNull(PageRequest
                    .of(0, limit, Sort.by(Constants.TITLE).ascending()));
        }
    }

    /**
     * Get orders by id.
     *
     * @param id Orders Id
     * @return Orders
     */
    @Override
    public Orders getOrdersById(final UUID id) {
        return ordersRepository.findById(id).orElse(null);
    }

    /**
     * Save orders.
     *
     * @param ordersRequest OrdersData
     * @param username       Users name
     * @return Orders
     */
    @Override
    @Transactional
    public Orders saveOrders(final OrdersRequest ordersRequest, final String username) {
        Users createdBy = usersRepository.findByEmail(username);
        Orders orders = new Orders();
        setFields(ordersRequest, orders);
        orders.setCreatedBy(createdBy);
        orders = ordersRepository.save(orders);
        setEntries(ordersRequest, orders, createdBy);
        return orders;
    }

    /**
     * Update orders.
     *
     * @param id             Orders Id
     * @param ordersRequest OrdersData
     * @param username       Users name
     * @return Orders
     */
    @Override
    @Transactional
    public Orders updateOrders(final UUID id, final OrdersRequest ordersRequest, final String username) {
        Orders orders = getOrdersById(id);
        if (orders == null) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_PRODUCT_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        Users updatedBy = usersRepository.findByEmail(username);
        setFields(ordersRequest, orders);
        setEntries(ordersRequest, orders, updatedBy);
        orders.setUpdatedBy(updatedBy);
        return orders;
    }

    /**
     * Delete orders.
     *
     * @param id Orders Id
     */
    @Override
    @Transactional
    public void deleteOrders(final UUID id) {
        if (!ordersRepository.existsById(id)) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_PRODUCT_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        ordersRepository.updateDeletedAt(id, new Date());
    }

    private void setFields(final OrdersRequest ordersRequest, final Orders orders) {

        orders.setOrder_date(ordersRequest.getOrder_date());
    
        orders.setAmount(ordersRequest.getAmount());
    
        orders.setStatus(ordersRequest.getStatus());
    

        orders.setImportHash(ordersRequest.getImportHash());
    }

    private void setEntries(final OrdersRequest ordersRequest, final Orders orders, final Users modifiedBy) {

    }


}
