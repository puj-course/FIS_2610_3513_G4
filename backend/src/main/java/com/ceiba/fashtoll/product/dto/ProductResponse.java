package com.ceiba.fashtoll.product.dto;

import com.ceiba.fashtoll.enums.Color;
import com.ceiba.fashtoll.enums.Gender;
import com.ceiba.fashtoll.enums.GeneralFit;
import com.ceiba.fashtoll.tag.dto.TagResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private Long brandId;
    private ProductTypeResponse productType;
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
    private List<String> imageUrls;
    private List<TagResponse> tags;
}
