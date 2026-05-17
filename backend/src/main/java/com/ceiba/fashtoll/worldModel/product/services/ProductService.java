package com.ceiba.fashtoll.worldModel.product.services;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.brand.BrandRepository;
import com.ceiba.fashtoll.worldModel.product.Builder.ProductBuilder;
import com.ceiba.fashtoll.worldModel.product.Builder.ProductDirector;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductC_U_Request;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductResponse;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.mappers.ProductMapper;
import com.ceiba.fashtoll.worldModel.product.Observer.EventType;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEvent;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEventPublisher;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductTypeRepository;
import com.ceiba.fashtoll.worldModel.tag.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final ProductMapper productMapper;
    private final ProductEventPublisher productEventPublisher;

    private final ProductTypeRepository productTypeRepository;
    private final TagRepository tagRepository;

    @Autowired
    @Qualifier("simpleBuilder")
    private ObjectProvider<ProductBuilder> simpleBuilderProvider;
    @Autowired
    @Qualifier("simpleJsonBuilder")
    private ObjectProvider<ProductBuilder> simpleJsonBuilderProvider;
    @Autowired
    @Qualifier("completeBuilder")
    private ObjectProvider<ProductBuilder> completeBuilderProvider;
    private ProductDirector director;

    @Autowired
    public ProductService(ProductRepository productRepository, BrandRepository brandRepository, ProductMapper productMapper, ProductEventPublisher productEventPublisher) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.productMapper = productMapper;
        this.productEventPublisher = productEventPublisher;
        this.productTypeRepository = null;
        this.tagRepository = null;
    }

    public ProductService(ProductRepository productRepository, BrandRepository brandRepository, ProductTypeRepository productTypeRepository, ProductMapper productMapper, TagRepository tagRepository, ProductEventPublisher productEventPublisher) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.productMapper = productMapper;
        this.productEventPublisher = productEventPublisher;
        this.productTypeRepository = productTypeRepository;
        this.tagRepository = tagRepository;
    }

    public List<ProductResponse> getAllProducts() {
        this.logger.info("Se devolvieron todos los productos");

        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        this.logger.info("Se devolvio el producto '" + product.getName() + "' con id: " + id);

        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse createSimpleProduct(ProductC_U_Request request) {
        ProductBuilder builder = simpleBuilderProvider.getObject();
        this.director = new ProductDirector(builder);
        this.director.makeSimpleProduct(request);
        Product product = builder.getResult();

        Product savedProduct = this.productRepository.save(product);
        productEventPublisher.notify(new ProductEvent(savedProduct, EventType.CREATED));

        this.logger.info("Se notifico a los suscriptores de 'ProductEventPublisher'");
        this.logger.info("Se creo el producto '" + savedProduct.getName() + "' con id: " + savedProduct.getId());

        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse createCompleteProduct(ProductC_U_Request request) {
        ProductBuilder builder = completeBuilderProvider.getObject();
        this.director = new ProductDirector(builder);
        this.director.makeCompleteProduct(request);
        Product product = builder.getResult();

        Product savedProduct = this.productRepository.save(product);
        this.productEventPublisher.notify(new ProductEvent(savedProduct, EventType.CREATED));

        this.logger.info("Se notifico a los suscriptores de 'ProductEventPublisher'");
        this.logger.info("Se creo el producto '" + savedProduct.getName() + "' con id: " + savedProduct.getId());

        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateSimpleProduct(Long productId, ProductC_U_Request request) {
        ProductBuilder builder = simpleBuilderProvider.getObject();
        this.director = new ProductDirector(builder);
        this.director.adminUpdateSimpleProduct(productId, request);
        Product product = builder.getResult();

        Product savedProduct = productRepository.save(product);
        productEventPublisher.notify(new ProductEvent(savedProduct, EventType.UPDATED));

        this.logger.info("Se notifico a los suscriptores de 'ProductEventPublisher'");
        this.logger.info("Se actualizo el producto '" + savedProduct.getName() + "' con productId: " + savedProduct.getId());

        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateCompleteProduct(Long productId, ProductC_U_Request request) {
        ProductBuilder builder = completeBuilderProvider.getObject();
        this.director = new ProductDirector(builder);
        this.director.adminUpdateCompleteProduct(productId, request);
        Product product = builder.getResult();

        Product savedProduct = this.productRepository.save(product);
        this.productEventPublisher.notify(new ProductEvent(savedProduct, EventType.CREATED));

        this.logger.info("Se notifico a los suscriptores de 'ProductEventPublisher'");
        this.logger.info("Se creo el producto '" + savedProduct.getName() + "' con id: " + savedProduct.getId());

        return productMapper.toResponse(savedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        productRepository.delete(product);
        productEventPublisher.notify(new ProductEvent(product, EventType.DELETED));

        this.logger.info("Se notifico a los suscriptores de 'ProductEventPublisher'");
        this.logger.info("Se elimino el producto '" + product.getName() + "' con id: " + product.getId());
    }

    public List<ProductResponse> getProductsByBrand(Long brandId) {
        this.logger.info("Se devolvieron todos los productos de la marca con id: " + brandId);

        return productRepository.findByBrandId(brandId).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductByBrand(Long brandId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", productId));

        this.logger.info("Se devolvio el producto '" + product.getName() + "' con id: " + product.getId() + " de la marca con id: " + brandId);

        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse createSimpleBrandProduct(Long brandId, ProductC_U_Request request) {
        ProductBuilder builder = simpleBuilderProvider.getObject();
        this.director = new ProductDirector(builder);
        request.setBrandId(brandId);
        this.director.makeSimpleProduct(request);
        Product product = builder.getResult();

        Product savedProduct = productRepository.save(product);
        productEventPublisher.notify(new ProductEvent(savedProduct, EventType.CREATED));

        this.logger.info("Se notifico a los suscriptores de 'ProductEventPublisher'");
        this.logger.info("Se creo el producto '" + savedProduct.getName() + "' con id: " + savedProduct.getId() + " de la marca '" + savedProduct.getBrand().getName() + "'");

        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse createCompleteBrandProduct(Long brandId, ProductC_U_Request request) {
        ProductBuilder builder = completeBuilderProvider.getObject();
        this.director = new ProductDirector(builder);
        request.setBrandId(brandId);
        this.director.makeCompleteProduct(request);
        Product product = builder.getResult();

        Product savedProduct = productRepository.save(product);
        productEventPublisher.notify(new ProductEvent(savedProduct, EventType.CREATED));

        this.logger.info("Se notifico a los suscriptores de 'ProductEventPublisher'");
        this.logger.info("Se creo el producto '" + savedProduct.getName() + "' con id: " + savedProduct.getId() + " de la marca '" + savedProduct.getBrand().getName() + "'");

        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateSimpleBrandProduct(Long brandId, Long productId, ProductC_U_Request request) {
        ProductBuilder builder = simpleBuilderProvider.getObject();
        this.director = new ProductDirector(builder);
        this.director.updateSimpleProduct(brandId, productId, request);
        Product product = builder.getResult();

        Product savedProduct = productRepository.save(product);
        productEventPublisher.notify(new ProductEvent(savedProduct, EventType.UPDATED));

        this.logger.info("Se notifico a los suscriptores de 'ProductEventPublisher'");
        this.logger.info("Se actualizo el producto '" + product.getName() + "' con id: " + product.getId() + " de la marca con id: " + brandId);

        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateCompleteBrandProduct(Long brandId, Long productId, ProductC_U_Request request) {
        ProductBuilder builder = completeBuilderProvider.getObject();
        this.director = new ProductDirector(builder);
        this.director.updateCompleteProduct(brandId, productId, request);
        Product product = builder.getResult();

        Product savedProduct = productRepository.save(product);
        productEventPublisher.notify(new ProductEvent(savedProduct, EventType.UPDATED));

        this.logger.info("Se notifico a los suscriptores de 'ProductEventPublisher'");
        this.logger.info("Se actualizo el producto '" + product.getName() + "' con id: " + product.getId() + " de la marca con id: " + brandId);

        return productMapper.toResponse(savedProduct);
    }

    public void deleteBrandProduct(Long brandId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        productRepository.delete(product);
        productEventPublisher.notify(new ProductEvent(product, EventType.DELETED));

        this.logger.info("Se notifico a los suscriptores de 'ProductEventPublisher'");
        this.logger.info("Se elimino el producto '" + product.getName() + "' con id: " + product.getId() + " de la marca con id: " + brandId);
    }

    @Transactional
    public void injectBrandProductFromJson(String brandName, List<ProductC_U_Request> productsDTOs) {
        Brand brand = brandRepository.findByName(brandName)
                .orElseThrow(() -> new ResourceNotFoundException("Brand","id", brandName));

        this.logger.info("Este builder no asocia Brand al producto, pues busca la marca una sola vez");

        for(ProductC_U_Request productDTO : productsDTOs) {
            ProductBuilder builder = simpleJsonBuilderProvider.getObject();
            this.director = new ProductDirector(builder);
            this.director.makeJsonSimpleProduct(productDTO, brand);
            Product product = builder.getResult();

            Product savedProduct = this.productRepository.save(product);
            this.productEventPublisher.notify(new ProductEvent(savedProduct, EventType.CREATED));
        }
    }
}
