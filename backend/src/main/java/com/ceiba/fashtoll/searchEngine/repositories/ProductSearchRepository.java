package com.ceiba.fashtoll.searchEngine.repositories;

import com.ceiba.fashtoll.searchEngine.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
}
