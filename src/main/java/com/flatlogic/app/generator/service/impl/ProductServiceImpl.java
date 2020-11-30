package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.controller.request.ProductRequest;
import com.flatlogic.app.generator.entity.Category;
import com.flatlogic.app.generator.entity.File;
import com.flatlogic.app.generator.entity.Product;
import com.flatlogic.app.generator.entity.User;
import com.flatlogic.app.generator.exception.NoSuchEntityException;
import com.flatlogic.app.generator.repository.CategoryRepository;
import com.flatlogic.app.generator.repository.ProductRepository;
import com.flatlogic.app.generator.repository.UserRepository;
import com.flatlogic.app.generator.service.ProductService;
import com.flatlogic.app.generator.type.BelongsToColumnType;
import com.flatlogic.app.generator.type.BelongsToType;
import com.flatlogic.app.generator.type.OrderType;
import com.flatlogic.app.generator.type.ProductStatusType;
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
 * ProductService service.
 */
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    /**
     * CategoryRepository instance.
     */
    @Autowired
    private CategoryRepository categoryRepository;

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
     * Get products.
     *
     * @param offset  Page offset
     * @param limit   Limit of Records
     * @param orderBy Order by default createdAt
     * @return List of Products
     */
    @Override
    public List<Product> getProducts(final Integer offset, final Integer limit, final String orderBy) {
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
            return productRepository.findProductsByDeletedAtIsNull(sortedByCreatedAtDesc);
        } else {
            return productRepository.findProductsByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());
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
    public List<Product> getProducts(final String query, final Integer limit) {
        if (!ObjectUtils.isEmpty(query)) {
            return productRepository.findProductsByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(query,
                    PageRequest.of(0, limit, Sort.by(Constants.TITLE).ascending()));
        } else {
            return productRepository.findProductsByDeletedAtIsNull(PageRequest
                    .of(0, limit, Sort.by(Constants.TITLE).ascending()));
        }
    }

    /**
     * Get product by id.
     *
     * @param id Product Id
     * @return Product
     */
    @Override
    public Product getProductById(final UUID id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Save product.
     *
     * @param productRequest ProductData
     * @param username       User name
     * @return Product
     */
    @Override
    @Transactional
    public Product saveProduct(final ProductRequest productRequest, final String username) {
        User createdBy = userRepository.findByEmail(username);
        Product product = new Product();
        setFields(productRequest, product);
        product.setCreatedBy(createdBy);
        product = productRepository.save(product);
        setEntries(productRequest, product, createdBy);
        return product;
    }

    /**
     * Update product.
     *
     * @param id             Product Id
     * @param productRequest ProductData
     * @param username       User name
     * @return Product
     */
    @Override
    @Transactional
    public Product updateProduct(final UUID id, final ProductRequest productRequest, final String username) {
        Product product = getProductById(id);
        if (product == null) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_PRODUCT_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        User updatedBy = userRepository.findByEmail(username);
        setFields(productRequest, product);
        setEntries(productRequest, product, updatedBy);
        product.setUpdatedBy(updatedBy);
        return product;
    }

    /**
     * Delete product.
     *
     * @param id Product Id
     */
    @Override
    @Transactional
    public void deleteProduct(final UUID id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_PRODUCT_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        productRepository.updateDeletedAt(id, new Date());
    }

    private void setFields(final ProductRequest productRequest, final Product product) {
        product.setTitle(productRequest.getTitle());
        product.setPrice(productRequest.getPrice());
        product.setDiscount(productRequest.getDiscount());
        product.setDescription(productRequest.getDescription());
        product.setRating(productRequest.getRating());
        product.setStatus(ProductStatusType.valueOfStatus(productRequest.getStatus()));
        product.setImportHash(productRequest.getImportHash());
    }

    private void setEntries(final ProductRequest productRequest, final Product product, final User modifiedBy) {
        Optional.ofNullable(productRequest.getCategoryIds()).ifPresent(categoryIds -> {
            final List<Category> categories = product.getCategories();
            categories.clear();
            categoryIds.forEach(categoryId -> categories.add(categoryRepository.getOne(categoryId)));
        });
        Optional.ofNullable(productRequest.getProductIds()).ifPresent(productIds -> {
            final List<Product> products = product.getProducts();
            products.clear();
            productIds.forEach(productId -> products.add(productRepository.getOne(productId)));
        });
        Optional.ofNullable(productRequest.getFileRequests()).ifPresent(fileRequests -> {
            final List<File> files = product.getFiles();
            Map<UUID, File> mapFiles = files.stream().collect(Collectors.toMap(File::getId, file -> file));
            files.clear();
            fileRequests.forEach(fileRequest -> {
                File file = null;
                if (fileRequest.isNew()) {
                    file = new File();
                    file.setBelongsTo(BelongsToType.PRODUCTS.getType());
                    file.setBelongsToId(product.getId());
                    file.setBelongsToColumn(BelongsToColumnType.IMAGE.getType());
                    file.setName(fileRequest.getName());
                    file.setPrivateUrl(fileRequest.getPrivateUrl());
                    file.setPublicUrl(fileRequest.getPublicUrl());
                    file.setSizeInBytes(fileRequest.getSizeInBytes());
                    file.setCreatedBy(modifiedBy);
                } else {
                    file = mapFiles.remove(fileRequest.getId());
                    file.setUpdatedBy(modifiedBy);
                }
                files.add(file);
            });
            mapFiles.forEach((key, value) -> {
                File file = value;
                file.setDeletedAt(new Date());
                files.add(file);
            });
        });
    }

}
