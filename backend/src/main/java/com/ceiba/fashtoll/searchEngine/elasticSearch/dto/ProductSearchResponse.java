package com.ceiba.fashtoll.searchEngine.elasticSearch.dto;

import com.ceiba.fashtoll.searchEngine.elasticSearch.document.ProductDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchResponse {
    private List<ProductDocument> products;
    private int currentPage;
    private int totalPages;
    private long totalResults;
    private int pageSize;
}
