
package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.controller.request.ProductsRequest;

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

import com.flatlogic.app.generator.service.ProductsService;
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
 * ProductsService service.
 */
@Service
@Transactional(readOnly = true)
public class ProductsServiceImpl implements ProductsService {


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
     * Get products.
     *
     * @param offset  Page offset
     * @param limit   Limit of Records
     * @param orderBy Order by default createdAt
     * @return List of Products
     */
    @Override
    public List<Products> getProducts(final Integer offset, final Integer limit, final String orderBy) {
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
            return productsRepository.findProductsByDeletedAtIsNull(sortedByCreatedAtDesc);
        } else {
            return productsRepository.findProductsByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());
        }
    }

    /**
     * Get products.
     *
     * @param query String for search
     * @param limit Limit of Records
     * @return List of Products
     */
    @Override
    public List<Products> getProducts(final String query, final Integer limit) {
        if (!ObjectUtils.isEmpty(query)) {
            return productsRepository.findProductsByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(query,
                    PageRequest.of(0, limit, Sort.by(Constants.TITLE).ascending()));
        } else {
            return productsRepository.findProductsByDeletedAtIsNull(PageRequest
                    .of(0, limit, Sort.by(Constants.TITLE).ascending()));
        }
    }

    /**
     * Get products by id.
     *
     * @param id Products Id
     * @return Products
     */
    @Override
    public Products getProductsById(final UUID id) {
        return productsRepository.findById(id).orElse(null);
    }

    /**
     * Save products.
     *
     * @param productsRequest ProductsData
     * @param username       Users name
     * @return Products
     */
    @Override
    @Transactional
    public Products saveProducts(final ProductsRequest productsRequest, final String username) {
        Users createdBy = usersRepository.findByEmail(username);
        Products products = new Products();
        setFields(productsRequest, products);
        products.setCreatedBy(createdBy);
        products = productsRepository.save(products);
        setEntries(productsRequest, products, createdBy);
        return products;
    }

    /**
     * Update products.
     *
     * @param id             Products Id
     * @param productsRequest ProductsData
     * @param username       Users name
     * @return Products
     */
    @Override
    @Transactional
    public Products updateProducts(final UUID id, final ProductsRequest productsRequest, final String username) {
        Products products = getProductsById(id);
        if (products == null) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_PRODUCT_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        Users updatedBy = usersRepository.findByEmail(username);
        setFields(productsRequest, products);
        setEntries(productsRequest, products, updatedBy);
        products.setUpdatedBy(updatedBy);
        return products;
    }

    /**
     * Delete products.
     *
     * @param id Products Id
     */
    @Override
    @Transactional
    public void deleteProducts(final UUID id) {
        if (!productsRepository.existsById(id)) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_PRODUCT_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        productsRepository.updateDeletedAt(id, new Date());
    }

    private void setFields(final ProductsRequest productsRequest, final Products products) {

        products.setTitle(productsRequest.getTitle());
    
        products.setPrice(productsRequest.getPrice());
    
        products.setDiscount(productsRequest.getDiscount());
    
        products.setDescription(productsRequest.getDescription());
    
        products.setRating(productsRequest.getRating());
    
        products.setStatus(productsRequest.getStatus());
    

        products.setImportHash(productsRequest.getImportHash());
    }

    private void setEntries(final ProductsRequest productsRequest, final Products products, final Users modifiedBy) {

        Optional.ofNullable(productsRequest.getCategories()).ifPresent(categoriesIds -> {
            final List<Categories> categories = products.getCategories();
            categories.clear();
            categoriesIds.forEach(categoriesId -> categories.add(categoriesRepository.getOne(categoriesId)));
        });

        Optional.ofNullable(productsRequest.getMore_products()).ifPresent(more_productsIds -> {
            final List<Products> more_products = products.getMore_products();
            more_products.clear();
            more_productsIds.forEach(more_productsId -> more_products.add(productsRepository.getOne(more_productsId)));
        });

        Optional.ofNullable(productsRequest.getImage()).ifPresent(fileRequests -> {
            final List<File> image = products.getImage();
            Map<UUID, File> mapFiles = image.stream().collect(Collectors.toMap(File::getId, file -> file));
            image.clear();
            fileRequests.forEach(fileRequest -> {
                File file = null;
                if (fileRequest.isNew()) {
                    file = new File();
                    file.setBelongsTo("products");
                    file.setBelongsToId(products.getId());
                    file.setBelongsToColumn("image");
                    file.setName(fileRequest.getName());
                    file.setPrivateUrl(fileRequest.getPrivateUrl());
                    file.setPublicUrl(fileRequest.getPublicUrl());
                    file.setSizeInBytes(fileRequest.getSizeInBytes());
                    file.setCreatedBy(modifiedBy);
                } else {
                    file = mapFiles.remove(fileRequest.getId());
                    file.setUpdatedBy(modifiedBy);
                }
                image.add(file);
            });
            mapFiles.forEach((key, value) -> {
                File file = value;
                file.setDeletedAt(new Date());
                image.add(file);
            });
        });

    }


}
