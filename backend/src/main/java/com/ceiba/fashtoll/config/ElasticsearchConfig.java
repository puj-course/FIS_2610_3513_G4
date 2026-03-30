package com.ceiba.fashtoll.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.ceiba.fashtoll.search.repository")
public class ElasticsearchConfig {
}
