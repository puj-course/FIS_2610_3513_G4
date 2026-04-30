package com.ceiba.fashtoll.exceptionHandling.exceptionTypes;

public class DuplicatedResourceException extends RuntimeException {
    public DuplicatedResourceException(String resourceName, String whereResource) {
        super(String.format("El recurso '%s' parte de '%s' ya existe y no puede tener duplicados", resourceName, whereResource));
    }
}
