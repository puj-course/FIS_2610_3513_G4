package com.ceiba.fashtoll.searchEngine.TemplateMethod;

import com.ceiba.fashtoll.searchEngine.dtos.ProductSearchRequest;
import com.ceiba.fashtoll.searchEngine.dtos.QueryFilters;
import com.ceiba.fashtoll.searchEngine.indexingComponent.IndexingComponent;
import com.ceiba.fashtoll.searchEngine.rankingComponent.RankingComponent;
import com.ceiba.fashtoll.utilities.Singleton.Analyzer;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import java.util.List;

public abstract class SearchEngine {

    protected final Analyzer analyzer;
    protected IndexingComponent indexingComponent;
    protected RankingComponent rankingComponent;
    protected final ProductRepository productRepository;

    public SearchEngine(IndexingComponent indexingComponent, RankingComponent rankingComponent, ProductRepository productRepository) {
        this.indexingComponent = indexingComponent;
        this.rankingComponent = rankingComponent;
        this.productRepository = productRepository;
        this.analyzer = Analyzer.getInstance();
    }

    public final List<Product> processSimpleQuery(String rawQuery){
        String cleanQuery = this.analyzer.characterFilter(rawQuery);
        List<String> keyWords = this.analyzer.obtainKeyWords(cleanQuery);

        return this.returnResults(keyWords, null);
    }

    public final List<Product> processFilterQuery(ProductSearchRequest request){
        String cleanQuery = this.analyzer.characterFilter(request.query());
        List<String> keyWords = this.analyzer.obtainKeyWords(cleanQuery);

        QueryFilters filters = new QueryFilters(
                request.productType(),
                request.category()
        );

        return this.returnResults(keyWords, filters);
    }

    protected abstract List<Product> returnResults(List<String> keyWords, QueryFilters filters);
}
