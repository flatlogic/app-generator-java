package com.flatlogic.app.generator.controller.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RowsData<R> {

    List<R> rows;

    int count;

}
