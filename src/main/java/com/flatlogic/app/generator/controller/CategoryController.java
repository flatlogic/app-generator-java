package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.data.AutocompleteData;
import com.flatlogic.app.generator.controller.request.CategoryRequest;
import com.flatlogic.app.generator.controller.request.RequestData;
import com.flatlogic.app.generator.controller.request.GetModelAttribute;
import com.flatlogic.app.generator.controller.data.RowsData;
import com.flatlogic.app.generator.dto.CategoryDto;
import com.flatlogic.app.generator.entity.Category;
import com.flatlogic.app.generator.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * CategoryController REST controller.
 */
@RestController
@RequestMapping("categories")
public class CategoryController {

    /**
     * Logger constant.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    /**
     * CategoryService instance.
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * DefaultConversionService instance.
     */
    @Autowired
    private DefaultConversionService defaultConversionService;

    /**
     * Get categories.
     *
     * @param modelAttribute GetModelAttribute
     * @return Category RowsWrapper
     */
    @GetMapping
    public ResponseEntity<RowsData<CategoryDto>> getCategories(@ModelAttribute GetModelAttribute modelAttribute) {
        LOGGER.info("Get categories.");
        RowsData<CategoryDto> rowsData = new RowsData<>();
        List<Category> categories = categoryService.getCategories(modelAttribute.getOffset(),
                modelAttribute.getLimit(), modelAttribute.getOrderBy());
        List<CategoryDto> categoryDtos = categories.stream().map(category -> defaultConversionService.
                convert(category, CategoryDto.class)).collect(Collectors.toList());
        rowsData.setRows(categoryDtos);
        rowsData.setCount(categoryDtos.size());
        return ResponseEntity.ok(rowsData);
    }

    /**
     * Get categories.
     *
     * @param query String for search
     * @param limit Limit of Records
     * @return List of Categories
     */
    @GetMapping("autocomplete")
    public ResponseEntity<List<AutocompleteData>> getCategoriesAutocomplete(@RequestParam String query,
                                                                            @RequestParam Integer limit) {
        LOGGER.info("Get categories (autocomplete).");
        List<Category> categories = categoryService.getCategories(query, limit);
        List<AutocompleteData> wrappers = categories.stream().map(category ->
                new AutocompleteData(category.getId(), category.getTitle())).collect(Collectors.toList());
        return ResponseEntity.ok(wrappers);
    }

    /**
     * Get category by id.
     *
     * @param id Category Id
     * @return Category
     */
    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable UUID id) {
        LOGGER.info("Get category by id.");
        return Optional.ofNullable(categoryService.getCategoryById(id))
                .map(category -> new ResponseEntity<>(defaultConversionService.
                        convert(category, CategoryDto.class), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Save category.
     *
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Category
     */
    @PostMapping
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody RequestData<CategoryRequest> requestData,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Save category.");
        Category category = categoryService.saveCategory(requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(category, CategoryDto.class), HttpStatus.OK);
    }

    /**
     * Update category.
     *
     * @param id          Category id
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Category
     */
    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable UUID id,
                                                      @RequestBody RequestData<CategoryRequest> requestData,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Update category.");
        Category category = categoryService.updateCategory(id, requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(category, CategoryDto.class), HttpStatus.OK);
    }

    /**
     * Delete category.
     *
     * @param id Category id
     * @return Void
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        LOGGER.info("Delete category.");
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
