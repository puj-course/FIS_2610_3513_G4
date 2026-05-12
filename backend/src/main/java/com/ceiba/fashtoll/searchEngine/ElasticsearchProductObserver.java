package com.ceiba.fashtoll.searchEngine;

import com.ceiba.fashtoll.worldModel.product.Observer.EventType;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEvent;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEventPublisher;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ConcreteObserver del patrón Observer (GoF - Comportamental).
 *
 * Reacciona a eventos de dominio sobre productos sincronizando
 * el índice de Elasticsearch: indexa cuando un producto es creado
 * o actualizado, y lo elimina del índice cuando es borrado.
 *
 * Se auto-suscribe al ProductEventPublisher,
 * de forma que el Subject (ProductEventPublisher)
 * y el Client (ProductService) no necesitan conocer la existencia
 * de esta clase ni referenciarla en ningún momento.
 *
 * Rol GoF: ConcreteObserver
 */
@Component
public class ElasticsearchProductObserver /*implements ProductObserver*/ {

    private final ProductSearchService productSearchService;

    @Autowired
    public ElasticsearchProductObserver(ProductSearchService productSearchService,
                                        ProductEventPublisher publisher) {
        this.productSearchService = productSearchService;
        //publisher.subscribe(this);
    }

    /**
     * Reacciona al evento de dominio recibido del Subject.
     *
     * - DELETED: elimina el documento del índice de Elasticsearch.
     * - CREATED / UPDATED: indexa o actualiza el documento en Elasticsearch.
     *
     * @param event el evento publicado por ProductEventPublisher.
     */
    /*
    @Override
    public void onProductEvent(ProductEvent event) {
        if (event.getType() == EventType.DELETED) {
            productSearchService.deleteProduct(event.getProduct().getId());
        } else {
            productSearchService.indexProduct(event.getProduct());
        }
    }
     */
}
