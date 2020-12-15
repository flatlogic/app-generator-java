
package com.flatlogic.app.generator.service;

import com.flatlogic.app.generator.controller.request.ProductsRequest;
import com.flatlogic.app.generator.entity.Products;

import java.util.List;
import java.util.UUID;

public interface ProductsService {

    List<Products> getProducts(Integer offset, Integer limit, String orderBy);

    List<Products> getProducts(String query, Integer limit);

    Products getProductsById(UUID id);

    Products saveProducts(ProductsRequest productsRequest, String username);

    Products updateProducts(UUID id, ProductsRequest productsRequest, String username);

    void deleteProducts(UUID id);

}
