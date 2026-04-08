package com.ceiba.fashtoll.searchEngine;

import com.ceiba.fashtoll.searchEngine.crawlingComponent.CrawlingComponent;
import com.ceiba.fashtoll.searchEngine.indexingComponent.IndexingComponent;
import com.ceiba.fashtoll.searchEngine.rankingComponent.RankingComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchEngine {         //la clase implementa el patron estructural Façade
    private CrawlingComponent crawlingComponent;
    private IndexingComponent indexingComponent;
    private RankingComponent rankingComponent;
    private Analyzer analyzer;

    @Autowired
    public SearchEngine(CrawlingComponent crawlingComponent, IndexingComponent indexingComponent, RankingComponent rankingComponent, Analyzer analyzer) {
        this.crawlingComponent = crawlingComponent;
        this.indexingComponent = indexingComponent;
        this.rankingComponent = rankingComponent;
        this.analyzer = analyzer;
    }

    public void processQuery(String rawQuery){
        this.analyzer.characterFilter(rawQuery);
    }
}
