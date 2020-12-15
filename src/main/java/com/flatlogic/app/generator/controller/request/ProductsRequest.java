
package com.flatlogic.app.generator.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductsRequest {

    private List<FileRequest> image;
    
    private String title;
    
    private BigDecimal price;
    
    private BigDecimal discount;
    
    private String description;
    
    private List<UUID> categories;
    
    private List<UUID> more_products;
    
    private Integer rating;
    
    private String status;
    

    private String importHash;
}
