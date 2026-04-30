package com.ceiba.fashtoll.worldModel.client.dtos;

public record ClientDTO(
        String email,
        String password,
        String role,
        String name
) {}
