package com.flatlogic.app.generator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class OrderDto extends AbstractDto {

    @JsonProperty("order_date")
    private Date orderDate;

    private Integer amount;

    private String status;

    private String importHash;

    private UUID productId;

    private UUID userId;

    @JsonProperty("product")
    private ProductDto productDto;

    @JsonProperty("user")
    private UserDto userDto;

}
