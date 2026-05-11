package com.ceiba.fashtoll.worldModel.product.Builder;

import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductAdminUpdateRequest;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductCreateRequest;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductDirector {
    private ProductBuilder builder;

    @Autowired
    public ProductDirector(@Qualifier("simpleBuilder") ProductBuilder builder) {
        this.builder = builder;
    }

    public void changeBuilder(ProductBuilder builder) {
        this.builder = builder;
    }

    public void makeSimpleProduct(ProductCreateRequest request){
        this.builder.reset();
        boolean available = false;
        if(request.getAvailable() != null) available = request.getAvailable();

        ProductDetails productDetails = new ProductDetails(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                available,
                0.0,
                LocalDateTime.now()
        );

        this.builder.associateBrand(request.getBrandId());
        this.builder.putProductDetails(productDetails);
        this.builder.putOfficialLink(request.getLinkProduct());
        this.builder.putProductType(request.getProductTypeId());
        this.builder.putEnums(request.getGeneralFit(), request.getGender(), request.getColor());
        this.builder.putImagesURLs(request.getImageUrls());
        this.builder.putTags(request.getTagIds());
    }

    public void adminUpdateSimpleProduct(Long productId, ProductAdminUpdateRequest request){
        this.builder.reset();
        this.builder.adminUpdateProduct(productId);

        boolean available = false;
        if(request.getAvailable() != null) available = request.getAvailable();

        ProductDetails productDetails = new ProductDetails(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                available,
                0.0,
                LocalDateTime.now()
        );

        this.builder.putProductDetails(productDetails);
        this.builder.putEnums(request.getGeneralFit(), request.getGender(), request.getColor());
        this.builder.putOfficialLink(request.getLinkProduct());
        this.builder.putProductType(request.getProductTypeId());
        this.builder.associateBrand(request.getBrandId());
        this.builder.putImagesURLs(request.getImageUrls());
        this.builder.putTags(request.getTagIds());
    }

    public void updateSimpleProduct(Long brandId, Long productId, ProductUpdateRequest request){
        this.builder.reset();
        this.builder.adminUpdateProduct(productId);

        boolean available = false;
        if(request.getAvailable() != null) available = request.getAvailable();

        ProductDetails productDetails = new ProductDetails(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                available,
                0.0,
                LocalDateTime.now()
        );

        this.builder.associateBrand(brandId);
        this.builder.putProductDetails(productDetails);
        this.builder.putEnums(request.getGeneralFit(), request.getGender(), request.getColor());
        this.builder.putOfficialLink(request.getLinkProduct());
        this.builder.putProductType(request.getProductTypeId());
        this.builder.putImagesURLs(request.getImageUrls());
        this.builder.putTags(request.getTagIds());
    }

    public void makeJsonSimpleProduct(ProductCreateRequest request, Brand brand){
        this.builder.reset();
        boolean available = false;
        if(request.getAvailable() != null) available = request.getAvailable();

        ProductDetails productDetails = new ProductDetails(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                available,
                0.0,
                LocalDateTime.now()
        );

        this.builder.setBrand(brand);
        this.builder.putProductDetails(productDetails);
        this.builder.putOfficialLink(request.getLinkProduct());
        this.builder.putProductType(request.getProductTypeId());
        this.builder.putEnums(request.getGeneralFit(), request.getGender(), request.getColor());
        this.builder.putImagesURLs(request.getImageUrls());
        this.builder.putTags(request.getTagIds());
    }
}
