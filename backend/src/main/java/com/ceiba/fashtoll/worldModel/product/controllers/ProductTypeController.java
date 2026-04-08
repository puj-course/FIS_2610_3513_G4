package com.ceiba.fashtoll.worldModel.product.controllers;

import com.ceiba.fashtoll.worldModel.product.dtos.ProductTypeRequest;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductTypeResponse;
import com.ceiba.fashtoll.worldModel.product.services.ProductTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-types")
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    @Autowired
    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @GetMapping
    public List<ProductTypeResponse> getAllProductTypes() {
        return productTypeService.getAllProductTypes();
    }

    @GetMapping("/{id}")
    public ProductTypeResponse getProductTypeById(@PathVariable Long id) {
        return productTypeService.getProductTypeById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductTypeResponse> createProductType(@Valid @RequestBody ProductTypeRequest request) {
        ProductTypeResponse newProductType = productTypeService.createProductType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProductType);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductTypeResponse updateProductType(@PathVariable Long id, @Valid @RequestBody ProductTypeRequest request) {
        return productTypeService.updateProductType(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) {
        productTypeService.deleteProductType(id);
        return ResponseEntity.noContent().build();
    }
}
