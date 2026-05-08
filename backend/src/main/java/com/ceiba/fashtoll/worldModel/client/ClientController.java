package com.ceiba.fashtoll.worldModel.client;

import com.ceiba.fashtoll.worldModel.client.dtos.*;
import com.ceiba.fashtoll.worldModel.user.dtos.PasswordChangeRequestDTO;
import com.ceiba.fashtoll.worldModel.user.User;
import com.ceiba.fashtoll.worldModel.brand.dtos.BrandPublicResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/profile")
    public ClientProfileResponse getProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return clientService.getProfile(user.getId());
    }

    @PutMapping("/profile")
    public ClientProfileResponse updateProfile(Authentication authentication,
                                               @Valid @RequestBody ClientProfileUpdateRequest request) {
        User user = (User) authentication.getPrincipal();
        return clientService.updateProfile(user.getId(), request);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(Authentication authentication,
                                               @Valid @RequestBody PasswordChangeRequestDTO request) {
        return this.clientService.changePassword(authentication, request);
    }

    @PostMapping("/profile/following/{brandId}")
    public ResponseEntity<Void> followBrand(Authentication authentication, @PathVariable Long brandId) {
        User user = (User) authentication.getPrincipal();
        clientService.followBrand(user.getId(), brandId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/profile/following/{brandId}")
    public ResponseEntity<Void> unfollowBrand(Authentication authentication, @PathVariable Long brandId) {
        User user = (User) authentication.getPrincipal();
        clientService.unfollowBrand(user.getId(), brandId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile/following")
    public ResponseEntity<List<BrandPublicResponse>> getFollowedBrands(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<BrandPublicResponse> followedBrands = clientService.getFollowedBrands(user.getId());
        return ResponseEntity.ok(followedBrands);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClientResponse> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClientResponse getClientById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientCreateRequest request) {
        //REVISAR
        ClientResponse newClient = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClientResponse updateClient(@PathVariable Long id, @Valid @RequestBody ClientUpdateRequest request) {
        return clientService.updateClient(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
