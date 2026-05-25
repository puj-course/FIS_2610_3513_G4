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
        List<String> tags,
        Integer page,
        Integer size
) {
    public ProductSearchRequest {
        if (page == null) page = 0;
        if (size == null) size = 12;
    }
}
