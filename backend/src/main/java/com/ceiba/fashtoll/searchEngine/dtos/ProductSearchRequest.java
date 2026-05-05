package com.ceiba.fashtoll.searchEngine.dtos;

public record ProductSearchRequest(
        String query,
        String productType,
        String category
) {
    public ProductSearchRequest {
        if (query == null) {
            throw new IllegalArgumentException("Query is required");
        }
        if (productType == null) {
            productType = "";
        }
        if (category == null) {
            category = "";
        }
    }
}
