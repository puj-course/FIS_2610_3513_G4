package com.ceiba.fashtoll.searchEngine.TemplateMethod;

import com.ceiba.fashtoll.searchEngine.dtos.QueryFilters;
import com.ceiba.fashtoll.searchEngine.indexingComponent.IndexingComponent;
import com.ceiba.fashtoll.searchEngine.rankingComponent.RankingComponent;
import com.ceiba.fashtoll.searchEngine.repositories.ProductSpecs;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<Product> products = this.productRepository.findAll(ProductSpecs.keyWords(keyWords)
                .and(ProductSpecs.productType(filters.productType())));

        return new ArrayList<>(new LinkedHashSet<>(products));
    }
}
