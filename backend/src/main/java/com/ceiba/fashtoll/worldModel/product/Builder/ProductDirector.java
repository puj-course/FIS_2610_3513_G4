package com.ceiba.fashtoll.worldModel.product.Builder;

import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductC_U_Request;

import java.time.LocalDateTime;

public class ProductDirector {
    private ProductBuilder builder;

    public ProductDirector(ProductBuilder builder) {
        this.builder = builder;
    }

    public void changeBuilder(ProductBuilder builder) {
        this.builder = builder;
    }

    public void makeSimpleProduct(ProductC_U_Request request){
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

        this.builder.associateBrand(request.getBrandId(), true);
        this.builder.putProductDetails(productDetails);
        this.builder.putOfficialLink(request.getLinkProduct());
        this.builder.putProductType(request.getProductTypeId());
        this.builder.putEnums(request.getGeneralFit(), request.getGender(), request.getColor());
        this.builder.putImagesURLs(request.getImageUrls());
        this.builder.putTags(request.getTags());
    }

    public void makeCompleteProduct(ProductC_U_Request request){
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

        this.builder.associateBrand(request.getBrandId(), true);
        this.builder.putProductDetails(productDetails);
        this.builder.putOfficialLink(request.getLinkProduct());
        this.builder.putProductType(request.getProductTypeId());
        this.builder.putEnums(request.getGeneralFit(), request.getGender(), request.getColor());
        this.builder.putImagesURLs(request.getImageUrls());
        this.builder.putTags(request.getTags());
    }

    public void adminUpdateSimpleProduct(Long productId, ProductC_U_Request request){
        this.builder.reset();
        this.builder.updateProductID(productId);

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
        this.builder.associateBrand(request.getBrandId(), true);
        this.builder.putImagesURLs(request.getImageUrls());
        this.builder.putTags(request.getTags());
    }

    public void updateSimpleProduct(Long brandId, Long productId, ProductC_U_Request request){
        this.builder.reset();
        this.builder.updateProductID(productId);

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

        this.builder.associateBrand(brandId, true);
        this.builder.putProductDetails(productDetails);
        this.builder.putEnums(request.getGeneralFit(), request.getGender(), request.getColor());
        this.builder.putOfficialLink(request.getLinkProduct());
        this.builder.putProductType(request.getProductTypeId());
        this.builder.putImagesURLs(request.getImageUrls());
        this.builder.putTags(request.getTags());
    }

    public void adminUpdateCompleteProduct(Long productId, ProductC_U_Request request){
        this.builder.reset();
        this.builder.updateProductID(productId);

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
        this.builder.associateBrand(request.getBrandId(), true);
        this.builder.putImagesURLs(request.getImageUrls());
        this.builder.putTags(request.getTags());
    }

    public void updateCompleteProduct(Long brandId, Long productId, ProductC_U_Request request){
        this.builder.reset();
        this.builder.updateProductID(productId);

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

        this.builder.associateBrand(brandId, true);
        this.builder.putProductDetails(productDetails);
        this.builder.putEnums(request.getGeneralFit(), request.getGender(), request.getColor());
        this.builder.putOfficialLink(request.getLinkProduct());
        this.builder.putProductType(request.getProductTypeId());
        this.builder.putImagesURLs(request.getImageUrls());
        this.builder.putTags(request.getTags());
    }

    public void makeJsonSimpleProduct(ProductC_U_Request request, Brand brand){
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
        this.builder.putTags(request.getTags());
    }
}
