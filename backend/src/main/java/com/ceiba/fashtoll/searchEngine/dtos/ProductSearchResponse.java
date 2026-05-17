package com.ceiba.fashtoll.searchEngine.dtos;

import java.util.List;

public record ProductSearchResponse(
   List<ProductDocument> searchedProducts,
   int currentPage,         //Page<T> p.getNumber()
   int totalPages,          //Page<T> p.getTotalPages()
   long totalResults,       //Page<T> p.getTotalElements()
   int pageSize         //Page<T> p.getSize()
) {}