package com.ceiba.fashtoll.worldModel.brand;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.security.auth.AuthService;
import com.ceiba.fashtoll.security.auth.dtos.RegisterRequest;
import com.ceiba.fashtoll.utilities.enums.Role;
import com.ceiba.fashtoll.worldModel.brand.dtos.*;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductCreateRequest;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductResponse;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductUpdateRequest;
import com.ceiba.fashtoll.worldModel.product.services.ProductService;
import com.ceiba.fashtoll.worldModel.user.User;
import com.ceiba.fashtoll.worldModel.user.UserService;
import com.ceiba.fashtoll.worldModel.user.dtos.PasswordChangeRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final AuthService authService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public BrandService(BrandRepository brandRepository, BrandMapper brandMapper, AuthService authService, UserService userService, ProductService productService) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
        this.authService = authService;
        this.userService = userService;
        this.productService = productService;
    }

    public List<BrandResponse> getAllBrands() {
        this.logger.info("Se devolvieron todas las marcas");

        return brandRepository.findAll().stream()
                .map(brandMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<BrandPublicResponse> getAllPublicBrands() {
        this.logger.info("Se devolvieron todas las marcas publicas");

        return brandRepository.findAll().stream()
                .map(brandMapper::toPublicResponse)
                .collect(Collectors.toList());
    }

    public BrandResponse getBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id",id));

        this.logger.info("Se devolvio la marca '" + brand.getName() + "' con id: " + brand.getId());

        return brandMapper.toResponse(brand);
    }

    public BrandPublicResponse getPublicBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id",id));

        this.logger.info("Se devolvio la marca publica '" + brand.getName() + "' con id: " + brand.getId());

        return brandMapper.toPublicResponse(brand);
    }

    @Transactional
    public BrandResponse createBrand(BrandCreateRequest request) {
        Brand brand = brandMapper.toEntity(request);
        brand.setFollowers(0);
        brand.setRating(0.0);
        brand.setIsVerified(false);
        //Brand savedBrand = brandRepository.save(brand);
        brandRepository.save(brand);
        //return brandMapper.toResponse(savedBrand);

        this.logger.info("Se creo la marca '" + brand.getName() + "' con id: " + brand.getId());

        return brandMapper.toResponse(brand);
    }

    @Transactional
    public BrandResponse updateBrandAdmin(Long id, BrandAdminUpdateRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id",id));

        brandMapper.updateEntityFromAdmin(request, brand);
        Brand savedBrand = brandRepository.save(brand);

        this.logger.info("Se actualizo la marca '" + brand.getName() + "' con id: " + brand.getId());

        return brandMapper.toResponse(savedBrand);
    }

    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id",id));
        brandRepository.delete(brand);

        this.logger.info("Se elimino la marca '" + brand.getName() + "' con id: " + brand.getId());
    }

    public BrandProfileResponse getProfile(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id", brandId));

        this.logger.info("Se devolvio el perfil de la marca '" + brand.getName() + "' con id: " + brand.getId());

        return brandMapper.toProfileResponse(brand);
    }

    @Transactional
    public BrandProfileResponse updateProfile(Long brandId, BrandProfileUpdateRequest request) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id",brandId));

        brandMapper.updateEntityFromProfile(request, brand);
        Brand savedBrand = brandRepository.save(brand);

        this.logger.info("Se actualizo el perfil de la marca '" + brand.getName() + "' con id: " + brand.getId());

        return brandMapper.toProfileResponse(savedBrand);
    }

    public boolean changePassword(Authentication authentication, PasswordChangeRequestDTO request){
        User user = (User) authentication.getPrincipal();
        userService.changePassword(user.getId(), request);

        return true;
    }

    public List<ProductResponse> getMyProducts(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return productService.getProductsByBrand(user.getId());
    }

    public ProductResponse getMyProduct(Authentication authentication, Long id) {
        User user = (User) authentication.getPrincipal();
        return productService.getProductByBrand(user.getId(), id);
    }

    public ResponseEntity<ProductResponse> createMyProduct(Authentication authentication, ProductCreateRequest request) {
        User user = (User) authentication.getPrincipal();
        ProductResponse newProduct = new ProductResponse();
        if (user != null) {
            newProduct = productService.createBrandProduct(user.getId(), request);
        } else {
            this.logger.error("No hay una marca para asociar al producto a crear");
            throw new ResourceNotFoundException("Marca","id",user.getId());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    public ProductResponse updateMyProduct(Authentication authentication, Long id, ProductUpdateRequest request) {
        User user = (User) authentication.getPrincipal();
        return productService.updateBrandProduct(user.getId(), id, request);
    }

    public ResponseEntity<Void> deleteMyProduct(Authentication authentication, Long id) {
        User user = (User) authentication.getPrincipal();
        productService.deleteBrandProduct(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public void verifyBrand(Long id, boolean verified) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id",id));
        brand.setIsVerified(verified);
        brandRepository.save(brand);

        this.logger.info("Se verifico la  marca '" + brand.getName() + "' con id: " + brand.getId());
    }

    public void injectBrandsFromJSON(List<BrandDTO>  brandDTOs) {
        for (BrandDTO brandDTO : brandDTOs) {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(brandDTO.email());
            registerRequest.setPassword(brandDTO.password());
            registerRequest.setRole(Role.categorize(brandDTO.role()));
            registerRequest.setName(brandDTO.name());
            registerRequest.setLinkOfficial(brandDTO.linkOfficial());
            registerRequest.setPictureURL(brandDTO.pictureURL());

            this.authService.brandRegister(registerRequest);
        }
    }
}
