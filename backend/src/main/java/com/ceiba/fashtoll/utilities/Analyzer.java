package com.ceiba.fashtoll.utilities;

import org.springframework.stereotype.Component;
import java.text.Normalizer;
import java.util.ArrayList;
import org.tartarus.snowball.ext.spanishStemmer;
import java.util.List;
import java.util.Set;

@Component
public class Analyzer {
    public static String characterFilter(String rawQuery){
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

    public static List<String> obtainKeyWords(String query){
        query = query.toLowerCase();
        String[] words = query.split(" ");
        List<String> keyWords = new ArrayList<>();
        spanishStemmer stemmer = new spanishStemmer();

        //Palabras de parada
        Set<String> stopWords = Set.of("de", "la", "que", "el", "en", "y", "a", "los",
                "del", "se", "las", "por", "un", "con", "una", "para", "su", "al", "lo",
                "como", "mas", "pero", "sus", "le", "si", "o", "sin", "sobre", "entre",
                "hasta", "desde", "durante", "contra", "hacia", "mediante", "este", "esta",
                "esto", "estos", "estas", "ese", "esa", "eso", "esos", "esas", "aquel",
                "aquella", "aquello", "aquellos", "aquellas", "mi", "mis", "tu", "tus",
                "nuestro", "nuestra", "nuestros", "nuevas", "vuestro", "vuestra", "vuestros",
                "vuestras", "yo", "ella", "nosotros", "nosotras", "vosotros", "vosotras",
                "ellos", "ellas", "me", "te", "nos", "os", "les", "mio", "mia", "mios",
                "mias", "tuyo", "tuya", "tuyos", "tuyas", "suyo", "suya", "suyos", "suyas",
                "es", "son", "era", "eran", "fue", "fueron", "sea", "sean", "ser", "estar",
                "estan", "estamos", "estaba", "estaban", "serian", "seria", "haber", "hay",
                "habia", "hubo", "han", "hemos", "tiene", "tienen", "tenia", "tendra", "tendran",
                "hacer", "hace", "hacen", "hizo", "hecho", "muy", "mucho", "poco", "tan",
                "tanto", "solo", "ya", "aun", "tambien", "tampoco", "siempre", "nunca", "jamas",
                "tal", "cual", "quien", "quienes", "donde", "cuando", "porque", "mientras");

        for(String word : words){
            if (!stopWords.contains(word)){
                stemmer.setCurrent(word);
                stemmer.stem();
                String rootWord = stemmer.getCurrent();
                keyWords.add(rootWord);
            }

        }

        return keyWords;
    }
}
