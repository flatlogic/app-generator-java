
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
public class CategoriesDto extends AbstractDto {


    private String title;
    

    private String importHash;
}
