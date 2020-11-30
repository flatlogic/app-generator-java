package com.flatlogic.app.generator.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetModelAttribute {

    private String filter;

    private Integer limit;

    private Integer offset;

    private String orderBy;

}
