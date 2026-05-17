package com.ceiba.fashtoll.searchEngine.TemplateMethod.ConcreteSearchEngines;

import com.ceiba.fashtoll.searchEngine.TemplateMethod.SearchEngine;
import com.ceiba.fashtoll.searchEngine.dtos.QueryFilters;
import com.ceiba.fashtoll.searchEngine.IndexingComponent;
import com.ceiba.fashtoll.searchEngine.RankingComponent;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SimpleSearchEngine extends SearchEngine {

    @Autowired
    public SimpleSearchEngine(IndexingComponent indexingComponent, RankingComponent rankingComponent, ProductRepository productRepository) {
        super(indexingComponent, rankingComponent, productRepository);
    }

    @Override
    protected Page<Product> returnResults(List<String> keyWords, QueryFilters filters, Pageable pageRequest) {
        return this.productRepository.findBySearchTokens(keyWords, pageRequest);
    }
}
