package com.ceiba.fashtoll.searchEngine.rankingComponent;

import com.ceiba.fashtoll.searchEngine.indexingComponent.SearchToken;
import com.ceiba.fashtoll.searchEngine.repositories.SearchTokenRepository;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class RankingComponent {
    private final SearchTokenRepository searchTokenRepository;
    private final ProductRepository productRepository;
    private final int keyWordFrequencyLimit;

    @Autowired
    public RankingComponent(SearchTokenRepository searchTokenRepository, ProductRepository productRepository) {
        this.searchTokenRepository = searchTokenRepository;
        this.productRepository = productRepository;
        this.keyWordFrequencyLimit = 5;
    }


    // .stream() es para objetos que no se van a modificar
    // mientras que new es para objetos que si se modificaran
    // ordenar cuenta como modificar.
    //las keyWords son las del query de busqueda
    public Page<Product> scoreKeywordsAlgorithm(List<String> queryKeyWords, Specification spec, Pageable pageable) {
        List<Product> productList = new ArrayList<>(this.productRepository.findAll(spec));

        for(Product product : productList){
            this.calculateTermFrequency(product, queryKeyWords);
            this.calculateInverseDocumentFrequency(product, queryKeyWords);
        }

        productList.sort(Comparator.comparing(Product::getRankingScore).reversed());

        // 4. Manual Pagination (Slicing)
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productList.size());

        List<Product> pagedList = (start < productList.size())
                ? productList.subList(start, end)
                : new ArrayList<>();

        for(Product product : productList){
            product.setRankingScore(0);
        }

        return new PageImpl<>(pagedList, pageable, productList.size());
    }

    //evalua un solo producto a la vez, evalua las keywords de ese producto.
    public void calculateTermFrequency(Product product, List<String> queryKeyWords) {
        int productScore = 0, productNameScore = 0, productDescriptionScore = 0;
        String description = product.getDescription();
        String name = product.getName();

        List<Integer> keyWordNameFrequency = new ArrayList<>();
        for(String keyword : queryKeyWords) {
            int aux = (int) this.keyWordCounter(name, keyword);
            if(aux <= this.keyWordFrequencyLimit && aux > 0) {
                keyWordNameFrequency.add(aux);
            } else if(aux > this.keyWordFrequencyLimit && aux > 0) {
                keyWordNameFrequency.add(this.keyWordFrequencyLimit);
            }
        }

        //queryKeyWords.stream().forEach(keyWord -> );

        for(Integer p : keyWordNameFrequency){
            productNameScore += p;
        }

        List<Integer> keyWordDescFrequency = new ArrayList<>();
        for(String keyword : queryKeyWords) {
            int aux = (int) this.keyWordCounter(description, keyword);
            if(aux <= this.keyWordFrequencyLimit && aux > 0) {
                keyWordDescFrequency.add(aux);
            } else if(aux > this.keyWordFrequencyLimit && aux > 0) {
                keyWordDescFrequency.add(this.keyWordFrequencyLimit);
            }
        }

        for(Integer p : keyWordDescFrequency){
            productDescriptionScore += p;
        }

        if(description.length() >= 150) productDescriptionScore -= 3;
        productScore = productNameScore + productDescriptionScore;

        product.setRankingScore(productScore);
    }

    public void calculateInverseDocumentFrequency(Product product, List<String> queryKeyWords) {
        int productScore = 0;
        Set<String> queryKeyWordsSet = new HashSet<>(queryKeyWords);
        List<SearchToken> productTokens = new ArrayList<>(product.getTokens());
        List<SearchToken> allSearchTokens = new ArrayList<>(this.searchTokenRepository.findAll());

        // evalua que tanto se repite una palabra en el search token repository
        List<Pair<String,Long>> keyWordI_D_Frequency = new ArrayList<>();
        for(SearchToken token : allSearchTokens) {
            Pair<String,Long> pair = this.searchTokenRepository.countByToken(token.getToken());
            if(pair.getSecond() != 0){
                keyWordI_D_Frequency.add(pair);
            }
        }

        // obtiene las palabras clave mas raras y las que no son raras
        List<Pair<String,Long>> rarestKeyWords = new ArrayList<>();

        if(!keyWordI_D_Frequency.isEmpty()){

            long minimumFrequency = keyWordI_D_Frequency.getFirst().getSecond();

            for(Pair<String, Long> pair : keyWordI_D_Frequency){
                if(pair.getSecond() < minimumFrequency){
                    minimumFrequency = pair.getSecond();
                }
            }

            for (Pair<String, Long> pair : keyWordI_D_Frequency) {
                if (pair.getSecond() == minimumFrequency) {
                    rarestKeyWords.add(pair);
                }
            }
        }

        List<String> rarestQueryKeyWords = new ArrayList<>();

        // evalua que palabras clave del query coinciden con las palabras mas raras y cuales coinciden con las que no son raras
        for(Pair<String, Long> pair : rarestKeyWords){
            if (queryKeyWordsSet.contains(pair.getFirst())) {
                rarestQueryKeyWords.add(pair.getFirst());
            }
        }

        // evalua que palabras clave del producto coinciden con las palabras clave de la busqueda
        int i = 0;
        for(String queryKey : rarestQueryKeyWords){
            if(queryKey.equals(productTokens.get(i).getToken())){
                productScore += 3;
            } else productScore += 1;
            i++;
        }
        productScore += product.getRankingScore();
        product.setRankingScore(productScore);
    }

    public long keyWordCounter(String desc, String kW){
        String cleanTexto = cleanDescription(desc);

        long count = Pattern.compile(Pattern.quote(kW), Pattern.CASE_INSENSITIVE)
                .matcher(cleanTexto)
                .results()
                .count();

       return count;
    }

    public static String cleanDescription(String input) {
        if (input == null || input.isBlank()) return "";

        // Normalización NFD
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        // Eliminar diacríticos y caracteres especiales
        return Pattern.compile("\\p{M}").matcher(normalized).replaceAll("")
                .replaceAll("[^a-zA-Z0-9\\s]", "");
    }
}
