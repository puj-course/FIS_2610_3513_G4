package com.ceiba.fashtoll.client.service;

import com.ceiba.fashtoll.client.dto.ClientDTO;
import com.ceiba.fashtoll.client.entity.Client;
import com.ceiba.fashtoll.user.entity.User;
import com.ceiba.fashtoll.client.mapper.ClientMapper;
import com.ceiba.fashtoll.client.repository.ClientRepository;
import com.ceiba.fashtoll.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientService(ClientRepository clientRepository, UserRepository userRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.clientMapper = clientMapper;
    }

    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
        return clientMapper.toDTO(client);
    }

    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = clientMapper.toEntity(clientDTO);
        if (clientDTO.getUserId() != null) {
            User user = userRepository.findById(clientDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + clientDTO.getUserId()));
            client.setUser(user);
        }
        Client savedClient = clientRepository.save(client);
        return clientMapper.toDTO(savedClient);
    }

    public ClientDTO updateClient(Long id, ClientDTO updatedClientDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
        client.setName(updatedClientDTO.getName());
        if (updatedClientDTO.getUserId() != null) {
            User user = userRepository.findById(updatedClientDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + updatedClientDTO.getUserId()));
            client.setUser(user);
        }
        Client savedClient = clientRepository.save(client);
        return clientMapper.toDTO(savedClient);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
        clientRepository.delete(client);
    }
}
