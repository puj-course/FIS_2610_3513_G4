package com.ceiba.fashtoll.searchEngine.elasticSearch.config;

import com.ceiba.fashtoll.searchEngine.elasticSearch.document.ProductDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;

/**
 * Inicializa el índice 'products_index' de Elasticsearch al ejecutar la aplicación.
 */
@Component
public class ElasticsearchIndexInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchIndexInitializer.class);
    private static final String INDEX_NAME = "products_search";

    private final ElasticsearchOperations elasticsearchOperations;

    public ElasticsearchIndexInitializer(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            IndexOperations indexOps = elasticsearchOperations.indexOps(ProductDocument.class);
            log.info("Elasticsearch: intentando crear índice '{}'...", INDEX_NAME);
            indexOps.createWithMapping();
            log.info("Elasticsearch: índice '{}' creado exitosamente.", INDEX_NAME);
        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (msg.contains("resource_already_exists_exception") || msg.contains("already_exists")) {
                log.info("Elasticsearch: el índice '{}' ya existe, todo en orden.", INDEX_NAME);
            } else {
                log.error("Elasticsearch: no se pudo inicializar el índice '{}'. " +
                          "La búsqueda no estará disponible. Error: {}", INDEX_NAME, msg);
            }
        }
    }
}
