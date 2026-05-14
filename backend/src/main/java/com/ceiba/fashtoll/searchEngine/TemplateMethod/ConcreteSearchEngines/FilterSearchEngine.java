package com.ceiba.fashtoll.searchEngine.TemplateMethod.ConcreteSearchEngines;

import com.ceiba.fashtoll.searchEngine.TemplateMethod.SearchEngine;
import com.ceiba.fashtoll.searchEngine.dtos.QueryFilters;
import com.ceiba.fashtoll.searchEngine.indexingComponent.IndexingComponent;
import com.ceiba.fashtoll.searchEngine.rankingComponent.RankingComponent;
import com.ceiba.fashtoll.searchEngine.repositories.ProductSpecs;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    protected Page<Product> returnResults(List<String> keyWords, QueryFilters filters, Pageable pageRequest) {
        Specification<Product> spec = Specification.where(
                ProductSpecs.keyWords(keyWords)
                        .and(ProductSpecs.productType(filters.productType()))
                        .and(ProductSpecs.category(filters.category()))
                        .and(ProductSpecs.generalFit(filters.generalFit()))
                        .and(ProductSpecs.gender(filters.gender()))
                        .and(ProductSpecs.color(filters.color()))
                        .and(ProductSpecs.priceRange(filters.minPrice(), filters.maxPrice()))
                        .and(ProductSpecs.tags(filters.tags()))
        );

        return this.productRepository.findAll(spec, pageRequest);
    }
}
