
package com.flatlogic.app.generator.service;

import com.flatlogic.app.generator.controller.request.CategoriesRequest;
import com.flatlogic.app.generator.entity.Categories;

import java.util.List;
import java.util.UUID;

public interface CategoriesService {

    List<Categories> getCategories(Integer offset, Integer limit, String orderBy);

    List<Categories> getCategories(String query, Integer limit);

    Categories getCategoriesById(UUID id);

    Categories saveCategories(CategoriesRequest categoriesRequest, String username);

    Categories updateCategories(UUID id, CategoriesRequest categoriesRequest, String username);

    void deleteCategories(UUID id);

}
