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
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

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
        List<Product> productList = new ArrayList<>(this.productRepository.findAll(spec).stream().toList());

        productList.forEach(product -> {
            this.calculateTermFrequency(product, queryKeyWords);
            this.calculateInverseDocumentFrequency(product, queryKeyWords);
        });

        productList.sort(Comparator.comparing(Product::getRankingScore).reversed());

        // 4. Manual Pagination (Slicing)
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productList.size());

        List<Product> pagedList = (start < productList.size())
                ? productList.subList(start, end)
                : new ArrayList<>();

        productList.forEach(product -> {
            product.setRankingScore(0);
        });

        return new PageImpl<>(pagedList, pageable, productList.size());
    }

    //evalua un solo producto a la vez, evalua las keywords de ese producto.
    public void calculateTermFrequency(Product product, List<String> queryKeyWords) {
        int productScore = 0, productNameScore = 0, productDescriptionScore = 0;
        String description = product.getDescription();
        String name = product.getName();

        List<Integer> keyWordNameFrequency = queryKeyWords.stream()
                .map(keyword -> (int) this.keyWordCounter(name, keyword))
                .filter(frequency -> frequency > 0)
                .map(frequency -> Math.min(frequency, this.keyWordFrequencyLimit))
                .toList();

        productNameScore = keyWordNameFrequency.stream().mapToInt(Integer::intValue).sum();
        /* mapToInt(Integer::intValue) hace lo mismo que mapToInt(kWFrequency -> kWFrequency),
        * pues al final lo que hacen los dos es obtener el valor entero de un elemento de la lista
        * keyWordNameFrequency
        * */

        /*Stream API
        * filter() requires a parameter of the functional interface Predicate<T>,
        * which has the method: boolean test(T variableToTest).
        * map() requires a parameter of the functional interface Function<T,R>,
        * which has the method: R apply(T t),
        * the method does something with 't' and returns the result of the function R.
        * forEach() requires a parameter of the functional interface Consumer<T>,
        * which has a method: void accept(T t), the method does something with 't'.
        * */

        List<Integer> keyWordDescFrequency = queryKeyWords.stream()
                .map(keyWord -> (int) this.keyWordCounter(description, keyWord))
                .filter(kWFrequency -> kWFrequency > 0)
                .map(kWFrequency -> Math.min(kWFrequency, this.keyWordFrequencyLimit))
                .toList();

        productDescriptionScore = keyWordDescFrequency.stream().mapToInt(Integer::intValue).sum();

        if(description.length() >= 150) productDescriptionScore -= 3;
        productScore = productNameScore + productDescriptionScore;

        product.setRankingScore(productScore);
    }

    public void calculateInverseDocumentFrequency(Product product, List<String> queryKeyWords) {
        int productScore = 0;
        Set<String> queryKeyWordsSet = new HashSet<>(queryKeyWords);
        List<SearchToken> productTokens = new ArrayList<>(product.getTokens().stream().toList());
        List<SearchToken> allSearchTokens = new ArrayList<>(this.searchTokenRepository.findAll().stream().toList());

        // evalua que tanto se repite una palabra en el search token repository
        List<Pair<String,Long>> keyWordI_D_Frequency = allSearchTokens.stream()
                .map(token -> this.searchTokenRepository.countByToken(token.getToken()))
                .filter(pair -> pair.getSecond() != 0)
                .toList();

        // obtiene las palabras clave mas raras
        List<String> rarestKeyWords = new ArrayList<>();
        if(!keyWordI_D_Frequency.isEmpty()){
            OptionalLong aux = keyWordI_D_Frequency.stream()
                    .mapToLong(Pair::getSecond)
                    .min();

            long minimumFrequency = aux.orElse(0L);

            rarestKeyWords = keyWordI_D_Frequency.stream()
                    .filter(pair -> pair.getSecond() == minimumFrequency)
                    .map(Pair::getFirst)
                    .toList();
        }

        // evalua que palabras clave del query coinciden con las palabras mas raras
        List<String> rarestQueryKeyWords = rarestKeyWords.stream()
                //.filter(keyWord -> queryKeyWordsSet.contains(keyWord))
                .filter(queryKeyWordsSet::contains)
                .toList();

        // evalua que palabras clave del producto coinciden con las palabras clave de la busqueda
        productScore = IntStream.range(0, rarestQueryKeyWords.size())
                .map(index -> rarestQueryKeyWords.get(index)
                        .equals(productTokens.get(index).getToken())
                        ? 3 : 1)
                .sum();

        productScore += product.getRankingScore();
        product.setRankingScore(productScore);
    }

    public long keyWordCounter(String desc, String kW){
        String cleanTexto = cleanDescription(desc);

        return Pattern.compile(Pattern.quote(kW), Pattern.CASE_INSENSITIVE)
                .matcher(cleanTexto)
                .results()
                .count();
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
