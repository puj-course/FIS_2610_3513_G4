package com.ceiba.fashtoll.searchEngine;

import com.ceiba.fashtoll.searchEngine.TemplateMethod.ConcreteSearchEngines.FilterSearchEngine;
import com.ceiba.fashtoll.searchEngine.TemplateMethod.ConcreteSearchEngines.SimpleSearchEngine;
import com.ceiba.fashtoll.searchEngine.dtos.ProductSearchRequest;
import com.ceiba.fashtoll.searchEngine.dtos.ProductSearchResponse;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ceiba.fashtoll.searchEngine.dtos.ProductDocument;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de ProductSearchService")
class ProductSearchServiceTest {

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
        ProductSearchRequest test = new ProductSearchRequest(
                "test",
                "",
                "",
                "",
                "",
                "",
                0.0,
                0.0,
                null,
                0,
                0
        );
        when(simpleSearchEngine.processSimpleQuery(test)).thenReturn((Page<Product>) Collections.singletonList(p));

        ProductSearchResponse result = productSearchService.simpleSearch(test);

        assertNotNull(result);
        verify(simpleSearchEngine, times(1)).processSimpleQuery(test);
    }

    @Test
    @DisplayName("CP-SRC-02: filterSearch - Búsqueda con filtros")
    void filterSearch_returnsResponse() {
        ProductSearchRequest req = new ProductSearchRequest(
                "test",
                "",
                "",
                "",
                "",
                "",
                0.0,
                0.0,
                null,
                0,
                0
        );

        Product p = new Product();
        p.setId(1L);
        when(filterSearchEngine.processFilterQuery(req)).thenReturn((Page<Product>) Collections.singletonList(p));

        ProductSearchResponse result = productSearchService.filterSearch(req);

        assertNotNull(result);
        verify(filterSearchEngine, times(1)).processFilterQuery(req);
    }

    /** Se comento las pruebas que se hacian a elastic search, pues ya no se usa
    /*
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

    @Test
    @DisplayName("CP-SRC-06: search - Búsqueda compleja con filtros en Elasticsearch")
    void search_withMultipleFilters_returnsResponse() {
        // Arrange
        ProductElasticSearchRequest req = new ProductElasticSearchRequest();
        req.setKeyword("camisa");
        req.setProductTypeName("Remera");
        req.setCategory("ROPA");
        req.setGeneralFit("SLIM");
        req.setGender("MALE");
        req.setColor("WHITE");
        req.setAvailable(true);
        req.setMinPrice(10.0);
        req.setMaxPrice(100.0);
        req.setTags(List.of("tag1", "tag2"));
        req.setPage(0);
        req.setSize(10);

        SearchHits<ProductDocument> searchHits = mock(SearchHits.class);
        when(searchHits.getSearchHits()).thenReturn(Collections.emptyList());
        when(searchHits.getTotalHits()).thenReturn(0L);
        when(elasticsearchOperations.search(any(NativeQuery.class), eq(ProductDocument.class))).thenReturn(searchHits);

        // Act
        ProductElasticSearchResponse response = productSearchService.search(req);

        // Assert
        assertNotNull(response);
        verify(elasticsearchOperations).search(any(NativeQuery.class), eq(ProductDocument.class));
    }

    @Test
    @DisplayName("CP-SRC-07: mapToDocument - Cobertura de mapeo con campos nulos")
    void mapToDocument_handlesNulls() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test");

        productSearchService.indexProduct(product);

        verify(productSearchRepository).save(any(ProductDocument.class));
    }
    */
}
