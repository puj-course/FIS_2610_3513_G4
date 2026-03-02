package com.ceiba.fashtoll.service;

import com.ceiba.fashtoll.entity.Brand;
import com.ceiba.fashtoll.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Brand getBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada: " + id));
    }

    public Brand createBrand(Brand brand) {
        if (brand.getFollowers() == null) brand.setFollowers(0);
        if (brand.getRating() == null) brand.setRating(0.0);
        if (brand.getIsVerified() == null) brand.setIsVerified(false);
        return brandRepository.save(brand);
    }

    public Brand updateBrand(Long id, Brand updatedBrand) {
        Brand brand = getBrandById(id);
        brand.setName(updatedBrand.getName());
        brand.setEmail(updatedBrand.getEmail());
        brand.setPassword(updatedBrand.getPassword());
        brand.setPictureUrl(updatedBrand.getPictureUrl());
        brand.setLinkOfficial(updatedBrand.getLinkOfficial());
        brand.setFollowers(updatedBrand.getFollowers());
        brand.setRating(updatedBrand.getRating());
        brand.setIsVerified(updatedBrand.getIsVerified());
        return brandRepository.save(brand);
    }

    public void deleteBrand(Long id) {
        Brand brand = getBrandById(id);
        brandRepository.delete(brand);
    }
}
