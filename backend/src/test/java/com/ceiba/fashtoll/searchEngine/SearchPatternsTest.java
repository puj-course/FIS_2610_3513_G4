package com.ceiba.fashtoll.searchEngine;

import com.ceiba.fashtoll.searchEngine.TemplateMethod.ConcreteSearchEngines.FilterSearchEngine;
import com.ceiba.fashtoll.searchEngine.TemplateMethod.ConcreteSearchEngines.SimpleSearchEngine;
import com.ceiba.fashtoll.searchEngine.dtos.ProductSearchRequest;
import com.ceiba.fashtoll.searchEngine.IndexingComponent;
import com.ceiba.fashtoll.searchEngine.entities.SearchToken;
import com.ceiba.fashtoll.searchEngine.RankingComponent;
import com.ceiba.fashtoll.searchEngine.SearchTokenRepository;
import com.ceiba.fashtoll.worldModel.product.Observer.EventType;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEvent;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEventPublisher;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Pruebas unitarias de Motor de Búsqueda e Indexación")
class SearchPatternsTest {

    @Mock
    private SearchTokenRepository searchTokenRepository;
    @Mock(lenient = true)
    private ProductRepository productRepository;
    @Mock
    private ProductEventPublisher publisher;
    @Mock
    private RankingComponent rankingComponent;

    private IndexingComponent indexingComponent;
    private SimpleSearchEngine simpleSearchEngine;
    private FilterSearchEngine filterSearchEngine;

    @BeforeEach
    void setUp() {
        indexingComponent = new IndexingComponent(searchTokenRepository, publisher, productRepository);
        simpleSearchEngine = new SimpleSearchEngine(indexingComponent, rankingComponent, productRepository);
        filterSearchEngine = new FilterSearchEngine(indexingComponent, rankingComponent, productRepository);
    }

    @Test
    @DisplayName("Indexing: Almacenar palabras clave crea nuevos SearchTokens si no existen")
    void indexing_storeInfo_createsNewTokens() {
        when(searchTokenRepository.findByToken("camisa")).thenReturn(Optional.empty());
        when(searchTokenRepository.save(any(SearchToken.class))).thenAnswer(i -> i.getArgument(0));

        Set<SearchToken> result = indexingComponent.storeInfo(Collections.singletonList("camisa"));

        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(t -> t.getToken().equals("camisa")));
        verify(searchTokenRepository).save(any(SearchToken.class));
    }

    @Test
    @DisplayName("Indexing: onProductEvent procesa el producto correctamente")
    void indexing_onProductEvent_processesProduct() {
        Product product = new Product();
        product.setName("Camisa Blanca");
        product.setDescription("Algodon premium");
        ProductEvent event = new ProductEvent(product, EventType.CREATED);

        when(searchTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());
        when(searchTokenRepository.save(any(SearchToken.class))).thenAnswer(i -> i.getArgument(0));

        indexingComponent.onProductEvent(event);

        assertNotNull(product.getTokens());
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("SearchEngine: SimpleSearchEngine ejecuta búsqueda por tokens")
    void simpleSearchEngine_executesSearch() {
        List<Product> expected = Collections.singletonList(new Product());
        when(productRepository.findBySearchTokens(anyList())).thenReturn(expected);

        List<Product> result = simpleSearchEngine.processSimpleQuery("camisa azul");

        assertEquals(expected, result);
        verify(productRepository).findBySearchTokens(anyList());
    }

    @Test
    @DisplayName("SearchEngine: FilterSearchEngine ejecuta búsqueda con filtros (Cobertura)")
    void filterSearchEngine_executesSearchWithFilters() {
        ProductSearchRequest request = new ProductSearchRequest(
                "camisa", "1", "Remera", "SLIM", "MALE", "WHITE", 10.0, 50.0, null);

        filterSearchEngine.processFilterQuery(request);
    }
}
