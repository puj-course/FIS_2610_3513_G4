package com.ceiba.fashtoll.worldModel.client.mapper;

import com.ceiba.fashtoll.worldModel.client.dto.*;
import com.ceiba.fashtoll.worldModel.client.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientResponse toResponse(Client client) {
        if (client == null) return null;
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setName(client.getName());
        response.setUserId(client.getUser() != null ? client.getUser().getId() : null);
        return response;
    }

    public ClientProfileResponse toProfileResponse(Client client) {
        if (client == null) return null;
        ClientProfileResponse response = new ClientProfileResponse();
        response.setName(client.getName());
        response.setEmail(client.getUser() != null ? client.getUser().getEmail() : null);
        return response;
    }

    public Client toEntity(ClientCreateRequest request) {
        if (request == null) return null;
        Client client = new Client();
        client.setName(request.getName());
        return client;
    }

    public void updateEntityFromAdmin(ClientUpdateRequest request, Client client) {
        if (request == null || client == null) return;
        client.setName(request.getName());
    }
    public void updateEntityFromProfile(ClientProfileUpdateRequest request, Client client) {
        if (request == null || client == null) return;
        client.setName(request.getName());
    }
}
