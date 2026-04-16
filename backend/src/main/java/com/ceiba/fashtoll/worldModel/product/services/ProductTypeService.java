package com.ceiba.fashtoll.worldModel.product.services;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductTypeRequest;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductTypeResponse;
import com.ceiba.fashtoll.worldModel.product.entities.ProductType;
import com.ceiba.fashtoll.worldModel.product.mappers.ProductTypeMapper;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductTypeService {

    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeMapper productTypeMapper;

    @Autowired
    public ProductTypeService(ProductTypeRepository productTypeRepository, ProductTypeMapper productTypeMapper) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeMapper = productTypeMapper;
    }

    public List<ProductTypeResponse> getAllProductTypes() {
        return productTypeRepository.findAll().stream()
                .map(productTypeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductTypeResponse getProductTypeById(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de producto no encontrado: " + id));
        return productTypeMapper.toResponse(productType);
    }

    @Transactional
    public ProductTypeResponse createProductType(ProductTypeRequest request) {
        ProductType productType = productTypeMapper.toEntity(request);
        ProductType savedProductType = productTypeRepository.save(productType);
        return productTypeMapper.toResponse(savedProductType);
    }

    @Transactional
    public ProductTypeResponse updateProductType(Long id, ProductTypeRequest request) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("tipo de producto", "id", id));

        boolean result = productTypeMapper.updateEntity(request, productType);
        ProductType savedProductType = productTypeRepository.save(productType);
        return productTypeMapper.toResponse(savedProductType);
    }

    public void deleteProductType(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("tipo de producto", "id", id));
        productTypeRepository.delete(productType);
    }
}
