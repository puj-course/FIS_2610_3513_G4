package com.ceiba.fashtoll.worldModel.brand.dtos;

import com.ceiba.fashtoll.worldModel.review.dto.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private Long id;
    private String name;
    private String pictureURL;
    private String linkOfficial;
    private Integer followers;
    private Double rating;
    private Integer reviewCount;
    private Boolean isVerified;
    private List<ReviewResponse> reviews;
}

