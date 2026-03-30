package com.ceiba.fashtoll.product.mapper;

import com.ceiba.fashtoll.product.dto.ProductTypeRequest;
import com.ceiba.fashtoll.product.dto.ProductTypeResponse;
import com.ceiba.fashtoll.product.entity.ProductType;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeMapper {

    public ProductTypeResponse toResponse(ProductType type) {
        if (type == null) return null;
        ProductTypeResponse response = new ProductTypeResponse();
        response.setId(type.getId());
        response.setName(type.getName());
        response.setCategory(type.getCategory());
        return response;
    }

    public ProductType toEntity(ProductTypeRequest request) {
        if (request == null) return null;
        ProductType type = new ProductType();
        type.setName(request.getName());
        type.setCategory(request.getCategory());
        return type;
    }

    public void updateEntity(ProductTypeRequest request, ProductType type) {
        if (request == null || type == null) return;
        type.setName(request.getName());
        type.setCategory(request.getCategory());
    }
}
