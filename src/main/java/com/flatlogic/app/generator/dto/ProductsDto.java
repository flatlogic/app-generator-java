
package com.flatlogic.app.generator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductsDto extends AbstractDto {


    private List<FileDto> imageDtos = new ArrayList<>();
    
    private String title;
    
    private BigDecimal price;
    
    private BigDecimal discount;
    
    private String description;
    
    private List<CategoriesDto> categories = new ArrayList<>();
    
    private List<ProductsDto> more_products = new ArrayList<>();
    
    private Integer rating;
    
    private String status;
    

    private String importHash;
}
