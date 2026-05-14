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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    //las keyWords son las del query de busqueda
    public Page<Product> scoreKeywordsAlgorithm(List<String> queryKeyWords, Specification spec, Pageable pageable) {
        List<Product> productList = new ArrayList<>(this.productRepository.findAll(spec));

        for(Product product : productList){
            this.calculateTermFrequency(product, queryKeyWords);
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
        int productScore = 0;
        String desc = product.getDescription();
        String name = product.getName();

        List<Integer> keyWordFrequency = new ArrayList<>();
        for(String keyword : queryKeyWords) {
            int aux = (int) this.keyWordCounter(desc, keyword);
            if(aux <= this.keyWordFrequencyLimit && aux > 0) {
                keyWordFrequency.add(aux);
            } else if(aux > this.keyWordFrequencyLimit && aux > 0) {
                keyWordFrequency.add(this.keyWordFrequencyLimit);
            }
        }

        for(String keyword : queryKeyWords) {
            int aux = (int) this.keyWordCounter(name, keyword);
            if(aux <= this.keyWordFrequencyLimit && aux > 0) {
                keyWordFrequency.add(aux);
            } else if(aux > this.keyWordFrequencyLimit && aux > 0) {
                keyWordFrequency.add(this.keyWordFrequencyLimit);
            }
        }

        for(Integer p : keyWordFrequency){
            productScore += p;
        }
        product.setRankingScore(productScore);
    }

    // POR AHORA SOLO IMPLEMENTAN 2 DE 3 CRITERIOS PARA CLASIFICAR LOS PRODUCTOS
    public void calculateInverseDocumentFrequency(Product product, List<String> queryKeyWords) {
        int productScore = 0;
        List<SearchToken> searchTokens = new ArrayList<>(product.getTokens());

        // evalua que tanto se repite una palabra en el search token repository
        List<Pair<String,Long>> keyWordI_D_Frequency = new ArrayList<>();
        for(String keyword : queryKeyWords) {
            long aux = this.searchTokenRepository.countByToken(keyword);
            if(aux != 0){
                Pair<String,Long> pair = Pair.of(keyword, aux);
                keyWordI_D_Frequency.add(pair);
            }
        }

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


    }

    public void normalizationByFieldLength(){
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
