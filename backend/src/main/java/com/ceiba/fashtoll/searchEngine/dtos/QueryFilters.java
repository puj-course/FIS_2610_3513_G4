package com.ceiba.fashtoll.searchEngine.dtos;

import java.util.List;

public record QueryFilters(
        String productType,
        String category,
        String generalFit,
        String gender,
        String color,
        //Boolean available,
        Double minPrice,
        Double maxPrice,
        List<String> tags
) {}
