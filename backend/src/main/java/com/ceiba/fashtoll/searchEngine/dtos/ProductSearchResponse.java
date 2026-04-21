package com.ceiba.fashtoll.searchEngine.dtos;

import java.util.List;

public record ProductSearchResponse(
   List<ProductDocument> searchedProducts
) {}