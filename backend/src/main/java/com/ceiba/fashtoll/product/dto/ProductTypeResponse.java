package com.ceiba.fashtoll.product.dto;

import com.ceiba.fashtoll.enums.Category;
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
