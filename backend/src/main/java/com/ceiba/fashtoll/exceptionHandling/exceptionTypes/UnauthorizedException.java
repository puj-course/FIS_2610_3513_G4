package com.ceiba.fashtoll.exceptionHandling.exceptionTypes;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String operation, String resourceName) {
        super(String.format("No se permite la operacion '%s' en el recurso '%s'", operation, resourceName));
    }
}
