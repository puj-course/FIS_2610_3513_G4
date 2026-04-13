package com.ceiba.fashtoll.exceptionHandling.exceptionTypes;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        // Esto generará un mensaje como: "ProductType no fue encontrado con id : 6"
        super(String.format("El recurso '%s' con %s: '%s' no fue encontrado", resourceName, fieldName, fieldValue));
    }
}
