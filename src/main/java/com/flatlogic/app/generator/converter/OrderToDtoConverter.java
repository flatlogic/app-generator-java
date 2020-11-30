package com.flatlogic.app.generator.converter;

import com.flatlogic.app.generator.dto.OrderDto;
import com.flatlogic.app.generator.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderToDtoConverter implements Converter<Order, OrderDto> {

    @Autowired
    private ProductToDtoConverter productToDtoConverter;

    @Autowired
    private UserToDtoConverter userToDtoConverter;

    @Override
    public OrderDto convert(final Order source) {
        final OrderDto orderDto = new OrderDto();
        orderDto.setId(source.getId());
        orderDto.setOrderDate(source.getOrderDate());
        orderDto.setAmount(source.getAmount());
        Optional.ofNullable(source.getStatus()).ifPresent(
                status -> orderDto.setStatus(status.getStatusValue()));
        orderDto.setImportHash(source.getImportHash());
        Optional.ofNullable(source.getProduct()).ifPresent(product -> {
            orderDto.setProductId(product.getId());
            orderDto.setProductDto(productToDtoConverter.convert(product));
        });
        Optional.ofNullable(source.getUser()).ifPresent(user -> {
            orderDto.setUserId(user.getId());
            orderDto.setUserDto(userToDtoConverter.convert(user));
        });
        orderDto.setCreatedAt(source.getCreatedAt());
        orderDto.setUpdatedAt(source.getUpdatedAt());
        orderDto.setDeletedAt(source.getDeletedAt());
        Optional.ofNullable(source.getCreatedBy()).ifPresent(
                user -> orderDto.setCreatedById(user.getId()));
        Optional.ofNullable(source.getUpdatedBy()).ifPresent(
                user -> orderDto.setUpdatedById(user.getId()));
        return orderDto;
    }

}
