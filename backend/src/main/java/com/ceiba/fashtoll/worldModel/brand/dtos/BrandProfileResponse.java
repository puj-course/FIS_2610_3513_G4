package com.ceiba.fashtoll.worldModel.brand.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandProfileResponse {
    private String name;
    private String email;
    private String pictureUrl;
    private String linkOfficial;
    private Integer followers;
    private Double rating;
    private Boolean isVerified;
}
