package com.ceiba.fashtoll.searchEngine;

import org.springframework.stereotype.Component;

@Component
public class Analyzer {
    public void characterFilter(String rawQuery){
        //limpia las etiquetas HTML
        String cleanQuery = rawQuery.replaceAll("<[^>]+>", " ");

        cleanQuery.replaceAll("","");
    }

    public void obtainKeyWords(){

    }
}
