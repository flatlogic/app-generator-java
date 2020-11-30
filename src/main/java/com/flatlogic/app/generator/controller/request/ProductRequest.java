package com.flatlogic.app.generator.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductRequest {

    private String title;

    private BigDecimal price;

    private BigDecimal discount;

    private String description;

    private Integer rating;

    private String status;

    private String importHash;

    @JsonProperty("categories")
    private List<UUID> categoryIds;

    @JsonProperty("more_products")
    private List<UUID> productIds;

    @JsonProperty("image")
    private List<FileRequest> fileRequests;

}
