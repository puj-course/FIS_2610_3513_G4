package com.ceiba.fashtoll.worldModel.product.observer;

import com.ceiba.fashtoll.worldModel.product.entities.Product;
import lombok.Getter;

/**
 * Objeto que encapsula el estado del cambio ocurrido sobre un producto.
 * Es el mensaje que viaja del Subject (ProductEventPublisher) a cada Observer.
 *
 * Patrón Observer (GoF - Comportamental):
 * Este objeto permite que el Observer reciba toda la información necesaria
 * para reaccionar al evento sin necesidad de consultar el estado del Subject.
 */
@Getter
public class ProductEvent {

    private final Product product;
    private final EventType type;

    public ProductEvent(Product product, EventType type) {
        this.product = product;
        this.type = type;
    }
}
