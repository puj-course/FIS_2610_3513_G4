package com.ceiba.fashtoll.worldModel.product.mappers;

import com.ceiba.fashtoll.worldModel.product.dtos.ProductC_U_Request;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductResponse;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.review.mapper.ReviewMapper;
import com.ceiba.fashtoll.worldModel.tag.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ProductTypeMapper productTypeMapper;
    private final TagMapper tagMapper;
    private final ReviewMapper reviewMapper;

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
        response.setReviewCount(product.getReviewCount());
        response.setLinkProduct(product.getLinkProduct());
        response.setCreatedAt(product.getLastTimeEdited());

        if (product.getImages() != null) {
            response.setImageUrls(
                    product.getImages()
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

        if (product.getReviews() != null) {
            response.setReviews(
                    product.getReviews().stream()
                            .map(reviewMapper::toResponse)
                            .collect(Collectors.toList())
            );
        } else {
            response.setReviews(new ArrayList<>());
        }

        return response;
    }

    public Product toEntity(ProductC_U_Request request) {
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

    public void updateEntityFromBrand(ProductC_U_Request request, Product product) {
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
    public void updateEntityFromAdmin(ProductC_U_Request request, Product product) {
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
