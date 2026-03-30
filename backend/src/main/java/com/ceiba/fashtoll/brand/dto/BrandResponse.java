package com.ceiba.fashtoll.brand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private Long id;
    private String name;
    private String pictureUrl;
    private String linkOfficial;
    private Integer followers;
    private Double rating;
    private Boolean isVerified;
    private Long userId;
}
