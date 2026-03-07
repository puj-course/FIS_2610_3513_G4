package com.ceiba.fashtoll.service;

import com.ceiba.fashtoll.dto.ProductTypeDTO;
import com.ceiba.fashtoll.entity.ProductType;
import com.ceiba.fashtoll.mapper.ProductTypeMapper;
import com.ceiba.fashtoll.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<ProductTypeDTO> getAllProductTypes() {
        return productTypeRepository.findAll().stream()
                .map(productTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductTypeDTO getProductTypeById(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de producto no encontrado: " + id));
        return productTypeMapper.toDTO(productType);
    }

    public ProductTypeDTO createProductType(ProductTypeDTO productTypeDTO) {
        ProductType productType = productTypeMapper.toEntity(productTypeDTO);
        ProductType savedProductType = productTypeRepository.save(productType);
        return productTypeMapper.toDTO(savedProductType);
    }

    public ProductTypeDTO updateProductType(Long id, ProductTypeDTO updatedProductTypeDTO) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de producto no encontrado: " + id));
        productType.setName(updatedProductTypeDTO.getName());
        productType.setCategory(updatedProductTypeDTO.getCategory());
        ProductType savedProductType = productTypeRepository.save(productType);
        return productTypeMapper.toDTO(savedProductType);
    }

    public void deleteProductType(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de producto no encontrado: " + id));
        productTypeRepository.delete(productType);
    }
}
