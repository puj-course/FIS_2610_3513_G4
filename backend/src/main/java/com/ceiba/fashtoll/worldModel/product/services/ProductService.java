package com.ceiba.fashtoll.worldModel.product.services;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.brand.BrandRepository;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductAdminUpdateRequest;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductCreateRequest;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductResponse;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductUpdateRequest;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.entities.ProductImage;
import com.ceiba.fashtoll.worldModel.product.entities.ProductType;
import com.ceiba.fashtoll.worldModel.product.mappers.ProductMapper;
import com.ceiba.fashtoll.worldModel.product.observer.EventType;
import com.ceiba.fashtoll.worldModel.product.observer.ProductEvent;
import com.ceiba.fashtoll.worldModel.product.observer.ProductEventPublisher;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductTypeRepository;
import com.ceiba.fashtoll.worldModel.tag.Tag;
import com.ceiba.fashtoll.worldModel.tag.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductMapper productMapper;
    private final TagRepository tagRepository;
    private final ProductEventPublisher productEventPublisher;

    @Autowired
    public ProductService(ProductRepository productRepository, BrandRepository brandRepository,
                          ProductTypeRepository productTypeRepository, ProductMapper productMapper,
                          TagRepository tagRepository, ProductEventPublisher productEventPublisher) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.productTypeRepository = productTypeRepository;
        this.productMapper = productMapper;
        this.tagRepository = tagRepository;
        this.productEventPublisher = productEventPublisher;
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Marca","id",request.getBrandId()));

        ProductType productType = productTypeRepository.findById(request.getProductTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de producto","id", request.getProductTypeId()));

        Product product = productMapper.toEntity(request);
        product.setBrand(brand);
        product.setProductType(productType);

        if (product.getAvailable() == null) product.setAvailable(true);
        if (product.getRating() == null) product.setRating(0.0);

        // Imágenes
        if (request.getImageUrls() != null) {
            List<ProductImage> images = request.getImageUrls().stream()
                    .map(url -> {
                        ProductImage img = new ProductImage();
                        img.setImageUrl(url);
                        img.setProduct(product);
                        return img;
                    }).collect(Collectors.toList());
            product.setImages(images);
        }

        // Tags
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            product.setTags(tags);
        }

        Product savedProduct = productRepository.save(product);
        productEventPublisher.notify(new ProductEvent(savedProduct, EventType.CREATED));
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
                ProductImage img = new ProductImage();
                img.setImageUrl(url);
                img.setProduct(product);
                product.getImages().add(img);
            });
        }

        // Tags
        if (request.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            product.setTags(tags);
        }

        Product savedProduct = productRepository.save(product);
        productEventPublisher.notify(new ProductEvent(savedProduct, EventType.UPDATED));
        return productMapper.toResponse(savedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        productRepository.delete(product);
        productEventPublisher.notify(new ProductEvent(product, EventType.DELETED));
    }

    public List<ProductResponse> getProductsByBrand(Long brandId) {
        return productRepository.findByBrandId(brandId).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductByBrand(Long brandId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", productId));

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
            List<ProductImage> images = request.getImageUrls().stream()
                    .map(url -> {
                        ProductImage img = new ProductImage();
                        img.setImageUrl(url);
                        img.setProduct(product);
                        return img;
                    }).collect(Collectors.toList());
            product.setImages(images);
        }

        // Tags
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            product.setTags(tags);
        }

        Product savedProduct = productRepository.save(product);
        productEventPublisher.notify(new ProductEvent(savedProduct, EventType.CREATED));
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
            product.getImages().clear();
            request.getImageUrls().forEach(url -> {
                ProductImage img = new ProductImage();
                img.setImageUrl(url);
                img.setProduct(product);
                product.getImages().add(img);
            });
        }

        // Tags
        if (request.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            product.setTags(tags);
        }

        Product savedProduct = productRepository.save(product);
        productEventPublisher.notify(new ProductEvent(savedProduct, EventType.UPDATED));
        return productMapper.toResponse(savedProduct);
    }

    public void deleteBrandProduct(Long brandId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        productRepository.delete(product);
        productEventPublisher.notify(new ProductEvent(product, EventType.DELETED));
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
                List<ProductImage> images = productDTO.getImageUrls().stream()
                        .map(url -> {
                            ProductImage img = new ProductImage();
                            img.setImageUrl(url);
                            img.setProduct(product);
                            return img;
                        }).collect(Collectors.toList());
                product.setImages(images);
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
