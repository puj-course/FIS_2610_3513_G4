package com.ceiba.fashtoll.searchEngine.dtos;

import com.ceiba.fashtoll.searchEngine.ProductDocument;

import java.util.List;

public record ProductSearchResponse(
   List<ProductDocument> searchedProducts
) {}