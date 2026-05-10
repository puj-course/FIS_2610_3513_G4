package com.ceiba.fashtoll.worldModel.product.dtos;

import com.ceiba.fashtoll.utilities.enums.Color;
import com.ceiba.fashtoll.utilities.enums.Gender;
import com.ceiba.fashtoll.utilities.enums.GeneralFit;
import com.ceiba.fashtoll.worldModel.review.dto.ReviewResponse;
import com.ceiba.fashtoll.worldModel.tag.dto.TagResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Integer reviewCount;
    private String linkProduct;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    private List<String> imageUrls;
    private List<TagResponse> tags;
    private List<ReviewResponse> reviews;
}

