package com.ceiba.fashtoll.worldModel.product.controllers;

//import com.ceiba.fashtoll.worldModel.product.dtos.*;
//utiliza solo estos dtos? o utiliza todos
import com.ceiba.fashtoll.worldModel.product.dtos.ProductC_U_Request;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductResponse;

import com.ceiba.fashtoll.worldModel.product.services.ProductService;
import com.ceiba.fashtoll.worldModel.review.dto.ReviewResponse;
import com.ceiba.fashtoll.worldModel.client.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@PreAuthorize("hasAnyRole('ADMIN','BRAND','CLIENT')")
public class ProductController {

    private final ProductService productService;
    private final ClientService clientService;

    @Autowired
    public ProductController(ProductService productService, ClientService clientService) {
        this.productService = productService;
        this.clientService = clientService;
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewResponse>> getProductReviews(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getReviewsForProduct(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/simple")
    public ResponseEntity<ProductResponse> createSimpleProduct(@Valid @RequestBody ProductC_U_Request request) {
        ProductResponse newProduct = productService.createSimpleProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> createCompleteProduct(@Valid @RequestBody ProductC_U_Request request) {
        ProductResponse newProduct = productService.createCompleteProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/simple/{id}")
    public ProductResponse updateSimpleProduct(@PathVariable Long id, @Valid @RequestBody ProductC_U_Request request) {
        return productService.updateSimpleProduct(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductResponse updateCompleteProduct(@PathVariable Long id, @Valid @RequestBody ProductC_U_Request request) {
        return productService.updateCompleteProduct(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

