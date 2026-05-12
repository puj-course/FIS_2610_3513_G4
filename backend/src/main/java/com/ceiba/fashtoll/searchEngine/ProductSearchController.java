package com.ceiba.fashtoll.searchEngine;

import com.ceiba.fashtoll.searchEngine.dtos.ProductElasticSearchRequest;
import com.ceiba.fashtoll.searchEngine.dtos.ProductElasticSearchResponse;
import com.ceiba.fashtoll.searchEngine.dtos.ProductSearchRequest;
import com.ceiba.fashtoll.searchEngine.dtos.ProductSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/search")
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @Autowired
    public ProductSearchController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @GetMapping("/simple-search")
    public ProductSearchResponse simpleSearchProducts(@RequestBody String query){
        return this.productSearchService.simpleSearch(query);
    }

    @GetMapping
    public ProductSearchResponse filterSearchProducts(@RequestBody ProductSearchRequest request){
        return this.productSearchService.filterSearch(request);
    }

    /**
     * Endpoint de búsqueda pública de productos.
     * Soporta búsqueda por keywords, filtros y paginación.
     * No requiere autenticación.
     */
    /*
    @GetMapping("/elastic-search")
    public ProductElasticSearchResponse searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String productTypeName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String generalFit,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        ProductElasticSearchRequest request = ProductElasticSearchRequest.builder()
                .keyword(keyword)
                .productTypeName(productTypeName)
                .category(category)
                .generalFit(generalFit)
                .gender(gender)
                .color(color)
                .available(available)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .tags(tags)
                .page(page)
                .size(size)
                .build();

        return productSearchService.search(request);
    }
     */

    /**
     * Re-indexa TODOS los products desde PostgreSQL hacia Elasticsearch.
     * Endpoint solo para Admin para setup inicial o re-sincronización.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reindex")
    public ResponseEntity<String> reindexAll() {
        //productSearchService.reindexAll();
        return ResponseEntity.ok("Reindexación completada exitosamente.");
    }

}
