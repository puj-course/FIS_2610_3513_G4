package com.ceiba.fashtoll.worldModel.product.Builder.builders;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.utilities.enums.Color;
import com.ceiba.fashtoll.utilities.enums.Gender;
import com.ceiba.fashtoll.utilities.enums.GeneralFit;
import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.brand.BrandRepository;
import com.ceiba.fashtoll.worldModel.product.Builder.ProductBuilder;
import com.ceiba.fashtoll.worldModel.product.Builder.ProductDetails;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.entities.ProductType;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class SimpleJsonProductBuilder implements ProductBuilder {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BrandRepository brandRepository;
    private final ProductTypeRepository productTypeRepository;
    private Product product;

    @Autowired
    public SimpleJsonProductBuilder(BrandRepository brandRepository, ProductTypeRepository productTypeRepository) {
        this.brandRepository = brandRepository;
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public void reset() {
        this.product = new Product();
    }

    @Override
    public void associateBrand(Long brandId) {
    }

    public void setBrand(Brand brand) {
        this.product.setBrand(brand);
    }

    @Override
    public void putProductDetails(ProductDetails productDetails) {
        this.product.setName(productDetails.name());
        this.product.setDescription(productDetails.description());
        this.product.setPrice(productDetails.price());
        this.product.setAvailable(productDetails.available());
        this.product.setRating(productDetails.rating());
        this.product.setCreatedAt(productDetails.createdAt());
    }

    @Override
    public void putOfficialLink(String linkOfficial) {
        this.product.setLinkProduct("");
        this.logger.info("El producto '" + this.product.getName() + "' con id: " + this.product.getId() + " NO TIENE LINK");
    }

    @Override
    public void putProductType(Long productTypeId) {
        ProductType productType = this.productTypeRepository.findById(productTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de producto","id", productTypeId));

        this.product.setProductType(productType);
    }

    @Override
    public void putEnums(GeneralFit generalFit, Gender gender, Color color) {
        this.product.setGeneralFit(generalFit);
        this.product.setGender(gender);
        this.product.setColor(color);
    }

    @Override
    public void putImagesURLs(List<String> imagesURLs) {
        this.product.setImages(null);
        this.logger.info("El producto '" + this.product.getName() + "' con id: " + this.product.getId() + " NO TIENE URLs DE IMAGENES");
    }

    @Override
    public void putTags(List<Long> tagsIds) {
        this.product.setTags(null);
        this.logger.info("El producto '" + this.product.getName() + "' con id: " + this.product.getId() + " NO TIENE TAGS");
    }

    @Override
    public void updateProduct() {
    }

    public Product getResult(){
        return this.product;
    }
}
