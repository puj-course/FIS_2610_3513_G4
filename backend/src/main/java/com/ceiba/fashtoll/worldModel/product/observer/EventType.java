package com.ceiba.fashtoll.worldModel.product.observer;

/**
 * Enumeración que representa los tipos de eventos de dominio
 * que puede publicar ProductService sobre un producto.
 *
 * Patrón Observer (GoF - Comportamental): parte del objeto-evento
 * que transporta el estado del cambio entre Subject y Observers.
 */
public enum EventType {
    CREATED,
    UPDATED,
    DELETED
}
