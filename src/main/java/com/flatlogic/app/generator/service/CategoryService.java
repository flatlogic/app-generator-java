package com.flatlogic.app.generator.service;

import com.flatlogic.app.generator.controller.request.CategoryRequest;
import com.flatlogic.app.generator.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<Category> getCategories(Integer offset, Integer limit, String orderBy);

    List<Category> getCategories(String query, Integer limit);

    Category getCategoryById(UUID id);

    Category saveCategory(CategoryRequest categoryRequest, String username);

    Category updateCategory(UUID id, CategoryRequest categoryRequest, String username);

    void deleteCategory(UUID id);

}
