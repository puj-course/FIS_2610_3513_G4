package com.ceiba.fashtoll.brand.service;

import com.ceiba.fashtoll.brand.dto.BrandDTO;
import com.ceiba.fashtoll.brand.entity.Brand;
import com.ceiba.fashtoll.user.entity.User;
import com.ceiba.fashtoll.brand.mapper.BrandMapper;
import com.ceiba.fashtoll.brand.repository.BrandRepository;
import com.ceiba.fashtoll.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<BrandDTO> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BrandDTO getBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada: " + id));
        return brandMapper.toDTO(brand);
    }

    public BrandDTO createBrand(BrandDTO brandDTO) {
        Brand brand = brandMapper.toEntity(brandDTO);
        if (brand.getFollowers() == null) brand.setFollowers(0);
        if (brand.getRating() == null) brand.setRating(0.0);
        if (brand.getIsVerified() == null) brand.setIsVerified(false);
        if (brandDTO.getUserId() != null) {
            User user = userRepository.findById(brandDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + brandDTO.getUserId()));
            brand.setUser(user);
        }
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toDTO(savedBrand);
    }

    public BrandDTO updateBrand(Long id, BrandDTO updatedBrandDTO) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada: " + id));
        brand.setName(updatedBrandDTO.getName());
        brand.setPictureUrl(updatedBrandDTO.getPictureUrl());
        brand.setLinkOfficial(updatedBrandDTO.getLinkOfficial());
        brand.setFollowers(updatedBrandDTO.getFollowers());
        brand.setRating(updatedBrandDTO.getRating());
        brand.setIsVerified(updatedBrandDTO.getIsVerified());
        if (updatedBrandDTO.getUserId() != null) {
            User user = userRepository.findById(updatedBrandDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + updatedBrandDTO.getUserId()));
            brand.setUser(user);
        }
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toDTO(savedBrand);
    }

    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada: " + id));
        brandRepository.delete(brand);
    }
}
