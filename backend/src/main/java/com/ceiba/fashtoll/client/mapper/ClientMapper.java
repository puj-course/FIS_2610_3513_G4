package com.ceiba.fashtoll.client.mapper;

import com.ceiba.fashtoll.client.dto.ClientDTO;
import com.ceiba.fashtoll.client.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientDTO toDTO(Client client) {
        if (client == null) return null;
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setUserId(client.getUser() != null ? client.getUser().getId() : null);
        return dto;
    }

    public Client toEntity(ClientDTO dto) {
        if (dto == null) return null;
        Client client = new Client();
        client.setId(dto.getId());
        client.setName(dto.getName());
        return client;
    }
}
