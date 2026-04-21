package com.ceiba.fashtoll.worldModel.client;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.security.auth.AuthService;
import com.ceiba.fashtoll.security.auth.dtos.RegisterRequest;
import com.ceiba.fashtoll.utilities.enums.Role;
import com.ceiba.fashtoll.worldModel.brand.dtos.BrandDTO;
import com.ceiba.fashtoll.worldModel.client.dtos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final AuthService authService;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper, AuthService authService) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.authService = authService;
    }

    public List<ClientResponse> getAllClients() {
        this.logger.info("Se devolvieron todos los clientes");

        return clientRepository.findAll().stream()
                .map(clientMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ClientResponse getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", id));

        this.logger.info("Se devolvio el cliente '" + client.getName() + "' con id: " + id);

        return clientMapper.toResponse(client);
    }

    @Transactional
    public ClientResponse createClient(ClientCreateRequest request) {
        Client client = clientMapper.toEntity(request);
        Client savedClient = clientRepository.save(client);

        this.logger.info("Se creo el cliente '" + savedClient.getName() + "' con id: " + savedClient.getId());

        return clientMapper.toResponse(savedClient);
    }

    @Transactional
    public ClientResponse updateClient(Long id, ClientUpdateRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", id));

        clientMapper.updateEntityFromAdmin(request, client);
        Client savedClient = clientRepository.save(client);

        this.logger.info("Se actualizo el cliente '" + savedClient.getName() + "' con id: " + id);

        return clientMapper.toResponse(savedClient);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", id));
        clientRepository.delete(client);

        this.logger.info("Se elimino el cliente '" + client.getName() + "' con id: " + id);
    }

    public ClientProfileResponse getProfile(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", id));

        this.logger.info("Se devolvio el perfil del cliente '" + client.getName() + "' con id: " + id);

        return clientMapper.toProfileResponse(client);
    }

    @Transactional
    public ClientProfileResponse updateProfile(Long id, ClientProfileUpdateRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", id));

        clientMapper.updateEntityFromProfile(request, client);
        Client savedClient = clientRepository.save(client);

        this.logger.info("Se actualizo el perfil del cliente '" + savedClient.getName() + "' con id: " + id);

        return clientMapper.toProfileResponse(savedClient);
    }

    public void injectClientsFromJSON(List<ClientDTO>  clientDTOs) {
        for (ClientDTO clientDTO : clientDTOs) {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(clientDTO.email());
            registerRequest.setPassword(clientDTO.password());
            registerRequest.setRole(Role.categorize(clientDTO.role()));
            registerRequest.setName(clientDTO.name());

            this.authService.clientRegister(registerRequest);
        }
    }
}
