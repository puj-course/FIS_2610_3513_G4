package com.ceiba.fashtoll.searchEngine;

import com.ceiba.fashtoll.searchEngine.TemplateMethod.ConcreteSearchEngines.FilterSearchEngine;
import com.ceiba.fashtoll.searchEngine.TemplateMethod.ConcreteSearchEngines.SimpleSearchEngine;
import com.ceiba.fashtoll.searchEngine.dtos.*;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import com.ceiba.fashtoll.worldModel.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSearchService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SimpleSearchEngine simpleSearchEngine;
    private final FilterSearchEngine filterSearchEngine;
    private final ProductRepository productRepository;

    @Autowired
    public ProductSearchService(SimpleSearchEngine simpleSearchEngine, FilterSearchEngine filterSearchEngine, ProductRepository productRepository) {
        this.simpleSearchEngine = simpleSearchEngine;
        this.filterSearchEngine = filterSearchEngine;
        this.productRepository = productRepository;
    }

    public ProductSearchResponse simpleSearch(ProductSearchRequest request) {
        Page<Product> searchResultPage = this.simpleSearchEngine.processSimpleQuery(request);
        List<Product> searchResultProducts;

        if(searchResultPage != null) {
            searchResultProducts = searchResultPage.getContent();


            List<ProductDocument> searchResponseProducts = new ArrayList<>();
            for (Product p : searchResultProducts) {
                ProductDocument nP = this.mapToDocument(p);
                searchResponseProducts.add(nP);
            }

            ProductSearchResponse searchResponse = new ProductSearchResponse(
                    searchResponseProducts,
                    searchResultPage.getNumber(),
                    searchResultPage.getTotalPages(),
                    searchResultPage.getTotalElements(),
                    searchResultPage.getSize()
            );

            this.logger.info("Se buscaron productos con el query '" + request.query() + "'");

            return searchResponse;
        }

        return null;
    }

    public ProductSearchResponse filterSearch(ProductSearchRequest request){
        Page<Product> searchResultPage;
        List<Product> searchResultProducts;

        if(checkIfHasntFilter(request) && (request.query() == null || request.query().isEmpty())) {
            Pageable pageRequest = PageRequest.of(request.page(), request.size());

            searchResultPage = this.productRepository.findAll(pageRequest);
        } else {
            searchResultPage = this.filterSearchEngine.processFilterQuery(request);
        }

        if(searchResultPage != null) {
            searchResultProducts = searchResultPage.getContent();

            List<ProductDocument> searchResponseProducts = new ArrayList<>();
            for (Product p : searchResultProducts) {
                ProductDocument nP = this.mapToDocument(p);

                searchResponseProducts.add(nP);
            }

            ProductSearchResponse searchResponse = new ProductSearchResponse(
                    searchResponseProducts,
                    searchResultPage.getNumber(),
                    searchResultPage.getTotalPages(),
                    searchResultPage.getTotalElements(),
                    searchResultPage.getSize()
            );

            this.logger.info("Se buscaron productos con filtros y con el query '" + request.query() + "'");

            return searchResponse;
        }

        return null;
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
                            .map(Tag::getName)
                            .collect(Collectors.toList())
                        : List.of())
                .createdAt(product.getLastTimeEdited())
                .build();
    }

    public boolean checkIfHasntFilter(ProductSearchRequest request){
        return (request.productType() == null || request.productType().isEmpty()) &&
                (request.category()== null || request.category().isEmpty()) &&
                (request.color()== null || request.color().isEmpty()) &&
                (request.generalFit()== null || request.generalFit().isEmpty()) &&
                (request.gender()== null || request.gender().isEmpty()) &&
                request.minPrice() == null && request.maxPrice() == null && request.tags() == null;
    }
}
