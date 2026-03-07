package com.ceiba.fashtoll.mapper;

import com.ceiba.fashtoll.dto.ProductTypeDTO;
import com.ceiba.fashtoll.entity.ProductType;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeMapper {

    public ProductTypeDTO toDTO(ProductType productType) {
        if (productType == null) return null;
        ProductTypeDTO dto = new ProductTypeDTO();
        dto.setId(productType.getId());
        dto.setName(productType.getName());
        dto.setCategory(productType.getCategory());
        return dto;
    }

    public ProductType toEntity(ProductTypeDTO dto) {
        if (dto == null) return null;
        ProductType productType = new ProductType();
        productType.setId(dto.getId());
        productType.setName(dto.getName());
        productType.setCategory(dto.getCategory());
        return productType;
    }
}
