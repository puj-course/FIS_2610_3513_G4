package com.ceiba.fashtoll.searchEngine.TemplateMethod;

import com.ceiba.fashtoll.searchEngine.dtos.ProductSearchRequest;
import com.ceiba.fashtoll.searchEngine.dtos.QueryFilters;
import com.ceiba.fashtoll.searchEngine.IndexingComponent;
import com.ceiba.fashtoll.searchEngine.RankingComponent;
import com.ceiba.fashtoll.utilities.Singleton.Analyzer;
import com.ceiba.fashtoll.worldModel.admin.metrics.QualityMetricsTracker;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchEngine {

    protected final Analyzer analyzer;
    protected IndexingComponent indexingComponent;
    protected RankingComponent rankingComponent;
    protected final ProductRepository productRepository;
    protected final QualityMetricsTracker metricsTracker;

    public SearchEngine(IndexingComponent indexingComponent, RankingComponent rankingComponent, ProductRepository productRepository, QualityMetricsTracker metricsTracker) {
        this.indexingComponent = indexingComponent;
        this.rankingComponent = rankingComponent;
        this.productRepository = productRepository;
        this.metricsTracker = metricsTracker;
        this.analyzer = Analyzer.getInstance();
    }

    public final Page<Product> processSimpleQuery(ProductSearchRequest request) {
        String cleanQuery = this.analyzer.characterFilter(request.query());
        List<String> keyWords = this.analyzer.obtainKeyWords(cleanQuery);

        // Evalua el porcentaje de palabras utiles en el query del usuario
        if(!request.query().isEmpty()){
            double textQuality = analyzer.calculateQueryQualityIndex(request.query(), keyWords);
            metricsTracker.addQueryQualitySample(textQuality);
        }

        Pageable pageRequest = PageRequest.of(request.page(), request.size());

        return this.returnResults(keyWords, null, pageRequest);
    }

    public final Page<Product> processFilterQuery(ProductSearchRequest request) {
        String cleanQuery = this.analyzer.characterFilter(request.query());
        List<String> keyWords = this.analyzer.obtainKeyWords(cleanQuery);

        // Evalua el porcentaje de palabras utiles en el query del usuario
        if(!request.query().isEmpty()){
            double textQuality = analyzer.calculateQueryQualityIndex(request.query(), keyWords);
            metricsTracker.addQueryQualitySample(textQuality);
        }

        QueryFilters filters = new QueryFilters(
                request.productType(),
                request.category(),
                request.generalFit(),
                request.gender(),
                request.color(),
                request.minPrice(),
                request.maxPrice(),
                request.tags()
        );

        Pageable pageRequest = PageRequest.of(request.page(), request.size());

        return this.returnResults(keyWords, filters, pageRequest);
    }

    protected abstract Page<Product> returnResults(List<String> keyWords, QueryFilters filters, Pageable pageRequest);
}
