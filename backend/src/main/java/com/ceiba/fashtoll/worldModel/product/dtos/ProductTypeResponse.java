package com.ceiba.fashtoll.worldModel.product.dtos;

import com.ceiba.fashtoll.utilities.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductTypeResponse {
    private Long id;
    private String name;
    private Category category;
}
