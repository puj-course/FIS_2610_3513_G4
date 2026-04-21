package com.ceiba.fashtoll.worldModel.product.services;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductTypeRequest;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductTypeResponse;
import com.ceiba.fashtoll.worldModel.product.entities.ProductType;
import com.ceiba.fashtoll.worldModel.product.mappers.ProductTypeMapper;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductTypeService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeMapper productTypeMapper;

    @Autowired
    public ProductTypeService(ProductTypeRepository productTypeRepository, ProductTypeMapper productTypeMapper) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeMapper = productTypeMapper;
    }

    public List<ProductTypeResponse> getAllProductTypes() {
        this.logger.info("Se devolvieron todos los tipos de productos");

        return productTypeRepository.findAll().stream()
                .map(productTypeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductTypeResponse getProductTypeById(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de producto no encontrado: " + id));

        this.logger.info("Se devolvio el tipo de producto '" + productType.getName() + "' con id: " + productType.getId());

        return productTypeMapper.toResponse(productType);
    }

    @Transactional
    public ProductTypeResponse createProductType(ProductTypeRequest request) {
        ProductType productType = productTypeMapper.toEntity(request);
        ProductType savedProductType = productTypeRepository.save(productType);

        this.logger.info("Se creo el tipo de producto '" + productType.getName() + "' con id: " + savedProductType.getId());

        return productTypeMapper.toResponse(savedProductType);
    }

    @Transactional
    public ProductTypeResponse updateProductType(Long id, ProductTypeRequest request) {
        ProductType productType = this.productTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("tipo de producto", "id", id));

        boolean result = this.productTypeMapper.updateEntity(request, productType);
        ProductType savedProductType = this.productTypeRepository.save(productType);

        this.logger.info("Se actualizo el tipo de producto '" + productType.getName() + "' con id: " + savedProductType.getId());

        return this.productTypeMapper.toResponse(savedProductType);
    }

    public void deleteProductType(Long id) {
        ProductType productType = this.productTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("tipo de producto", "id", id));
        this.productTypeRepository.delete(productType);

        this.logger.info("Se elimino el tipo de producto '" + productType.getName() + "' con id: " + productType.getId());
    }

    @Transactional
    public void injectProductTypeFromJson(List<ProductTypeRequest> productTypesDTOs) {
        for(ProductTypeRequest productTypeDTO : productTypesDTOs) {
            this.productTypeRepository.save(this.productTypeMapper.toEntity(productTypeDTO));
        }
    }
}
