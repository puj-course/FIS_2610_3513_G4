package com.ceiba.fashtoll.searchEngine.TemplateMethod;

import com.ceiba.fashtoll.searchEngine.dtos.QueryFilters;
import com.ceiba.fashtoll.searchEngine.indexingComponent.IndexingComponent;
import com.ceiba.fashtoll.searchEngine.rankingComponent.RankingComponent;
import com.ceiba.fashtoll.searchEngine.repositories.ProductSpecs;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Component
public class FilterSearchEngine extends SearchEngine {

    @Autowired
    public FilterSearchEngine(IndexingComponent indexingComponent, RankingComponent rankingComponent, ProductRepository productRepository) {
        super(indexingComponent, rankingComponent, productRepository);
    }

    @Override
    protected List<Product> returnResults(List<String> keyWords, QueryFilters filters) {
        PredicateSpecification<Product> spec = PredicateSpecification.where(
                ProductSpecs.keyWords(keyWords)
                        .and(ProductSpecs.productType(filters.productType()))
                        .and(ProductSpecs.category(filters.category()))
                        .and(ProductSpecs.generalFit(filters.generalFit()))
                        .and(ProductSpecs.gender(filters.gender()))
                        .and(ProductSpecs.color(filters.color()))
                        //.and(ProductSpecs.available(filters.available()))
                        .and(ProductSpecs.priceRange(filters.minPrice(), filters.maxPrice()))
                        //.and(ProductSpecs.tags(filters.tags()))
        );

        List<Product> products = this.productRepository.findAll(spec);

        return new ArrayList<>(new LinkedHashSet<>(products));
    }
}
