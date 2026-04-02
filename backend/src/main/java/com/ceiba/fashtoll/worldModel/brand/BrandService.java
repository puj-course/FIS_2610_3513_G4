package com.ceiba.fashtoll.worldModel.brand;

import com.ceiba.fashtoll.worldModel.brand.dtos.*;
import com.ceiba.fashtoll.worldModel.user.User;
import com.ceiba.fashtoll.worldModel.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final UserRepository userRepository;
    private final BrandMapper brandMapper;

    @Autowired
    public BrandService(BrandRepository brandRepository, UserRepository userRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.userRepository = userRepository;
        this.brandMapper = brandMapper;
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
                .orElseThrow(() -> new RuntimeException("Marca no encontrada: " + id));
        return brandMapper.toResponse(brand);
    }

    public BrandPublicResponse getPublicBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada: " + id));
        return brandMapper.toPublicResponse(brand);
    }

    @Transactional
    public BrandResponse createBrand(BrandCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + request.getUserId()));

        Brand brand = brandMapper.toEntity(request);
        brand.setUser(user);
        brand.setFollowers(0);
        brand.setRating(0.0);
        brand.setIsVerified(false);
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toResponse(savedBrand);
    }

    @Transactional
    public BrandResponse updateBrandAdmin(Long id, BrandAdminUpdateRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada: " + id));

        brandMapper.updateEntityFromAdmin(request, brand);
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toResponse(savedBrand);
    }

    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada: " + id));
        brandRepository.delete(brand);
    }

    public BrandProfileResponse getProfile(Long userId) {
        Brand brand = brandRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        return brandMapper.toProfileResponse(brand);
    }

    @Transactional
    public BrandProfileResponse updateProfile(Long userId, BrandProfileUpdateRequest request) {
        Brand brand = brandRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        brandMapper.updateEntityFromProfile(request, brand);
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toProfileResponse(savedBrand);
    }

    @Transactional
    public void verifyBrand(Long id, boolean verified) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        brand.setIsVerified(verified);
        brandRepository.save(brand);
    }
}
