package com.flatlogic.app.generator.converter;

import com.flatlogic.app.generator.dto.CategoryDto;
import com.flatlogic.app.generator.dto.FileDto;
import com.flatlogic.app.generator.dto.ProductDto;
import com.flatlogic.app.generator.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductToDtoConverter implements Converter<Product, ProductDto> {

    @Autowired
    private CategoryToDtoConverter categoryToDtoConverter;

    @Autowired
    private ProductListToDtoListConverter productListToDtoListConverter;

    @Autowired
    private FileToDtoConverter fileToDtoConverter;

    @Override
    public ProductDto convert(final Product source) {
        final ProductDto productDto = new ProductDto();
        productDto.setId(source.getId());
        productDto.setTitle(source.getTitle());
        productDto.setPrice(source.getPrice());
        productDto.setDiscount(source.getDiscount());
        productDto.setDescription(source.getDescription());
        productDto.setRating(source.getRating());
        Optional.ofNullable(source.getStatus()).ifPresent(
                status -> productDto.setStatus(status.getStatusValue()));
        productDto.setImportHash(source.getImportHash());
        Optional.ofNullable(source.getCategories()).ifPresent(categories -> {
            final List<CategoryDto> categoryDtos = productDto.getCategoryDtos();
            categories.forEach(category -> categoryDtos.add(categoryToDtoConverter.convert(category)));
        });
        Optional.ofNullable(source.getProducts()).ifPresent(products ->
                productDto.setProductDtos(productListToDtoListConverter.convert(products)));
        Optional.ofNullable(source.getFiles()).ifPresent(files -> {
            final List<FileDto> fileDtos = productDto.getFileDtos();
            files.forEach(file -> fileDtos.add(fileToDtoConverter.convert(file)));
        });
        productDto.setCreatedAt(source.getCreatedAt());
        productDto.setUpdatedAt(source.getUpdatedAt());
        productDto.setDeletedAt(source.getDeletedAt());
        Optional.ofNullable(source.getCreatedBy()).ifPresent(
                user -> productDto.setCreatedById(user.getId()));
        Optional.ofNullable(source.getUpdatedBy()).ifPresent(
                user -> productDto.setUpdatedById(user.getId()));
        return productDto;
    }

}
