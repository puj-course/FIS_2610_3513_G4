package com.ceiba.fashtoll.searchEngine.repositories;

import com.ceiba.fashtoll.searchEngine.indexingComponent.SearchToken;
import com.ceiba.fashtoll.searchEngine.indexingComponent.SearchToken_;
import com.ceiba.fashtoll.utilities.enums.GeneralFit;
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
            if (keyWords.isEmpty()) return null;
            Join<Product, SearchToken> joinTokens = from.join(Product_.tokens);
            
            return joinTokens.get(SearchToken_.token).in(keyWords);
        };
    }

    public static PredicateSpecification<Product> productType(String productType){
        return (from,  builder) -> {
            if (productType.isEmpty()) return null;
            return builder.equal(from.get(Product_.productType).get(ProductType_.name), productType);
        };
    }

    public static PredicateSpecification<Product> category(String category){
        return (from, builder) -> {
            if (category.isEmpty()) return null;
            return builder.equal(from.get(Product_.productType).get(ProductType_.category), category);
        };
    }

    public static PredicateSpecification<Product> generalFit(String generalFit){
        return (from, builder) -> {
            if (generalFit.isEmpty()) return null;
            return builder.equal(from.get(Product_.generalFit), generalFit);
        };
    }

    public static PredicateSpecification<Product> gender(String gender){
        return (from, builder) -> {
            if (gender.isEmpty()) return null;
            return builder.equal(from.get(Product_.gender), gender);
        };
    }

    public static PredicateSpecification<Product> color(String color){
        return (from, builder) -> {
            if (color.isEmpty()) return null;
            return builder.equal(from.get(Product_.color), color);
        };
    }

    // NO DEBE HABER UN FILTRO POR AVAILABLE,
    // PORQUE ALGUIEN QUISIERA BUSCAR PRODUCTOS QUE NO ESTAN DISPONIBLES

    public static PredicateSpecification<Product> priceRange(Double min, Double max){
        return (from, builder) -> {
            if (min == -1.0 || max == -1.0) return null;
            return builder.between(from.get(Product_.PRICE), min, max);
        };
    }

    public static PredicateSpecification<Product> tags(List<String> tags){
        return (from, builder) -> {
            if (tags.isEmpty()) return null;
            return builder.equal(from.get(Product_.tags), tags);
        };
    }

}
