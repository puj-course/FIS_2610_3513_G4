package com.ceiba.fashtoll.searchEngine.elasticsearch.repository;

import com.ceiba.fashtoll.searchEngine.elasticsearch.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
}
