package com.ceiba.fashtoll.worldModel.product.services;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.brand.BrandRepository;
import com.ceiba.fashtoll.worldModel.product.Builder.ProductBuilder;
import com.ceiba.fashtoll.worldModel.product.Builder.ProductDirector;
import com.ceiba.fashtoll.worldModel.product.Builder.builders.SimpleProductBuilder;
import com.ceiba.fashtoll.worldModel.product.Builder.ProductDetails;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductAdminUpdateRequest;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductCreateRequest;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductResponse;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductUpdateRequest;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.entities.ProductType;
import com.ceiba.fashtoll.worldModel.product.mappers.ProductMapper;
import com.ceiba.fashtoll.worldModel.product.Observer.EventType;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEvent;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEventPublisher;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductTypeRepository;
import com.ceiba.fashtoll.worldModel.tag.Tag;
import com.ceiba.fashtoll.worldModel.tag.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductMapper productMapper;
    private final TagRepository tagRepository;
    private final ProductEventPublisher productEventPublisher;

    @Autowired
    private ObjectProvider<ProductBuilder> builderProvider;
    private ProductDirector director;

    @Autowired
    public ProductService(ProductRepository productRepository, BrandRepository brandRepository,
                          ProductTypeRepository productTypeRepository, ProductMapper productMapper,
                          TagRepository tagRepository, ProductEventPublisher productEventPublisher, ProductDirector director) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.productTypeRepository = productTypeRepository;
        this.productMapper = productMapper;
        this.tagRepository = tagRepository;
        this.productEventPublisher = productEventPublisher;
        this.director = director;
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
    public ProductResponse createProduct(ProductCreateRequest request) {

        ProductBuilder builder = builderProvider.getObject();
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
    public ProductResponse updateProduct(Long id, ProductAdminUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        productMapper.updateEntityFromAdmin(request, product);

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Marca", "id", request.getBrandId()));
            product.setBrand(brand);
        }

        if (request.getProductTypeId() != null) {
            ProductType productType = productTypeRepository.findById(request.getProductTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de producto", "id", request.getProductTypeId()));
            product.setProductType(productType);
        }

        // Imágenes
        if (request.getImageUrls() != null) {
            product.getImages().clear(); // Se borran las antiguas
            request.getImageUrls().forEach(url -> {
                product.getImages().add(url);
            });
        }

        // Tags
        if (request.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            product.setTags(tags);
        }

        Product savedProduct = productRepository.save(product);
        productEventPublisher.notify(new ProductEvent(savedProduct, EventType.UPDATED));

        this.logger.info("Se notifico a los suscriptores de 'ProductEventPublisher'");
        this.logger.info("Se actualizo el producto '" + savedProduct.getName() + "' con id: " + savedProduct.getId());

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
    public ProductResponse createBrandProduct(Long brandId, ProductCreateRequest request) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand","id",request.getBrandId()));

        ProductType productType = productTypeRepository.findById(request.getProductTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("ProductType","id", request.getProductTypeId()));

        Product product = productMapper.toEntity(request);
        product.setBrand(brand);
        product.setProductType(productType);

        if (product.getAvailable() == null) product.setAvailable(true);
        if (product.getRating() == null) product.setRating(0.0);

        // Imágenes
        if (request.getImageUrls() != null) {
            product.setImages(request.getImageUrls());
        }

        // Tags
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            product.setTags(tags);
        }

        Product savedProduct = productRepository.save(product);
        productEventPublisher.notify(new ProductEvent(savedProduct, EventType.CREATED));

        this.logger.info("Se notifico a los suscriptores de 'ProductEventPublisher'");
        this.logger.info("Se creo el producto '" + product.getName() + "' con id: " + product.getId() + " de la marca '" + brand.getName() + "'");

        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateBrandProduct(Long brandId, Long productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        productMapper.updateEntityFromBrand(request, product);

        if (request.getProductTypeId() != null) {
            ProductType productType = productTypeRepository.findById(request.getProductTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductType","id", request.getProductTypeId()));
            product.setProductType(productType);
        }

        // Images
        if (request.getImageUrls() != null) {
            product.getImages().clear(); // Se borran las antiguas
            request.getImageUrls().forEach(url -> {
                product.getImages().add(url);
            });
        }

        // Tags
        if (request.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            product.setTags(tags);
        }

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
    public void injectBrandProductFromJson(String brandName, List<ProductCreateRequest> productsDTOs) {
        Brand brand = brandRepository.findByName(brandName)
                .orElseThrow(() -> new ResourceNotFoundException("Brand","id", brandName));

        for(ProductCreateRequest productDTO : productsDTOs) {
            ProductType productType = this.productTypeRepository.findById(productDTO.getProductTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductType","id", productDTO.getProductTypeId()));

            Product product = this.productMapper.toEntity(productDTO);
            product.setBrand(brand);
            product.setProductType(productType);

            if (product.getAvailable() == null) product.setAvailable(true);
            if (product.getRating() == null) product.setRating(0.0);

            // Imágenes
            if (productDTO.getImageUrls() != null) {
                product.setImages(productDTO.getImageUrls());
            }

            // Tags
            if (productDTO.getTagIds() != null && !productDTO.getTagIds().isEmpty()) {
                Set<Tag> tags = new HashSet<>(tagRepository.findAllById(productDTO.getTagIds()));
                product.setTags(tags);
            }

            Product savedProduct = this.productRepository.save(product);
            this.productEventPublisher.notify(new ProductEvent(savedProduct, EventType.CREATED));
        }
    }
}
