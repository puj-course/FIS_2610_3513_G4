package com.ceiba.fashtoll.product.controller;

import com.ceiba.fashtoll.product.dto.ProductTypeDTO;
import com.ceiba.fashtoll.product.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<ProductTypeDTO> getAllProductTypes() {
        return productTypeService.getAllProductTypes();
    }

    @GetMapping("/{id}")
    public ProductTypeDTO getProductTypeById(@PathVariable Long id) {
        return productTypeService.getProductTypeById(id);
    }

    @PostMapping
    public ResponseEntity<ProductTypeDTO> createProductType(@RequestBody ProductTypeDTO productTypeDTO) {
        ProductTypeDTO newProductType = productTypeService.createProductType(productTypeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProductType);
    }

    @PutMapping("/{id}")
    public ProductTypeDTO updateProductType(@PathVariable Long id, @RequestBody ProductTypeDTO updatedProductTypeDTO) {
        return productTypeService.updateProductType(id, updatedProductTypeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) {
        productTypeService.deleteProductType(id);
        return ResponseEntity.noContent().build();
    }
}
