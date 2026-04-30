package com.ceiba.fashtoll.searchEngine.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductElasticSearchRequest {
    private String keyword;
    private String productTypeName;
    private String category;
    private String generalFit;
    private String gender;
    private String color;
    private Boolean available;
    private Double minPrice;
    private Double maxPrice;
    private List<String> tags;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 12;
}
