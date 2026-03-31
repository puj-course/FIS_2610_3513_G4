package com.ceiba.fashtoll.utilities.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.ceiba.fashtoll.elasticSearch.repository")
public class ElasticsearchConfig {
}
