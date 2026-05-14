package com.ceiba.fashtoll.searchEngine;

import com.ceiba.fashtoll.worldModel.product.Observer.EventType;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEvent;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEventPublisher;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de ElasticsearchProductObserver")
class ElasticsearchObserverTest {

    @Mock
    private ProductSearchService productSearchService;
    @Mock
    private ProductEventPublisher publisher;

    private ElasticsearchProductObserver observer;

    @BeforeEach
    void setUp() {
        observer = new ElasticsearchProductObserver(productSearchService, publisher);
    }

    @Test
    @DisplayName("Observer: Al recibir evento CREATED se llama a indexProduct")
    void onProductEvent_Created_callsIndex() {
        Product product = new Product();
        ProductEvent event = new ProductEvent(product, EventType.CREATED);

        observer.onProductEvent(event);

        verify(productSearchService, times(1)).indexProduct(product);
        verify(productSearchService, never()).deleteProduct(any());
    }

    @Test
    @DisplayName("Observer: Al recibir evento DELETED se llama a deleteProduct")
    void onProductEvent_Deleted_callsDelete() {
        Product product = new Product();
        product.setId(10L);
        ProductEvent event = new ProductEvent(product, EventType.DELETED);

        observer.onProductEvent(event);

        verify(productSearchService, times(1)).deleteProduct(10L);
        verify(productSearchService, never()).indexProduct(any());
    }

    @Test
    @DisplayName("Observer: Al recibir evento UPDATED se llama a indexProduct")
    void onProductEvent_Updated_callsIndex() {
        Product product = new Product();
        ProductEvent event = new ProductEvent(product, EventType.UPDATED);

        observer.onProductEvent(event);

        verify(productSearchService, times(1)).indexProduct(product);
    }
}
