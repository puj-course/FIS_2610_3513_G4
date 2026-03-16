package com.ceiba.fashtoll.client.controller;

import com.ceiba.fashtoll.client.dto.ClientDTO;
import com.ceiba.fashtoll.client.dto.ClientProfileDTO;
import com.ceiba.fashtoll.client.service.ClientService;
import com.ceiba.fashtoll.user.dto.PasswordChangeRequestDTO;
import com.ceiba.fashtoll.user.entity.User;
import com.ceiba.fashtoll.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;

    @Autowired
    public ClientController(ClientService clientService, UserService userService) {
        this.clientService = clientService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ClientProfileDTO getProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return clientService.getProfile(user.getId());
    }

    @PutMapping("/profile")
    public ClientProfileDTO updateProfile(Authentication authentication, @RequestBody ClientProfileDTO profileDTO) {
        User user = (User) authentication.getPrincipal();
        return clientService.updateProfile(user.getId(), profileDTO);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(Authentication authentication, @RequestBody PasswordChangeRequestDTO request) {
        User user = (User) authentication.getPrincipal();
        userService.changePassword(user.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClientDTO getClientById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        ClientDTO newClient = clientService.createClient(clientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClientDTO updateClient(@PathVariable Long id, @RequestBody ClientDTO updatedClientDTO) {
        return clientService.updateClient(id, updatedClientDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
