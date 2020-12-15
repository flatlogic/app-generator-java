
package com.flatlogic.app.generator.converter;


import com.flatlogic.app.generator.dto.ProductsDto;

import com.flatlogic.app.generator.dto.CategoriesDto;

import com.flatlogic.app.generator.dto.OrdersDto;

import com.flatlogic.app.generator.dto.UsersDto;

import com.flatlogic.app.generator.dto.FileDto;
import com.flatlogic.app.generator.entity.ProductsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductsToDtoConverter implements Converter<Products, ProductsDto> {


    @Autowired
    private CategoriesToDtoConverter categoriesToDtoConverter;

    @Autowired
    private OrdersToDtoConverter ordersToDtoConverter;

    @Autowired
    private UsersToDtoConverter usersToDtoConverter;


    @Autowired
    private FileToDtoConverter fileToDtoConverter;

    @Override
    public ProductsDto convert(final Products source) {
        final ProductsDto productsDto = new ProductsDto();

        productsDto.setId(source.getId());
        productsDto.setImportHash(source.getImportHash());


        Optional.ofNullable(source.getFiles()).ifPresent(files -> {
            final List<FileDto> fileDtos = productsDto.getFileDtos();
            files.forEach(__file -> fileDtos.add(fileToDtoConverter.convert(__file)));
        });
    
        productsDto.setTitle(source.getTitle());
    
        productsDto.setPrice(source.getPrice());
    
        productsDto.setDiscount(source.getDiscount());
    
        productsDto.setDescription(source.getDescription());
    
        Optional.ofNullable(source.getCategories()).ifPresent(categories -> {
            final List<CategoriesDto> categories = products.getCategories();
            categories.forEach(categories -> categories.add(CategoriesToDtoConverter.convert(categories)));
        });
    
        Optional.ofNullable(source.getMore_products()).ifPresent(products -> {
            final List<ProductsDto> productsDtos = productsDto.getMore_productsDtos();
            products.forEach(products -> productsDtos.add(convertProductsToDto(products)));
        });
    
        productsDto.setRating(source.getRating());
    
        Optional.ofNullable(source.getStatus()).ifPresent(
                status -> productsDto.setStatus(status));
    

        productsDto.setCreatedAt(source.getCreatedAt());
        productsDto.setUpdatedAt(source.getUpdatedAt());
        productsDto.setDeletedAt(source.getDeletedAt());
        Optional.ofNullable(source.getCreatedBy()).ifPresent(
                user -> productsDto.setCreatedById(user.getId()));
        Optional.ofNullable(source.getUpdatedBy()).ifPresent(
                user -> productsDto.setUpdatedById(user.getId()));
        return productsDto;
    }

    private ProductsDto convertProductsToDto (final Products products) {
        final ProductsDto productsDto = new ProductsDto();
        productsDto.setId(products.getId());


        productsDto.setTitle(products.getTitle());
    
        productsDto.setPrice(products.getPrice());
    
        productsDto.setDiscount(products.getDiscount());
    
        productsDto.setDescription(products.getDescription());
    
        productsDto.setRating(products.getRating());
    
        Optional.ofNullable(products.getStatus()).ifPresent(
                status -> productsDto.setStatus(status.getStatusValue()));
    

        productsDto.setImportHash(products.getImportHash());
        productsDto.setCreatedAt(products.getCreatedAt());
        productsDto.setUpdatedAt(products.getUpdatedAt());
        productsDto.setDeletedAt(products.getDeletedAt());
        Optional.ofNullable(products.getCreatedBy()).ifPresent(
                user -> productsDto.setCreatedById(user.getId()));
        Optional.ofNullable(products.getUpdatedBy()).ifPresent(
                user -> productsDto.setUpdatedById(user.getId()));
        return productsDto;
	}
}
