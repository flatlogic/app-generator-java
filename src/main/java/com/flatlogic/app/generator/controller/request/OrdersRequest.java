
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
public class OrdersRequest {

    private Date order_date;
    
    private UUID product;
    
    private UUID user;
    
    private Integer amount;
    
    private String status;
    

    private String importHash;
}
