package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.data.AutocompleteData;
import com.flatlogic.app.generator.controller.request.RequestData;
import com.flatlogic.app.generator.controller.request.GetModelAttribute;
import com.flatlogic.app.generator.controller.request.ProductRequest;
import com.flatlogic.app.generator.controller.data.RowsData;
import com.flatlogic.app.generator.dto.ProductDto;
import com.flatlogic.app.generator.entity.Product;
import com.flatlogic.app.generator.service.ProductService;
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
 * ProductController REST controller.
 */
@RestController
@RequestMapping("products")
public class ProductController {

    /**
     * Logger constant.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    /**
     * ProductService instance.
     */
    @Autowired
    private ProductService productService;

    /**
     * DefaultConversionService instance.
     */
    @Autowired
    private DefaultConversionService defaultConversionService;

    /**
     * Get products.
     *
     * @param modelAttribute GetModelAttribute
     * @return Product RowsWrapper
     */
    @GetMapping
    public ResponseEntity<RowsData<ProductDto>> getProducts(@ModelAttribute GetModelAttribute modelAttribute) {
        LOGGER.info("Get products.");
        RowsData<ProductDto> rowsData = new RowsData<>();
        List<Product> products = productService.getProducts(modelAttribute.getOffset(),
                modelAttribute.getLimit(), modelAttribute.getOrderBy());
        List<ProductDto> productDtos = products.stream().map(product -> defaultConversionService.
                convert(product, ProductDto.class)).collect(Collectors.toList());
        rowsData.setRows(productDtos);
        rowsData.setCount(productDtos.size());
        return ResponseEntity.ok(rowsData);
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
        List<Product> products = productService.getProducts(query, limit);
        List<AutocompleteData> wrappers = products.stream().map(product ->
                new AutocompleteData(product.getId(), product.getTitle())).collect(Collectors.toList());
        return ResponseEntity.ok(wrappers);
    }

    /**
     * Get product by id.
     *
     * @param id Product Id
     * @return Product
     */
    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
        LOGGER.info("Get product by id.");
        return Optional.ofNullable(productService.getProductById(id))
                .map(product -> new ResponseEntity<>(defaultConversionService
                        .convert(product, ProductDto.class), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Save product.
     *
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Product
     */
    @PostMapping
    public ResponseEntity<ProductDto> saveProduct(@RequestBody RequestData<ProductRequest> requestData,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Save product.");
        Product product = productService.saveProduct(requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(product, ProductDto.class), HttpStatus.OK);
    }

    /**
     * Update product.
     *
     * @param id          Product id
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Product
     */
    @PutMapping("{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id,
                                                    @RequestBody RequestData<ProductRequest> requestData,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Update product.");
        Product product = productService.updateProduct(id, requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(product, ProductDto.class), HttpStatus.OK);
    }

    /**
     * Delete product.
     *
     * @param id Product id
     * @return Void
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        LOGGER.info("Delete product.");
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
