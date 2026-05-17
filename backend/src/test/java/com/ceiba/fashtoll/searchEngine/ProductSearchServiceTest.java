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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        ProductSearchRequest req = new ProductSearchRequest("test", null, null, null, null, null, null, null, null, 0, 10);
        Product p = new Product();
        p.setId(1L);
        Page<Product> page = new PageImpl<>(Collections.singletonList(p));
        when(simpleSearchEngine.processSimpleQuery(req)).thenReturn(page);

        ProductSearchResponse result = productSearchService.simpleSearch(req);

        assertNotNull(result);
        verify(simpleSearchEngine, times(1)).processSimpleQuery(req);
    }

    @Test
    @DisplayName("CP-SRC-02: filterSearch - Búsqueda con filtros")
    void filterSearch_returnsResponse() {
        ProductSearchRequest req = new ProductSearchRequest("test", null, null, null, null, null, null, null, null, 0, 10);
        Product p = new Product();
        p.setId(1L);
        Page<Product> page = new PageImpl<>(Collections.singletonList(p));
        when(filterSearchEngine.processFilterQuery(req)).thenReturn(page);

        ProductSearchResponse result = productSearchService.filterSearch(req);

        assertNotNull(result);
        verify(filterSearchEngine, times(1)).processFilterQuery(req);
    }
}
