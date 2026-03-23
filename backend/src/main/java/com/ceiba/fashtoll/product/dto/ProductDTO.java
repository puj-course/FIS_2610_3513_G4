package com.ceiba.fashtoll.product.dto;

import com.ceiba.fashtoll.enums.Color;
import com.ceiba.fashtoll.enums.Gender;
import com.ceiba.fashtoll.enums.GeneralFit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private List<String> imageUrls;
    private Set<Long> tagIds;
}
