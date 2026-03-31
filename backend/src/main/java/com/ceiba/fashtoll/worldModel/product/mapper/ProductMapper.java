package com.ceiba.fashtoll.worldModel.product.mapper;

import com.ceiba.fashtoll.worldModel.product.dto.ProductAdminUpdateRequest;
import com.ceiba.fashtoll.worldModel.product.dto.ProductCreateRequest;
import com.ceiba.fashtoll.worldModel.product.dto.ProductResponse;
import com.ceiba.fashtoll.worldModel.product.dto.ProductUpdateRequest;
import com.ceiba.fashtoll.worldModel.product.entity.Product;
import com.ceiba.fashtoll.searchEngine.tag.mapper.TagMapper;
import com.ceiba.fashtoll.worldModel.product.entity.ProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ProductTypeMapper productTypeMapper;
    private final TagMapper tagMapper;

    public ProductResponse toResponse(Product product) {
        if (product == null) return null;
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setBrandId(product.getBrand() != null ? product.getBrand().getId() : null);
        response.setProductType(productTypeMapper.toResponse(product.getProductType()));
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setGeneralFit(product.getGeneralFit());
        response.setGender(product.getGender());
        response.setColor(product.getColor());
        response.setAvailable(product.getAvailable());
        response.setRating(product.getRating());
        response.setLinkProduct(product.getLinkProduct());
        response.setCreatedAt(product.getCreatedAt());

        if (product.getImages() != null) {
            response.setImageUrls(
                    product.getImages().stream()
                            .map(ProductImage::getImageUrl)
                            .collect(Collectors.toList())
            );
        }

        if (product.getTags() != null) {
            response.setTags(
                    product.getTags().stream()
                            .map(tagMapper::toResponse)
                            .collect(Collectors.toList())
            );
        } else {
            response.setTags(new ArrayList<>());
        }

        return response;
    }

    public Product toEntity(ProductCreateRequest request) {
        if (request == null) return null;
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setGeneralFit(request.getGeneralFit());
        product.setGender(request.getGender());
        product.setColor(request.getColor());
        product.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        product.setLinkProduct(request.getLinkProduct());
        return product;
    }

    public void updateEntityFromBrand(ProductUpdateRequest request, Product product) {
        if (request == null || product == null) return;
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setGeneralFit(request.getGeneralFit());
        product.setGender(request.getGender());
        product.setColor(request.getColor());
        if (request.getAvailable() != null) {
            product.setAvailable(request.getAvailable());
        }
        product.setLinkProduct(request.getLinkProduct());
    }
    public void updateEntityFromAdmin(ProductAdminUpdateRequest request, Product product) {
        if (request == null || product == null) return;
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setGeneralFit(request.getGeneralFit());
        product.setGender(request.getGender());
        product.setColor(request.getColor());
        if (request.getAvailable() != null) {
            product.setAvailable(request.getAvailable());
        }
        product.setLinkProduct(request.getLinkProduct());
    }
}
