package com.ceiba.fashtoll.searchEngine.repositories;

import com.ceiba.fashtoll.searchEngine.indexingComponent.SearchToken;
import com.ceiba.fashtoll.searchEngine.indexingComponent.SearchToken_;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.entities.ProductType_;
import com.ceiba.fashtoll.worldModel.product.entities.Product_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ProductSpecs{

    public static PredicateSpecification<Product> keyWords (List<String> keyWords) {
        return (from, builder) -> {
            if (keyWords == null || keyWords.isEmpty()) return null;
            Join<Product, SearchToken> joinTokens = from.join(Product_.tokens);
            
            return joinTokens.get(SearchToken_.token).in(keyWords);
        };
    }

    public static PredicateSpecification<Product> productType(String productTypeWord){
        return (from,  builder) -> {
            if (productTypeWord == null || productTypeWord.isBlank()) return null;
            return builder.equal(from.get(Product_.productType).get(ProductType_.name), productTypeWord);
        };
    }

    public static PredicateSpecification<Product> category(String categoryWord){
        return (from, builder) -> {
            if (categoryWord == null || categoryWord.isBlank()) return null;
            return builder.equal(from.get(Product_.productType).get(ProductType_.category), categoryWord);
        };
    }
}
