package com.ceiba.fashtoll.worldModel.product.mappers;

import com.ceiba.fashtoll.utilities.enums.Category;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductTypeRequest;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductTypeResponse;
import com.ceiba.fashtoll.worldModel.product.entities.ProductType;
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
        type.setCategory(Category.categorize(request.getCategory()));

        return type;
    }

    public boolean updateEntity(ProductTypeRequest request, ProductType type) {
        if (request == null || type == null) return false;
        type.setName(request.getName());
        type.setCategory(Category.categorize(request.getCategory()));

        return true;
    }
}
