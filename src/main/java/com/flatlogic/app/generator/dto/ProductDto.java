package com.flatlogic.app.generator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductDto extends AbstractDto {

    private String title;

    private BigDecimal price;

    private BigDecimal discount;

    private String description;

    private Integer rating;

    private String status;

    private String importHash;

    @JsonProperty("categories")
    private List<CategoryDto> categoryDtos = new ArrayList<>();

    @JsonProperty("more_products")
    private List<ProductDto> productDtos = new ArrayList<>();

    @JsonProperty("image")
    private List<FileDto> fileDtos = new ArrayList<>();

}
