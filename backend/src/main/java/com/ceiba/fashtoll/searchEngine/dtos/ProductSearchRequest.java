package com.ceiba.fashtoll.searchEngine.dtos;

import java.util.ArrayList;
import java.util.List;

public record ProductSearchRequest(
        String query,
        String productType,
        String category,
        String generalFit,
        String gender,
        String color,
        Double minPrice,
        Double maxPrice,
        List<String> tags
) {
    public ProductSearchRequest {
        if (query == null) throw new IllegalArgumentException("Query is required");
        if (query.isEmpty()) throw new IllegalArgumentException("Query is required");
        if (productType == null) productType = "";
        if (category == null) category = "";
        if (generalFit == null) generalFit = "";
        if (gender == null) gender = "";
        if (color == null) color = "";
        if (minPrice == null) minPrice = -1.0;
        if (maxPrice == null) maxPrice = -1.0;
        if (tags == null) tags = new ArrayList<>();
    }
}
