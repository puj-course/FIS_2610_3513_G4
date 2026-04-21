package com.ceiba.fashtoll.searchEngine.indexingComponent;

import com.ceiba.fashtoll.utilities.Singleton.Analyzer;
import com.ceiba.fashtoll.searchEngine.repositories.SearchTokenRepository;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.Observer.EventType;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEvent;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEventPublisher;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductObserver;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class IndexingComponent implements ProductObserver {
    private Analyzer analyzer;
    private final SearchTokenRepository searchTokenRepository;
    private final ProductRepository productRepository;

    public IndexingComponent(SearchTokenRepository searchTokenRepository, ProductEventPublisher publisher, ProductRepository productRepository) {
        this.searchTokenRepository = searchTokenRepository;
        this.productRepository = productRepository;
        publisher.subscribe(this);
    }

    public void setAnalyzer(){
        this.analyzer = Analyzer.getInstance();
    }

    public Set<SearchToken> storeInfo(List<String> keyWords) {
        Set<SearchToken> searchTokens = new HashSet<>();
        for (String keyWord : keyWords) {
            SearchToken token =this.searchTokenRepository.findByToken(keyWord)
                    .orElseGet(() -> {
                        SearchToken newToken = new SearchToken();
                        newToken.setToken(keyWord);

                        return this.searchTokenRepository.save(newToken);
                    });
            searchTokens.add(token);
        }

        return searchTokens;
    }

    @Override
    public void onProductEvent(ProductEvent event) {
        this.setAnalyzer();
        if (event.getType() == EventType.CREATED || event.getType() == EventType.UPDATED) {
            Product product = event.getProduct();
            String textToAnalize = product.getName() + " " + product.getDescription();

            String textToStore = this.analyzer.characterFilter(textToAnalize);
            List<String> keyWords = this.analyzer.obtainKeyWords(textToStore);

            Set<SearchToken> productTokens = this.storeInfo(keyWords);
            product.setTokens(productTokens);
            this.productRepository.save(product);
        }
    }
}
