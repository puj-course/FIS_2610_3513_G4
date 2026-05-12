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
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("simpleBuilder")
@Scope("prototype")
public class SimpleProductBuilder implements ProductBuilder {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private Product result;

    @Autowired
    public SimpleProductBuilder(BrandRepository brandRepository, ProductRepository productRepository, ProductTypeRepository productTypeRepository) {
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public void reset() {
        this.result = new Product();
    }

    @Override
    public void associateBrand(Long brandId, boolean update) {
        if(brandId != null) {
            Brand brand = this.brandRepository.findById(brandId)
                    .orElseThrow(() -> new ResourceNotFoundException("Marca","id",brandId));

            this.result.setBrand(brand);
        }
    }

    @Override
    public void setBrand(Brand brand) {}

    @Override
    public void putProductDetails(ProductDetails productDetails) {
        this.result.setName(productDetails.name());
        this.result.setDescription(productDetails.description());
        this.result.setPrice(productDetails.price());
        this.result.setAvailable(productDetails.available());
        this.result.setRating(productDetails.rating());
        this.result.setLastTimeEdited(productDetails.createdAt());
    }

    @Override
    public void putOfficialLink(String linkOfficial) {
        this.result.setLinkProduct(linkOfficial);
    }

    @Override
    public void putProductType(Long productTypeId) {
        if(productTypeId != null){
            ProductType productType = this.productTypeRepository.findById(productTypeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de producto","id", productTypeId));

            this.result.setProductType(productType);
        }
    }

    @Override
    public void putEnums(GeneralFit generalFit, Gender gender, Color color) {
        this.result.setGeneralFit(generalFit);
        this.result.setGender(gender);
        this.result.setColor(color);
    }

    @Override
    public void putImagesURLs(List<String> imagesURLs) {
        this.result.setImages(null);
        this.logger.info("El producto '" + this.result.getName() + "' con id: " + this.result.getId() + " NO TIENE URLs DE IMAGENES");
    }

    @Override
    public void putTags(List<String> tagsIds) {
        this.result.setTags(null);
        this.logger.info("El producto '" + this.result.getName() + "' con id: " + this.result.getId() + " NO TIENE TAGS");
    }

    @Override
    public void updateProductID(Long productId) {
        this.result = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", productId));
    }

    public Product getResult(){
        return this.result;
    }
}
