package com.ceiba.fashtoll.worldModel.product.observer;

/**
 * Interfaz Observer del patrón GoF (Comportamental).
 *
 * Define el contrato que debe cumplir cualquier clase que quiera
 * reaccionar a cambios en el ciclo de vida de un producto.
 *
 * Rol GoF: Observer / Listener
 */
public interface ProductObserver {

    /**
     * Invocado por el Subject (ProductEventPublisher) cuando ocurre
     * un cambio de estado sobre un producto.
     *
     * @param event objeto que describe qué producto cambió y de qué manera.
     */
    void onProductEvent(ProductEvent event);
}
