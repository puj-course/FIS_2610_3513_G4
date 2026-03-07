package com.ceiba.fashtoll.dto;

import com.ceiba.fashtoll.enums.Color;
import com.ceiba.fashtoll.enums.Gender;
import com.ceiba.fashtoll.enums.GeneralFit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private GeneralFit generalFit;
    private Gender gender;
    private Color color;
    private Boolean available;
    private Double rating;
    private String linkProduct;
    private LocalDateTime createdAt;
    private Long brandId;
    private Long productTypeId;
}
