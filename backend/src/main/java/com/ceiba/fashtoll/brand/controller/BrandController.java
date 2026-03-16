package com.ceiba.fashtoll.brand.controller;

import com.ceiba.fashtoll.brand.dto.BrandDTO;
import com.ceiba.fashtoll.brand.dto.BrandProfileDTO;
import com.ceiba.fashtoll.brand.service.BrandService;
import com.ceiba.fashtoll.product.dto.ProductDTO;
import com.ceiba.fashtoll.product.service.ProductService;
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
    public BrandProfileDTO getProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return brandService.getProfile(user.getId());
    }

    @PutMapping("/profile")
    public BrandProfileDTO updateProfile(Authentication authentication, @RequestBody BrandProfileDTO profileDTO) {
        User user = (User) authentication.getPrincipal();
        return brandService.updateProfile(user.getId(), profileDTO);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(Authentication authentication, @RequestBody PasswordChangeRequestDTO request) {
        User user = (User) authentication.getPrincipal();
        userService.changePassword(user.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-products")
    public List<ProductDTO> getMyProducts(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return productService.getProductsByBrand(user.getId());
    }

    // TODO: Implementar createMyProduct()

    @PutMapping("/my-products/{id}")
    public ProductDTO updateMyProduct(Authentication authentication, @PathVariable Long id, @RequestBody ProductDTO productDTO) {
        User user = (User) authentication.getPrincipal();
        return productService.updateBrandProduct(user.getId(), id, productDTO);
    }

    @DeleteMapping("/my-products/{id}")
    public ResponseEntity<Void> deleteMyProduct(Authentication authentication, @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        productService.deleteBrandProduct(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<BrandDTO> getAllBrands() {
        return brandService.getAllBrands();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BrandDTO getBrandById(@PathVariable Long id) {
        return brandService.getBrandById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BrandDTO> createBrand(@RequestBody BrandDTO brandDTO) {
        BrandDTO newBrand = brandService.createBrand(brandDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBrand);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BrandDTO updateBrand(@PathVariable Long id, @RequestBody BrandDTO updatedBrandDTO) {
        return brandService.updateBrand(id, updatedBrandDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
