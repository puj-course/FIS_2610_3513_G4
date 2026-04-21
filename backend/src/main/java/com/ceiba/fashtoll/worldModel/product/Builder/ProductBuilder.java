package com.ceiba.fashtoll.worldModel.product.Builder;

import com.ceiba.fashtoll.utilities.enums.Color;
import com.ceiba.fashtoll.utilities.enums.Gender;
import com.ceiba.fashtoll.utilities.enums.GeneralFit;
import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import java.util.List;

public interface ProductBuilder {
    void reset();
    void associateBrand(Long brandId);
    void setBrand(Brand brand);
    void putProductDetails(ProductDetails productDetails);
    void putOfficialLink(String linkOfficial);
    void putProductType(Long productTypeId);
    void putEnums(GeneralFit generalFit, Gender gender, Color color);
    void putImagesURLs(List<String> imagesURLs);
    void putTags(List<Long> tagsIds);
    void updateProduct();
    Product getResult();
}
