package com.ceiba.fashtoll.worldModel.product.observer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject (Observable) del patrón Observer (GoF - Comportamental).
 *
 * Mantiene la lista de observers suscritos y los notifica en orden
 * cuando ocurre un evento de dominio sobre un producto.
 *
 * Rol GoF: Subject / Observable / Publisher
 */
@Component
public class ProductEventPublisher {

    private final List<ProductObserver> observers = new ArrayList<>();

    /**
     * Suscribe un observer. A partir de este momento será notificado
     * de todos los eventos futuros que publique este Subject.
     *
     * @param observer el objeto que desea observar los cambios de producto.
     */
    public void subscribe(ProductObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Cancela la suscripción de un observer.
     * Desde este momento dejará de recibir notificaciones.
     *
     * @param observer el objeto que desea dejar de observar.
     */
    public void unsubscribe(ProductObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica a todos los observers suscritos sobre un evento de producto.
     * El Subject no sabe quiénes son los observers ni qué harán con el evento;
     * solo garantiza que todos los suscritos serán invocados.
     *
     * @param event el evento que describe el cambio ocurrido sobre el producto.
     */
    public void notify(ProductEvent event) {
        for (ProductObserver observer : observers) {
            observer.onProductEvent(event);
        }
    }

    /**
     * Retorna la cantidad de observers actualmente suscritos.
     *
     * @return número de observers suscritos.
     */
    public int getObserverCount() {
        return observers.size();
    }
}
