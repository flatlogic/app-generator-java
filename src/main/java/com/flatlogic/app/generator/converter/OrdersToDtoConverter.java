
package com.flatlogic.app.generator.converter;


import com.flatlogic.app.generator.dto.ProductsDto;

import com.flatlogic.app.generator.dto.CategoriesDto;

import com.flatlogic.app.generator.dto.OrdersDto;

import com.flatlogic.app.generator.dto.UsersDto;

import com.flatlogic.app.generator.dto.FileDto;
import com.flatlogic.app.generator.entity.OrdersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrdersToDtoConverter implements Converter<Orders, OrdersDto> {


    @Autowired
    private ProductsToDtoConverter productsToDtoConverter;

    @Autowired
    private CategoriesToDtoConverter categoriesToDtoConverter;

    @Autowired
    private UsersToDtoConverter usersToDtoConverter;


    @Autowired
    private FileToDtoConverter fileToDtoConverter;

    @Override
    public OrdersDto convert(final Orders source) {
        final OrdersDto ordersDto = new OrdersDto();

        ordersDto.setId(source.getId());
        ordersDto.setImportHash(source.getImportHash());


        ordersDto.setOrder_date(source.getOrder_date());
    
        Optional.ofNullable(source.getProduct()).ifPresent(products -> {
            ordersDto.setProducts(products.getId());
            ordersDto.setProductsDto(productsToDtoConverter.convert(products));
        });
    
        Optional.ofNullable(source.getUser()).ifPresent(users -> {
            ordersDto.setUsers(users.getId());
            ordersDto.setUsersDto(usersToDtoConverter.convert(users));
        });
    
        ordersDto.setAmount(source.getAmount());
    
        Optional.ofNullable(source.getStatus()).ifPresent(
                status -> ordersDto.setStatus(status));
    

        ordersDto.setCreatedAt(source.getCreatedAt());
        ordersDto.setUpdatedAt(source.getUpdatedAt());
        ordersDto.setDeletedAt(source.getDeletedAt());
        Optional.ofNullable(source.getCreatedBy()).ifPresent(
                user -> ordersDto.setCreatedById(user.getId()));
        Optional.ofNullable(source.getUpdatedBy()).ifPresent(
                user -> ordersDto.setUpdatedById(user.getId()));
        return ordersDto;
    }

    private OrdersDto convertOrdersToDto (final Orders orders) {
        final OrdersDto ordersDto = new OrdersDto();
        ordersDto.setId(orders.getId());


        ordersDto.setOrder_date(orders.getOrder_date());
    
        ordersDto.setAmount(orders.getAmount());
    
        Optional.ofNullable(orders.getStatus()).ifPresent(
                status -> ordersDto.setStatus(status.getStatusValue()));
    

        ordersDto.setImportHash(orders.getImportHash());
        ordersDto.setCreatedAt(orders.getCreatedAt());
        ordersDto.setUpdatedAt(orders.getUpdatedAt());
        ordersDto.setDeletedAt(orders.getDeletedAt());
        Optional.ofNullable(orders.getCreatedBy()).ifPresent(
                user -> ordersDto.setCreatedById(user.getId()));
        Optional.ofNullable(orders.getUpdatedBy()).ifPresent(
                user -> ordersDto.setUpdatedById(user.getId()));
        return ordersDto;
	}
}
