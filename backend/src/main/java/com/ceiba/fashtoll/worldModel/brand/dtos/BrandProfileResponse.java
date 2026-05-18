package com.ceiba.fashtoll.worldModel.brand.dtos;

import com.ceiba.fashtoll.worldModel.review.dto.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandProfileResponse {
    private String name;
    private String email;
    private String pictureURL;
    private String linkOfficial;
    private Integer followers;
    private Double rating;
    private Integer reviewCount;
    private Boolean isVerified;
    private String phoneNumber;
    private List<ReviewResponse> reviews;
}

