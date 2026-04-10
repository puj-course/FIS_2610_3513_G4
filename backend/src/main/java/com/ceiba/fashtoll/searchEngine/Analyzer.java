package com.ceiba.fashtoll.searchEngine;

import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class Analyzer {
    public String characterFilter(String rawQuery){
        //filtro de etiquetas HTML
        String cleanQuery = rawQuery.replaceAll("<[^>]+>", " ");
        //filtro de &nbsp; representa un espacio normal en la web
        cleanQuery = cleanQuery.replaceAll("&[a-z]+;"," ");
        //filtro de precios
        cleanQuery = cleanQuery.replaceAll("\\$\\d+\\.\\d+", " ");
        //normalizacion del texto, NFD (Normalización de la Descomposición Canónica)
        //convierte á en a', separa la tile o el acento de la vocal
        cleanQuery = Normalizer.normalize(cleanQuery, Normalizer.Form.NFD);
        //filtro de tildes
        cleanQuery = cleanQuery.replaceAll("\\u0301", "");
        //cambio de & a y
        cleanQuery = cleanQuery.replace("&", "y");
        //filtro de correo electronico
        cleanQuery = cleanQuery.replaceAll("[a-zA-Z]+.[a-zA-Z]+(?=@)@[a-zA-Z]+.[a-zA-Z]+", " ");
        //filtro de caracteres especiales
        cleanQuery = cleanQuery.replaceAll("[^a-zA-Z\\s]+", " ");
        //filtro de espacios excesivos
        cleanQuery = cleanQuery.replaceAll("\\s+", " ");

        return cleanQuery;
    }

    public void obtainKeyWords(){

    }
}
