package com.ceiba.fashtoll.searchEngine;

import com.ceiba.fashtoll.searchEngine.dtos.ProductSearchRequest;
import com.ceiba.fashtoll.searchEngine.dtos.ProductSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/search")
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @Autowired
    public ProductSearchController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @PostMapping("/simple")
    public ProductSearchResponse simpleSearchProducts(@RequestBody ProductSearchRequest request){
        return this.productSearchService.simpleSearch(request);
    }

    @PostMapping
    public ProductSearchResponse filterSearchProducts(@RequestBody ProductSearchRequest request){
        return this.productSearchService.filterSearch(request);
    }
}
