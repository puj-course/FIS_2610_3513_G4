package com.ceiba.fashtoll.searchEngine;

import com.ceiba.fashtoll.searchEngine.TemplateMethod.FilterSearchEngine;
import com.ceiba.fashtoll.searchEngine.TemplateMethod.SimpleSearchEngine;
import com.ceiba.fashtoll.searchEngine.dtos.ProductSearchRequest;
import com.ceiba.fashtoll.searchEngine.dtos.ProductSearchResponse;
import com.ceiba.fashtoll.searchEngine.repositories.ProductSearchRepository;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de ProductSearchService")
class ProductSearchServiceTest {

    @Mock
    private ProductSearchRepository productSearchRepository;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SimpleSearchEngine simpleSearchEngine;

    @Mock
    private FilterSearchEngine filterSearchEngine;

    @InjectMocks
    private ProductSearchService productSearchService;

    @Test
    @DisplayName("CP-SRC-01: simpleSearch - Búsqueda simple")
    void simpleSearch_returnsResponse() {
        Product p = new Product();
        p.setId(1L);
        when(simpleSearchEngine.processSimpleQuery("test")).thenReturn(Collections.singletonList(p));

        ProductSearchResponse result = productSearchService.simpleSearch("test");

        assertNotNull(result);
        verify(simpleSearchEngine, times(1)).processSimpleQuery("test");
    }

    @Test
    @DisplayName("CP-SRC-02: filterSearch - Búsqueda con filtros")
    void filterSearch_returnsResponse() {
        ProductSearchRequest req = new ProductSearchRequest("test", null, null, null, null, null, null, null, null);
        Product p = new Product();
        p.setId(1L);
        when(filterSearchEngine.processFilterQuery(req)).thenReturn(Collections.singletonList(p));

        ProductSearchResponse result = productSearchService.filterSearch(req);

        assertNotNull(result);
        verify(filterSearchEngine, times(1)).processFilterQuery(req);
    }

    @Test
    @DisplayName("CP-SRC-03: indexProduct - Indexa producto")
    void indexProduct_indexesSuccessfully() {
        Product p = new Product();
        p.setId(1L);

        productSearchService.indexProduct(p);

        verify(productSearchRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("CP-SRC-04: deleteProduct - Elimina producto del índice")
    void deleteProduct_deletesSuccessfully() {
        productSearchService.deleteProduct(1L);

        verify(productSearchRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("CP-SRC-05: reindexAll - Reindexa todos los productos")
    void reindexAll_reindexesSuccessfully() {
        Product p = new Product();
        p.setId(1L);
        when(productRepository.findAll()).thenReturn(Collections.singletonList(p));

        productSearchService.reindexAll();

        verify(productSearchRepository, times(1)).deleteAll();
        verify(productSearchRepository, times(1)).saveAll(any());
    }
}
