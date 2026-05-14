package com.ceiba.fashtoll.worldModel.brand;

import com.ceiba.fashtoll.worldModel.brand.dtos.*;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductC_U_Request;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductResponse;
import com.ceiba.fashtoll.worldModel.review.dto.ReviewResponse;
import com.ceiba.fashtoll.worldModel.user.dtos.PasswordChangeRequestDTO;
import com.ceiba.fashtoll.worldModel.user.User;
import com.ceiba.fashtoll.worldModel.client.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService brandService;
    private final ClientService clientService;

    @Autowired
    public BrandController(BrandService brandService, ClientService clientService) {
        this.brandService = brandService;
        this.clientService = clientService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<BrandResponse> getAllBrands() {
        return brandService.getAllBrands();
    }

    @GetMapping("/public")
    public List<BrandPublicResponse> getAllPublicBrands() {
        return brandService.getAllPublicBrands();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BrandResponse getBrandById(@PathVariable Long id) {
        return brandService.getBrandById(id);
    }

    @GetMapping("/public/{id}")
    public BrandPublicResponse getPublicBrandById(@PathVariable Long id) {
        return brandService.getPublicBrandById(id);
    }

    @GetMapping("/public/{id}/reviews")
    public ResponseEntity<List<ReviewResponse>> getPublicBrandReviews(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getReviewsForBrand(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BrandResponse> createBrand(@Valid @RequestBody BrandCreateRequest request) {
        BrandResponse newBrand = brandService.createBrand(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBrand);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BrandResponse updateBrandAdmin(@PathVariable Long id, @Valid @RequestBody BrandAdminUpdateRequest request) {
        return brandService.updateBrandAdmin(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('BRAND','ADMIN')")
    @GetMapping("/profile")
    public BrandProfileResponse getProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return brandService.getProfile(user.getId());
    }

    @PreAuthorize("hasAnyRole('BRAND','ADMIN')")
    @PutMapping("/profile")
    public BrandProfileResponse updateProfile(Authentication authentication,
                                              @Valid @RequestBody BrandProfileUpdateRequest request) {
        User user = (User) authentication.getPrincipal();
        return brandService.updateProfile(user.getId(), request);
    }

    @PreAuthorize("hasAnyRole('BRAND','ADMIN')")
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(Authentication authentication,
                                               @Valid @RequestBody PasswordChangeRequestDTO request) {
        if (this.brandService.changePassword(authentication, request)){
            return ResponseEntity.noContent().build();
        }

        return null;
    }

    @PreAuthorize("hasAnyRole('BRAND','ADMIN')")
    @GetMapping("/my-products")
    public List<ProductResponse> getMyProducts(Authentication authentication) {
        return this.brandService.getMyProducts(authentication);
    }

    @PreAuthorize("hasAnyRole('BRAND','ADMIN')")
    @GetMapping("/my-products/{id}")
    public ProductResponse getMyProduct(Authentication authentication, @PathVariable Long id) {
        return this.brandService.getMyProduct(authentication, id);
    }

    @PreAuthorize("hasAnyRole('BRAND','ADMIN')")
    @PostMapping("/my-products/simple")
    public ResponseEntity<ProductResponse> createMySimpleProduct(Authentication authentication,
                                                           @Valid @RequestBody ProductC_U_Request request) {
        return this.brandService.createMySimpleProduct(authentication, request);
    }

    @PreAuthorize("hasAnyRole('BRAND','ADMIN')")
    @PostMapping("/my-products")
    public ResponseEntity<ProductResponse> createMyCompleteProduct(Authentication authentication, @Valid @RequestBody ProductC_U_Request request) {
        return this.brandService.createMyCompleteProduct(authentication, request);
    }

    @PreAuthorize("hasAnyRole('BRAND','ADMIN')")
    @PutMapping("/my-products/simple/{id}")
    public ProductResponse updateMySimpleProduct(Authentication authentication, @PathVariable Long id,
                                           @Valid @RequestBody ProductC_U_Request request) {
        return this.brandService.updateMySimpleProduct(authentication, id, request);
    }

    @PreAuthorize("hasAnyRole('BRAND','ADMIN')")
    @PutMapping("/my-products/{id}")
    public ProductResponse updateMyCompleteProduct(Authentication authentication, @PathVariable Long id,
                                           @Valid @RequestBody ProductC_U_Request request) {
        return this.brandService.updateMyCompleteProduct(authentication, id, request);
    }

    @PreAuthorize("hasAnyRole('BRAND','ADMIN')")
    @DeleteMapping("/my-products/{id}")
    public ResponseEntity<Void> deleteMyProduct(Authentication authentication, @PathVariable Long id) {
        return this.brandService.deleteMyProduct(authentication, id);
    }
}

