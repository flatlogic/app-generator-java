package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.data.AutocompleteData;
import com.flatlogic.app.generator.controller.request.RequestData;
import com.flatlogic.app.generator.controller.request.GetModelAttribute;
import com.flatlogic.app.generator.controller.request.ProductsRequest;
import com.flatlogic.app.generator.controller.data.RowsData;
import com.flatlogic.app.generator.dto.ProductsDto;
import com.flatlogic.app.generator.entity.Products;
import com.flatlogic.app.generator.service.ProductsService;
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
 * ProductsController REST controller.
 */
@RestController
@RequestMapping("products")
public class ProductsController {

    /**
     * Logger constant.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsController.class);

    /**
     * ProductsService instance.
     */
    @Autowired
    private ProductsService productsService;

    /**
     * DefaultConversionService instance.
     */
    @Autowired
    private DefaultConversionService defaultConversionService;

    /**
     * Get products.
     *
     * @param modelAttribute GetModelAttribute
     * @return Products RowsWrapper
     */
    @GetMapping
    public ResponseEntity<RowsData<ProductsDto>> getProducts(@ModelAttribute GetModelAttribute modelAttribute) {
        LOGGER.info("Get products.");
        RowsData<ProductsDto> rowsData = new RowsData<>();
        List<Products> products = productsService.getProducts(modelAttribute.getOffset(),
                modelAttribute.getLimit(), modelAttribute.getOrderBy());
        List<ProductsDto> productsDtos = products.stream().map(__products -> defaultConversionService.
                convert(__products, ProductsDto.class)).collect(Collectors.toList());
        rowsData.setRows(productsDtos);
        rowsData.setCount(productsDtos.size());
        return new ResponseEntity<>(rowsData, HttpStatus.OK);
    }

    /**
     * Get products.
     *
     * @param query String for search
     * @param limit Limit of Records
     * @return List of Products
     */
    @GetMapping("autocomplete")
    public ResponseEntity<List<AutocompleteData>> getProductsAutocomplete(@RequestParam String query,
                                                                          @RequestParam Integer limit) {
        LOGGER.info("Get products (autocomplete).");
        List<Products> products = productsService.getProducts(query, limit);
        List<AutocompleteData> wrappers = products.stream().map(__products ->
                new AutocompleteData(__products.getId(), __products.getImportHash())).collect(Collectors.toList());
        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }

    /**
     * Get products by id.
     *
     * @param id Products Id
     * @return Products
     */
    @GetMapping("{id}")
    public ResponseEntity<ProductsDto> getProductsById(@PathVariable UUID id) {
        LOGGER.info("Get products by id.");
        return Optional.ofNullable(productsService.getProductsById(id))
                .map(products -> new ResponseEntity<>(defaultConversionService
                        .convert(products, ProductsDto.class), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Save products.
     *
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Products
     */
    @PostMapping
    public ResponseEntity<ProductsDto> saveProducts(@RequestBody RequestData<ProductsRequest> requestData,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Save products.");
        Products products = productsService.saveProducts(requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(products, ProductsDto.class), HttpStatus.OK);
    }

    /**
     * Update products.
     *
     * @param id          Products id
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Products
     */
    @PutMapping("{id}")
    public ResponseEntity<ProductsDto> updateProducts(@PathVariable UUID id,
                                                    @RequestBody RequestData<ProductsRequest> requestData,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Update products.");
        Products products = productsService.updateProducts(id, requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(products, ProductsDto.class), HttpStatus.OK);
    }

    /**
     * Delete products.
     *
     * @param id Products id
     * @return Void
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProducts(@PathVariable UUID id) {
        LOGGER.info("Delete products.");
        productsService.deleteProducts(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
