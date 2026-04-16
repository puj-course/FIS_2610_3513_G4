package com.ceiba.fashtoll.searchEngine;

import com.ceiba.fashtoll.searchEngine.crawlingComponent.CrawlingComponent;
import com.ceiba.fashtoll.searchEngine.indexingComponent.IndexingComponent;
import com.ceiba.fashtoll.searchEngine.rankingComponent.RankingComponent;
import com.ceiba.fashtoll.utilities.Analyzer;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchEngine {         //la clase implementa el patron estructural Façade
    private CrawlingComponent crawlingComponent;
    private IndexingComponent indexingComponent;
    private RankingComponent rankingComponent;
    private final ProductRepository productRepository;

    @Autowired
    public SearchEngine(CrawlingComponent crawlingComponent, IndexingComponent indexingComponent, RankingComponent rankingComponent, Analyzer analyzer, ProductRepository productRepository) {
        this.crawlingComponent = crawlingComponent;
        this.indexingComponent = indexingComponent;
        this.rankingComponent = rankingComponent;
        this.productRepository = productRepository;
    }

    public List<Product> processQuery(String rawQuery){
        String cleanQuery = Analyzer.characterFilter(rawQuery);
        List<String> keyWords = Analyzer.obtainKeyWords(cleanQuery);

        return this.productRepository.findBySearchTokens(keyWords);
    }
}
