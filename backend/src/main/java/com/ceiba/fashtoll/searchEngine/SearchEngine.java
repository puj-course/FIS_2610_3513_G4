package com.ceiba.fashtoll.searchEngine;

import com.ceiba.fashtoll.searchEngine.crawlingComponent.CrawlingComponent;
import com.ceiba.fashtoll.searchEngine.indexingComponent.IndexingComponent;
import com.ceiba.fashtoll.searchEngine.rankingComponent.RankingComponent;
import com.ceiba.fashtoll.utilities.Analyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchEngine {         //la clase implementa el patron estructural Façade
    private CrawlingComponent crawlingComponent;
    private IndexingComponent indexingComponent;
    private RankingComponent rankingComponent;

    @Autowired
    public SearchEngine(CrawlingComponent crawlingComponent, IndexingComponent indexingComponent, RankingComponent rankingComponent, Analyzer analyzer) {
        this.crawlingComponent = crawlingComponent;
        this.indexingComponent = indexingComponent;
        this.rankingComponent = rankingComponent;
    }

    public void processQuery(String rawQuery){
        List<String> keyWords = new ArrayList<>();

        String cleanQuery = Analyzer.characterFilter(rawQuery);
        keyWords = Analyzer.obtainKeyWords(cleanQuery);
    }
}
