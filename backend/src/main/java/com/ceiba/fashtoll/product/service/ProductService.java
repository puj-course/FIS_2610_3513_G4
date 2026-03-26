package com.ceiba.fashtoll.product.service;

import com.ceiba.fashtoll.brand.entity.Brand;
import com.ceiba.fashtoll.brand.repository.BrandRepository;
import com.ceiba.fashtoll.product.dto.*;
import com.ceiba.fashtoll.product.entity.Product;
import com.ceiba.fashtoll.product.entity.ProductImage;
import com.ceiba.fashtoll.product.entity.ProductType;
import com.ceiba.fashtoll.product.mapper.ProductMapper;
import com.ceiba.fashtoll.product.repository.ProductRepository;
import com.ceiba.fashtoll.product.repository.ProductTypeRepository;
import com.ceiba.fashtoll.tag.entity.Tag;
import com.ceiba.fashtoll.tag.repository.TagRepository;
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

    @Autowired
    public ProductService(ProductRepository productRepository, BrandRepository brandRepository,
                          ProductTypeRepository productTypeRepository, ProductMapper productMapper,
                          TagRepository tagRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.productTypeRepository = productTypeRepository;
        this.productMapper = productMapper;
        this.tagRepository = tagRepository;
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada: " + request.getBrandId()));

        ProductType productType = productTypeRepository.findById(request.getProductTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de producto no encontrado: " + request.getProductTypeId()));

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
        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductAdminUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));

        productMapper.updateEntityFromAdmin(request, product);

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada: " + request.getBrandId()));
            product.setBrand(brand);
        }

        if (request.getProductTypeId() != null) {
            ProductType productType = productTypeRepository.findById(request.getProductTypeId())
                    .orElseThrow(() -> new RuntimeException("Tipo de producto no encontrado: " + request.getProductTypeId()));
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
        return productMapper.toResponse(savedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        productRepository.delete(product);
    }

    public List<ProductResponse> getProductsByBrand(Long brandId) {
        return productRepository.findByBrandId(brandId).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductByBrand(Long brandId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productId));

        if (!product.getBrand().getId().equals(brandId)) {
            throw new RuntimeException("No tiene permisos para ver este producto");
        }

        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse createBrandProduct(Long brandId, ProductCreateRequest request) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada: " + brandId));

        ProductType productType = productTypeRepository.findById(request.getProductTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de producto no encontrado: " + request.getProductTypeId()));

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
        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateBrandProduct(Long brandId, Long productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productId));

        if (!product.getBrand().getId().equals(brandId)) {
            throw new RuntimeException("No tiene permisos para actualizar este producto");
        }

        productMapper.updateEntityFromBrand(request, product);

        if (request.getProductTypeId() != null) {
            ProductType productType = productTypeRepository.findById(request.getProductTypeId())
                    .orElseThrow(() -> new RuntimeException("Tipo de producto no encontrado: " + request.getProductTypeId()));
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
        return productMapper.toResponse(savedProduct);
    }

    public void deleteBrandProduct(Long brandId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productId));

        if (!product.getBrand().getId().equals(brandId)) {
            throw new RuntimeException("No tiene permisos para eliminar este producto");
        }

        productRepository.delete(product);
    }
}
