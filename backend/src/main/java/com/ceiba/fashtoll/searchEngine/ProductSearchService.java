package com.ceiba.fashtoll.searchEngine;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.ceiba.fashtoll.searchEngine.dtos.ProductDocument;
import com.ceiba.fashtoll.searchEngine.dtos.ProductSearchResponse;
import com.ceiba.fashtoll.searchEngine.repositories.ProductSearchRepository;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import com.ceiba.fashtoll.searchEngine.dtos.ProductElasticSearchRequest;
import com.ceiba.fashtoll.searchEngine.dtos.ProductElasticSearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSearchService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProductSearchRepository productSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ProductRepository productRepository;
    private final SearchEngine searchEngine;

    @Autowired
    public ProductSearchService(ProductSearchRepository productSearchRepository,
                                ElasticsearchOperations elasticsearchOperations,
                                ProductRepository productRepository,
                                SearchEngine searchEngine) {

        this.productSearchRepository = productSearchRepository;
        this.elasticsearchOperations = elasticsearchOperations;
        this.productRepository = productRepository;
        this.searchEngine = searchEngine;
    }

    public ProductSearchResponse search(String query){
        List<Product> searchResultProducts = this.searchEngine.processQuery(query);
        List<ProductDocument> searchResponseProducts = new ArrayList<>();

        for(Product p: searchResultProducts){
            ProductDocument nP = this.mapToDocument(p);

            searchResponseProducts.add(nP);
        }

        this.logger.info("Se buscaron productos con el query '" + query + "'");

        return new ProductSearchResponse(searchResponseProducts);
    }

    /**
     * Buscar productos usando Elasticsearch con búsqueda Full-Text por keywords,
     * filtros estrictos (AND), y tags coincidentes (OR con scoring).
     */
    public ProductElasticSearchResponse search(ProductElasticSearchRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize());

        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // Búsqueda Full-Text por palabras clave y tolerancia a errores tipográficos
        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            boolQueryBuilder.must(Query.of(q -> q
                    .multiMatch(mm -> mm
                            .query(request.getKeyword())
                            .fields("name^3", "description^1", "tags^2")
                            .fuzziness("AUTO")
                            .prefixLength(1)
                    )
            ));
        }

        // Búsqueda por filtros estrictos (todos se deben cumplir)
        if (request.getProductTypeName() != null && !request.getProductTypeName().isBlank()) {
            boolQueryBuilder.filter(Query.of(q -> q
                    .term(t -> t.field("productTypeName").value(request.getProductTypeName()))
            ));
        }

        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            boolQueryBuilder.filter(Query.of(q -> q
                    .term(t -> t.field("category").value(request.getCategory()))
            ));
        }

        if (request.getGeneralFit() != null && !request.getGeneralFit().isBlank()) {
            boolQueryBuilder.filter(Query.of(q -> q
                    .term(t -> t.field("generalFit").value(request.getGeneralFit()))
            ));
        }

        if (request.getGender() != null && !request.getGender().isBlank()) {
            boolQueryBuilder.filter(Query.of(q -> q
                    .term(t -> t.field("gender").value(request.getGender()))
            ));
        }

        if (request.getColor() != null && !request.getColor().isBlank()) {
            boolQueryBuilder.filter(Query.of(q -> q
                    .term(t -> t.field("color").value(request.getColor()))
            ));
        }

        if (request.getAvailable() != null) {
            boolQueryBuilder.filter(Query.of(q -> q
                    .term(t -> t.field("available").value(request.getAvailable()))
            ));
        }

        // Filtro de rango de precio
        if (request.getMinPrice() != null || request.getMaxPrice() != null) {
            boolQueryBuilder.filter(Query.of(q -> q
                    .range(r -> {
                        var rangeQuery = r.number(n -> {
                            var nq = n.field("price");
                            if (request.getMinPrice() != null) {
                                nq.gte(request.getMinPrice());
                            }
                            if (request.getMaxPrice() != null) {
                                nq.lte(request.getMaxPrice());
                            }
                            return nq;
                        });
                        return rangeQuery;
                    })
            ));
        }

        // Búsqueda por tags con score
        // Cada tag coincidente incrementa el score y aparecerá primero
        // Al menos un tag debe coincidir
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            for (String tag : request.getTags()) {
                boolQueryBuilder.should(Query.of(q -> q
                        .term(t -> t.field("tags").value(tag))
                ));
            }
            boolQueryBuilder.minimumShouldMatch("1");
        }

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(Query.of(q -> q.bool(boolQueryBuilder.build())))
                .withPageable(pageRequest)
                .build();

        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(
                nativeQuery, ProductDocument.class);

        List<ProductDocument> products = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        long totalResults = searchHits.getTotalHits();
        int totalPages = (int) Math.ceil((double) totalResults / request.getSize());

        this.logger.info("Se buscaron productos con el query '" + request.toString() + "' con elastic search");

        return ProductElasticSearchResponse.builder()
                .products(products)
                .currentPage(request.getPage())
                .totalPages(totalPages)
                .totalResults(totalResults)
                .pageSize(request.getSize())
                .build();
    }

    /**
     * Indexa o actualiza un Producto en Elasticsearch.
     * Llamado después de operaciones create/update en ProductService.
     */
    public void indexProduct(Product product) {
        ProductDocument document = mapToDocument(product);
        productSearchRepository.save(document);

        this.logger.info("Se indexo el producto '" + product.getName() + "' con el id: " + product.getId());
    }

    /**
     * Elimina un Producto del index de Elasticsearch.
     * Llamado después de operaciones delete en ProductService.
     */
    public void deleteProduct(Long id) {
        productSearchRepository.deleteById(id);

        this.logger.info("Se elimino el producto con id: " + id + " del repositorio de elastic search");
    }

    /**
     * Re-indexa TODOS los productos desde PostgreSQL hacia Elasticsearch.
     * Útil para la primera vez o en caso de desicronización
     */
    public void reindexAll() {
        // Limpiar el index
        productSearchRepository.deleteAll();

        // Cargar todos los productos desde PostgreSQL e indexarlos
        List<Product> allProducts = productRepository.findAll();
        List<ProductDocument> documents = allProducts.stream()
                .map(this::mapToDocument)
                .collect(Collectors.toList());
        productSearchRepository.saveAll(documents);

        this.logger.info("Se reindexaron todos los productos del repositorio de elastic search");
    }

    /**
     * Mapea una entidad JPA Product a un ProductDocument de Elasticsearch
     */
    private ProductDocument mapToDocument(Product product) {
        this.logger.info("Se mapeo el producto '" + product.getName() + "' con el id: " + product.getId() + " a un 'ProductDocument'");

        return ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .brandPictureUrl(product.getBrand() != null ? product.getBrand().getPictureURL() : null)
                .brandIsVerified(product.getBrand() != null ? product.getBrand().getIsVerified() : null)
                .productTypeName(product.getProductType() != null ? product.getProductType().getName() : null)
                .category(product.getProductType() != null && product.getProductType().getCategory() != null
                        ? product.getProductType().getCategory().name() : null)
                .price(product.getPrice() != null ? product.getPrice() : null)
                .generalFit(product.getGeneralFit() != null ? product.getGeneralFit().name() : null)
                .gender(product.getGender() != null ? product.getGender().name() : null)
                .color(product.getColor() != null ? product.getColor().name() : null)
                .available(product.getAvailable())
                .rating(product.getRating())
                .linkProduct(product.getLinkProduct())
                .imageUrls(product.getImages() != null
                        ? product.getImages()
                        : List.of())
                .tags(product.getTags() != null
                        ? product.getTags().stream()
                            .map(tag -> tag.getName())
                            .collect(Collectors.toList())
                        : List.of())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
