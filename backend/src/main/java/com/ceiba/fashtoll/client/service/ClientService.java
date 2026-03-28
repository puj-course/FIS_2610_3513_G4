package com.ceiba.fashtoll.client.service;

import com.ceiba.fashtoll.client.dto.*;
import com.ceiba.fashtoll.client.entity.Client;
import com.ceiba.fashtoll.client.mapper.ClientMapper;
import com.ceiba.fashtoll.client.repository.ClientRepository;
import com.ceiba.fashtoll.user.entity.User;
import com.ceiba.fashtoll.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<ClientResponse> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ClientResponse getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
        return clientMapper.toResponse(client);
    }

    @Transactional
    public ClientResponse createClient(ClientCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + request.getUserId()));

        Client client = clientMapper.toEntity(request);
        client.setUser(user);

        Client savedClient = clientRepository.save(client);
        return clientMapper.toResponse(savedClient);
    }

    @Transactional
    public ClientResponse updateClient(Long id, ClientUpdateRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));

        clientMapper.updateEntityFromAdmin(request, client);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toResponse(savedClient);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
        clientRepository.delete(client);
    }

    public ClientProfileResponse getProfile(Long userId) {
        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return clientMapper.toProfileResponse(client);
    }

    @Transactional
    public ClientProfileResponse updateProfile(Long userId, ClientProfileUpdateRequest request) {
        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        clientMapper.updateEntityFromProfile(request, client);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toProfileResponse(savedClient);
    }
}
