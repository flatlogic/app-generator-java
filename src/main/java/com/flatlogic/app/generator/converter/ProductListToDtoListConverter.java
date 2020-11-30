package com.flatlogic.app.generator.converter;

import com.flatlogic.app.generator.dto.ProductDto;
import com.flatlogic.app.generator.entity.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProductListToDtoListConverter implements Converter<List<Product>, List<ProductDto>> {

    @Override
    public List<ProductDto> convert(List<Product> source) {
        final List<ProductDto> productDtos = new ArrayList<>();
        source.forEach(product -> {
            final ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setTitle(product.getTitle());
            productDto.setPrice(product.getPrice());
            productDto.setDiscount(product.getDiscount());
            productDto.setDescription(product.getDescription());
            productDto.setRating(product.getRating());
            Optional.ofNullable(product.getStatus()).ifPresent(
                    status -> productDto.setStatus(status.getStatusValue()));
            productDto.setImportHash(product.getImportHash());
            productDto.setCreatedAt(product.getCreatedAt());
            productDto.setUpdatedAt(product.getUpdatedAt());
            productDto.setDeletedAt(product.getDeletedAt());
            Optional.ofNullable(product.getCreatedBy()).ifPresent(
                    user -> productDto.setCreatedById(user.getId()));
            Optional.ofNullable(product.getUpdatedBy()).ifPresent(
                    user -> productDto.setUpdatedById(user.getId()));
            productDtos.add(productDto);
        });
        return productDtos;
    }

}
