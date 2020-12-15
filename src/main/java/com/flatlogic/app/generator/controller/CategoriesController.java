package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.data.AutocompleteData;
import com.flatlogic.app.generator.controller.request.RequestData;
import com.flatlogic.app.generator.controller.request.GetModelAttribute;
import com.flatlogic.app.generator.controller.request.CategoriesRequest;
import com.flatlogic.app.generator.controller.data.RowsData;
import com.flatlogic.app.generator.dto.CategoriesDto;
import com.flatlogic.app.generator.entity.Categories;
import com.flatlogic.app.generator.service.CategoriesService;
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
 * CategoriesController REST controller.
 */
@RestController
@RequestMapping("categories")
public class CategoriesController {

    /**
     * Logger constant.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriesController.class);

    /**
     * CategoriesService instance.
     */
    @Autowired
    private CategoriesService categoriesService;

    /**
     * DefaultConversionService instance.
     */
    @Autowired
    private DefaultConversionService defaultConversionService;

    /**
     * Get categories.
     *
     * @param modelAttribute GetModelAttribute
     * @return Categories RowsWrapper
     */
    @GetMapping
    public ResponseEntity<RowsData<CategoriesDto>> getCategories(@ModelAttribute GetModelAttribute modelAttribute) {
        LOGGER.info("Get categories.");
        RowsData<CategoriesDto> rowsData = new RowsData<>();
        List<Categories> categories = categoriesService.getCategories(modelAttribute.getOffset(),
                modelAttribute.getLimit(), modelAttribute.getOrderBy());
        List<CategoriesDto> categoriesDtos = categories.stream().map(__categories -> defaultConversionService.
                convert(__categories, CategoriesDto.class)).collect(Collectors.toList());
        rowsData.setRows(categoriesDtos);
        rowsData.setCount(categoriesDtos.size());
        return new ResponseEntity<>(rowsData, HttpStatus.OK);
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
        List<Categories> categories = categoriesService.getCategories(query, limit);
        List<AutocompleteData> wrappers = categories.stream().map(__categories ->
                new AutocompleteData(__categories.getId(), __categories.getImportHash())).collect(Collectors.toList());
        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }

    /**
     * Get categories by id.
     *
     * @param id Categories Id
     * @return Categories
     */
    @GetMapping("{id}")
    public ResponseEntity<CategoriesDto> getCategoriesById(@PathVariable UUID id) {
        LOGGER.info("Get categories by id.");
        return Optional.ofNullable(categoriesService.getCategoriesById(id))
                .map(categories -> new ResponseEntity<>(defaultConversionService
                        .convert(categories, CategoriesDto.class), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Save categories.
     *
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Categories
     */
    @PostMapping
    public ResponseEntity<CategoriesDto> saveCategories(@RequestBody RequestData<CategoriesRequest> requestData,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Save categories.");
        Categories categories = categoriesService.saveCategories(requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(categories, CategoriesDto.class), HttpStatus.OK);
    }

    /**
     * Update categories.
     *
     * @param id          Categories id
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Categories
     */
    @PutMapping("{id}")
    public ResponseEntity<CategoriesDto> updateCategories(@PathVariable UUID id,
                                                    @RequestBody RequestData<CategoriesRequest> requestData,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Update categories.");
        Categories categories = categoriesService.updateCategories(id, requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(categories, CategoriesDto.class), HttpStatus.OK);
    }

    /**
     * Delete categories.
     *
     * @param id Categories id
     * @return Void
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategories(@PathVariable UUID id) {
        LOGGER.info("Delete categories.");
        categoriesService.deleteCategories(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
