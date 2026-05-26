package com.ceiba.fashtoll.worldModel.client;

import com.ceiba.fashtoll.worldModel.client.dtos.*;
import com.ceiba.fashtoll.worldModel.user.dtos.PasswordChangeRequestDTO;
import com.ceiba.fashtoll.worldModel.user.User;
import com.ceiba.fashtoll.worldModel.brand.dtos.BrandPublicResponse;
import com.ceiba.fashtoll.worldModel.review.dto.ReviewRequest;
import com.ceiba.fashtoll.worldModel.review.dto.ReviewResponse;
import com.ceiba.fashtoll.worldModel.wishlist.dtos.WishlistRequest;
import com.ceiba.fashtoll.worldModel.wishlist.dtos.WishlistResponse;
import com.ceiba.fashtoll.worldModel.wishlist.dtos.WishlistDetailsResponse;
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

    // ==================== PERFIL ====================

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

    // ==================== SEGUIMIENTO DE MARCAS ====================

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

    // ==================== LISTAS DE DESEOS ====================

    @GetMapping("/profile/wishlists")
    public ResponseEntity<List<WishlistResponse>> getWishlists(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(clientService.getWishlists(user.getId()));
    }

    @GetMapping("/profile/wishlists/default")
    public ResponseEntity<WishlistDetailsResponse> getDefaultWishlist(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(clientService.getDefaultWishlist(user.getId()));
    }

    @GetMapping("/profile/wishlists/{wishlistId}")
    public ResponseEntity<WishlistDetailsResponse> getWishlist(Authentication authentication,
            @PathVariable Long wishlistId) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(clientService.getWishlist(user.getId(), wishlistId));
    }

    @PostMapping("/profile/wishlists")
    public ResponseEntity<WishlistResponse> createWishlist(Authentication authentication,
            @Valid @RequestBody WishlistRequest request) {
        User user = (User) authentication.getPrincipal();
        WishlistResponse response = clientService.createWishlist(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/profile/wishlists/{wishlistId}")
    public ResponseEntity<WishlistResponse> updateWishlist(Authentication authentication, @PathVariable Long wishlistId,
            @Valid @RequestBody WishlistRequest request) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(clientService.updateWishlist(user.getId(), wishlistId, request));
    }

    @DeleteMapping("/profile/wishlists/{wishlistId}")
    public ResponseEntity<Void> deleteWishlist(Authentication authentication, @PathVariable Long wishlistId) {
        User user = (User) authentication.getPrincipal();
        clientService.deleteWishlist(user.getId(), wishlistId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/profile/wishlists/default/products/{productId}")
    public ResponseEntity<Void> addToDefaultWishlist(Authentication authentication, @PathVariable Long productId) {
        User user = (User) authentication.getPrincipal();
        clientService.addToDefaultWishlist(user.getId(), productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/profile/wishlists/default/products/{productId}")
    public ResponseEntity<Void> removeFromDefaultWishlist(Authentication authentication, @PathVariable Long productId) {
        User user = (User) authentication.getPrincipal();
        clientService.removeFromDefaultWishlist(user.getId(), productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/profile/wishlists/{wishlistId}/products/{productId}")
    public ResponseEntity<Void> addToWishlist(Authentication authentication, @PathVariable Long wishlistId,
            @PathVariable Long productId) {
        User user = (User) authentication.getPrincipal();
        clientService.addToWishlist(user.getId(), wishlistId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/profile/wishlists/{wishlistId}/products/{productId}")
    public ResponseEntity<Void> removeFromWishlist(Authentication authentication, @PathVariable Long wishlistId,
            @PathVariable Long productId) {
        User user = (User) authentication.getPrincipal();
        clientService.removeFromWishlist(user.getId(), wishlistId, productId);
        return ResponseEntity.noContent().build();
    }

    // ==================== RESEÑAS DE MARCAS ====================

    @GetMapping("/profile/reviews/brands")
    public ResponseEntity<List<ReviewResponse>> getMyBrandReviews(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(clientService.getBrandReviews(user.getId()));
    }

    @GetMapping("/profile/reviews/brands/{brandId}")
    public ResponseEntity<ReviewResponse> getMyBrandReview(Authentication authentication,
            @PathVariable Long brandId) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(clientService.getBrandReview(user.getId(), brandId));
    }

    @PostMapping("/profile/reviews/brands/{brandId}")
    public ResponseEntity<ReviewResponse> postBrandReview(Authentication authentication,
            @PathVariable Long brandId, @Valid @RequestBody ReviewRequest request) {
        User user = (User) authentication.getPrincipal();
        ReviewResponse response = clientService.postBrandReview(user.getId(), brandId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/profile/reviews/brands/{brandId}")
    public ResponseEntity<ReviewResponse> updateBrandReview(Authentication authentication,
            @PathVariable Long brandId, @Valid @RequestBody ReviewRequest request) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(clientService.updateBrandReview(user.getId(), brandId, request));
    }

    @DeleteMapping("/profile/reviews/brands/{brandId}")
    public ResponseEntity<Void> deleteBrandReview(Authentication authentication, @PathVariable Long brandId) {
        User user = (User) authentication.getPrincipal();
        clientService.deleteBrandReview(user.getId(), brandId);
        return ResponseEntity.noContent().build();
    }

    // ==================== RESEÑAS DE PRODUCTOS ====================

    @GetMapping("/profile/reviews/products")
    public ResponseEntity<List<ReviewResponse>> getMyProductReviews(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(clientService.getProductReviews(user.getId()));
    }

    @GetMapping("/profile/reviews/products/{productId}")
    public ResponseEntity<ReviewResponse> getMyProductReview(Authentication authentication,
            @PathVariable Long productId) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(clientService.getProductReview(user.getId(), productId));
    }

    @PostMapping("/profile/reviews/products/{productId}")
    public ResponseEntity<ReviewResponse> postProductReview(Authentication authentication,
            @PathVariable Long productId, @Valid @RequestBody ReviewRequest request) {
        User user = (User) authentication.getPrincipal();
        ReviewResponse response = clientService.postProductReview(user.getId(), productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/profile/reviews/products/{productId}")
    public ResponseEntity<ReviewResponse> updateProductReview(Authentication authentication,
            @PathVariable Long productId, @Valid @RequestBody ReviewRequest request) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(clientService.updateProductReview(user.getId(), productId, request));
    }

    @DeleteMapping("/profile/reviews/products/{productId}")
    public ResponseEntity<Void> deleteProductReview(Authentication authentication, @PathVariable Long productId) {
        User user = (User) authentication.getPrincipal();
        clientService.deleteProductReview(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }

    // ==================== ADMIN ====================

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
        // REVISAR
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
