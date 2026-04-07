package com.ceiba.fashtoll.searchEngine;

import com.ceiba.fashtoll.searchEngine.crawlingComponent.CrawlingComponent;
import com.ceiba.fashtoll.searchEngine.indexingComponent.IndexingComponent;
import com.ceiba.fashtoll.searchEngine.rankingComponent.RankingComponent;
import org.springframework.stereotype.Component;

@Component
public class SearchEngine {         //la clase implementa el patron estructural Façade
    private IndexingComponent indexingComponent;
    private RankingComponent rankingComponent;

    public void processQuery(String query){
        String[] split = query.split(" ");

        //definir las palabras clave
        for(String s: split){
           switch (s){
               case "chaqueta":
                   // pertenece a la categoria TOPS
                   break;
               case "pantalon":
                   // pertenece a la categoria BOTTOMS
                   break;
               case "":
                   break;
           }
        }
    }
}
