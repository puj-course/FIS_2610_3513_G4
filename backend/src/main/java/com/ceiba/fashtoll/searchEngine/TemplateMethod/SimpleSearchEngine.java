package com.ceiba.fashtoll.searchEngine.TemplateMethod;

import com.ceiba.fashtoll.searchEngine.indexingComponent.IndexingComponent;
import com.ceiba.fashtoll.searchEngine.rankingComponent.RankingComponent;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SimpleSearchEngine extends SearchEngine {

    @Autowired
    public SimpleSearchEngine(IndexingComponent indexingComponent, RankingComponent rankingComponent, ProductRepository productRepository) {
        super(indexingComponent, rankingComponent, productRepository);
    }

    @Override
    protected List<Product> returnResults(List<String> keyWords) {
        return this.productRepository.findBySearchTokens(keyWords);
    }
}
