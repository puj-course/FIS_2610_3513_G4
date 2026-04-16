package com.ceiba.fashtoll.worldModel.brand;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.security.auth.AuthService;
import com.ceiba.fashtoll.security.auth.dtos.RegisterRequest;
import com.ceiba.fashtoll.utilities.enums.Role;
import com.ceiba.fashtoll.worldModel.admin.dtos.AdminOperationResponse;
import com.ceiba.fashtoll.worldModel.brand.dtos.*;
import com.ceiba.fashtoll.worldModel.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final AuthService authService;

    @Autowired
    public BrandService(BrandRepository brandRepository, BrandMapper brandMapper, AuthService authService) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
        this.authService = authService;
    }

    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<BrandPublicResponse> getAllPublicBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toPublicResponse)
                .collect(Collectors.toList());
    }

    public BrandResponse getBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id",id));

        return brandMapper.toResponse(brand);
    }

    public BrandPublicResponse getPublicBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id",id));

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

        return brandMapper.toResponse(brand);
    }

    @Transactional
    public BrandResponse updateBrandAdmin(Long id, BrandAdminUpdateRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id",id));

        brandMapper.updateEntityFromAdmin(request, brand);
        Brand savedBrand = brandRepository.save(brand);

        return brandMapper.toResponse(savedBrand);
    }

    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id",id));
        brandRepository.delete(brand);
    }

    public BrandProfileResponse getProfile(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id", brandId));

        return brandMapper.toProfileResponse(brand);
    }

    @Transactional
    public BrandProfileResponse updateProfile(Long brandId, BrandProfileUpdateRequest request) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id",brandId));

        brandMapper.updateEntityFromProfile(request, brand);
        Brand savedBrand = brandRepository.save(brand);

        return brandMapper.toProfileResponse(savedBrand);
    }

    @Transactional
    public void verifyBrand(Long id, boolean verified) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("marca","id",id));
        brand.setIsVerified(verified);
        brandRepository.save(brand);
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
