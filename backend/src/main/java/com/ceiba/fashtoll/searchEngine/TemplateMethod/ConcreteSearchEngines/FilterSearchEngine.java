package com.ceiba.fashtoll.searchEngine.TemplateMethod.ConcreteSearchEngines;

import com.ceiba.fashtoll.searchEngine.TemplateMethod.SearchEngine;
import com.ceiba.fashtoll.searchEngine.dtos.QueryFilters;
import com.ceiba.fashtoll.searchEngine.IndexingComponent;
import com.ceiba.fashtoll.searchEngine.RankingComponent;
import com.ceiba.fashtoll.searchEngine.entities.ProductSpecs;
import com.ceiba.fashtoll.worldModel.admin.metrics.QualityMetricsTracker;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterSearchEngine extends SearchEngine {

    @Autowired
    public FilterSearchEngine(IndexingComponent indexingComponent, RankingComponent rankingComponent, ProductRepository productRepository, QualityMetricsTracker metricsTracker) {
        super(indexingComponent, rankingComponent, productRepository, metricsTracker);
    }

    @Override
    protected Page<Product> returnResults(List<String> keyWords, QueryFilters filters, Pageable pageRequest) {
        Specification<Product> spec = Specification.where(
                ProductSpecs.keyWords(keyWords)
                        .or(ProductSpecs.productType(filters.productType()))
                        .or(ProductSpecs.category(filters.category()))
                        .or(ProductSpecs.generalFit(filters.generalFit()))
                        .or(ProductSpecs.gender(filters.gender()))
                        .or(ProductSpecs.color(filters.color()))
                        .or(ProductSpecs.priceRange(filters.minPrice(), filters.maxPrice()))
                        .or(ProductSpecs.tags(filters.tags()))
        );
        /* 1. SI BUSCO UN PRODUCTO QUE NO ESTA ME MUESTRA LOS PRODUCTOS QUE COINCIDEN CON MI BUSQUEDA
        * DEBERIA DECIR "NO TENEMOS ESE PRODUCTO PERO TE PODRIA INTERESAR:"
        * 2. SI BUSCO PANTALONES JEAN ME APARECEN PANTALONES DE SUDADERA, CHINOS, JOGGER,
        * NO ME APARECEN SOLO JEANS
        */
        if(keyWords != null && !keyWords.isEmpty()){

            return this.rankingComponent.scoreKeywordsAlgorithm(keyWords, spec, pageRequest);
        }

        return this.productRepository.findAll(spec, pageRequest);
    }
}
