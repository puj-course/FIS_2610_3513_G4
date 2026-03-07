package com.ceiba.fashtoll.mapper;

import com.ceiba.fashtoll.dto.ProductDTO;
import com.ceiba.fashtoll.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        if (product == null) return null;
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setGeneralFit(product.getGeneralFit());
        dto.setGender(product.getGender());
        dto.setColor(product.getColor());
        dto.setAvailable(product.getAvailable());
        dto.setRating(product.getRating());
        dto.setLinkProduct(product.getLinkProduct());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setBrandId(product.getBrand() != null ? product.getBrand().getId() : null);
        dto.setProductTypeId(product.getProductType() != null ? product.getProductType().getId() : null);
        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) return null;
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setGeneralFit(dto.getGeneralFit());
        product.setGender(dto.getGender());
        product.setColor(dto.getColor());
        product.setAvailable(dto.getAvailable());
        product.setRating(dto.getRating());
        product.setLinkProduct(dto.getLinkProduct());
        product.setCreatedAt(dto.getCreatedAt());
        return product;
    }
}
