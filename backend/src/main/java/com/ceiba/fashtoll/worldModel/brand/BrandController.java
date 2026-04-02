package com.ceiba.fashtoll.worldModel.brand;

import com.ceiba.fashtoll.worldModel.brand.dtos.*;
import com.ceiba.fashtoll.worldModel.product.dto.ProductCreateRequest;
import com.ceiba.fashtoll.worldModel.product.dto.ProductResponse;
import com.ceiba.fashtoll.worldModel.product.dto.ProductUpdateRequest;
import com.ceiba.fashtoll.worldModel.product.service.ProductService;
import com.ceiba.fashtoll.worldModel.user.dtos.PasswordChangeRequestDTO;
import com.ceiba.fashtoll.worldModel.user.User;
import com.ceiba.fashtoll.worldModel.user.UserService;
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
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public BrandController(BrandService brandService, UserService userService, ProductService productService) {
        this.brandService = brandService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/profile")
    public BrandProfileResponse getProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return brandService.getProfile(user.getId());
    }

    @PutMapping("/profile")
    public BrandProfileResponse updateProfile(Authentication authentication,
                                              @Valid @RequestBody BrandProfileUpdateRequest request) {
        User user = (User) authentication.getPrincipal();
        return brandService.updateProfile(user.getId(), request);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(Authentication authentication,
                                               @Valid @RequestBody PasswordChangeRequestDTO request) {
        User user = (User) authentication.getPrincipal();
        userService.changePassword(user.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-products")
    public List<ProductResponse> getMyProducts(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return productService.getProductsByBrand(user.getId());
    }

    @GetMapping("/my-products/{id}")
    public ProductResponse getMyProduct(Authentication authentication, @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        return productService.getProductByBrand(user.getId(), id);
    }

    @PostMapping("/my-products")
    public ResponseEntity<ProductResponse> createMyProduct(Authentication authentication,
                                                           @Valid @RequestBody ProductCreateRequest request) {
        User user = (User) authentication.getPrincipal();
        ProductResponse newProduct = productService.createBrandProduct(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PutMapping("/my-products/{id}")
    public ProductResponse updateMyProduct(Authentication authentication, @PathVariable Long id,
                                           @Valid @RequestBody ProductUpdateRequest request) {
        User user = (User) authentication.getPrincipal();
        return productService.updateBrandProduct(user.getId(), id, request);
    }

    @DeleteMapping("/my-products/{id}")
    public ResponseEntity<Void> deleteMyProduct(Authentication authentication, @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        productService.deleteBrandProduct(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/public")
    public List<BrandPublicResponse> getAllPublicBrands() {
        return brandService.getAllPublicBrands();
    }

    @GetMapping("/public/{id}")
    public BrandPublicResponse getPublicBrandById(@PathVariable Long id) {
        return brandService.getPublicBrandById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<BrandResponse> getAllBrands() {
        return brandService.getAllBrands();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BrandResponse getBrandById(@PathVariable Long id) {
        return brandService.getBrandById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BrandResponse> createBrand(@Valid @RequestBody BrandCreateRequest request) {
        BrandResponse newBrand = brandService.createBrand(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBrand);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BrandResponse updateBrand(@PathVariable Long id, @Valid @RequestBody BrandAdminUpdateRequest request) {
        return brandService.updateBrandAdmin(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
