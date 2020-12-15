
package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.controller.request.CategoriesRequest;

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

import com.flatlogic.app.generator.service.CategoriesService;
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
 * CategoriesService service.
 */
@Service
@Transactional(readOnly = true)
public class CategoriesServiceImpl implements CategoriesService {


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
     * Get categories.
     *
     * @param offset  Page offset
     * @param limit   Limit of Records
     * @param orderBy Order by default createdAt
     * @return List of Categories
     */
    @Override
    public List<Categories> getCategories(final Integer offset, final Integer limit, final String orderBy) {
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
            return categoriesRepository.findCategoriesByDeletedAtIsNull(sortedByCreatedAtDesc);
        } else {
            return categoriesRepository.findCategoriesByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());
        }
    }

    /**
     * Get categories.
     *
     * @param query String for search
     * @param limit Limit of Records
     * @return List of Categories
     */
    @Override
    public List<Categories> getCategories(final String query, final Integer limit) {
        if (!ObjectUtils.isEmpty(query)) {
            return categoriesRepository.findCategoriesByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(query,
                    PageRequest.of(0, limit, Sort.by(Constants.TITLE).ascending()));
        } else {
            return categoriesRepository.findCategoriesByDeletedAtIsNull(PageRequest
                    .of(0, limit, Sort.by(Constants.TITLE).ascending()));
        }
    }

    /**
     * Get categories by id.
     *
     * @param id Categories Id
     * @return Categories
     */
    @Override
    public Categories getCategoriesById(final UUID id) {
        return categoriesRepository.findById(id).orElse(null);
    }

    /**
     * Save categories.
     *
     * @param categoriesRequest CategoriesData
     * @param username       Users name
     * @return Categories
     */
    @Override
    @Transactional
    public Categories saveCategories(final CategoriesRequest categoriesRequest, final String username) {
        Users createdBy = usersRepository.findByEmail(username);
        Categories categories = new Categories();
        setFields(categoriesRequest, categories);
        categories.setCreatedBy(createdBy);
        categories = categoriesRepository.save(categories);
        setEntries(categoriesRequest, categories, createdBy);
        return categories;
    }

    /**
     * Update categories.
     *
     * @param id             Categories Id
     * @param categoriesRequest CategoriesData
     * @param username       Users name
     * @return Categories
     */
    @Override
    @Transactional
    public Categories updateCategories(final UUID id, final CategoriesRequest categoriesRequest, final String username) {
        Categories categories = getCategoriesById(id);
        if (categories == null) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_PRODUCT_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        Users updatedBy = usersRepository.findByEmail(username);
        setFields(categoriesRequest, categories);
        setEntries(categoriesRequest, categories, updatedBy);
        categories.setUpdatedBy(updatedBy);
        return categories;
    }

    /**
     * Delete categories.
     *
     * @param id Categories Id
     */
    @Override
    @Transactional
    public void deleteCategories(final UUID id) {
        if (!categoriesRepository.existsById(id)) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_PRODUCT_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        categoriesRepository.updateDeletedAt(id, new Date());
    }

    private void setFields(final CategoriesRequest categoriesRequest, final Categories categories) {

        categories.setTitle(categoriesRequest.getTitle());
    

        categories.setImportHash(categoriesRequest.getImportHash());
    }

    private void setEntries(final CategoriesRequest categoriesRequest, final Categories categories, final Users modifiedBy) {

    }


}
