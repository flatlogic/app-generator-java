
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
public class OrdersDto extends AbstractDto {


    private Date order_date;
    
    private ProductsDto product;
    
    private UsersDto user;
    
    private Integer amount;
    
    private String status;
    

    private String importHash;
}
