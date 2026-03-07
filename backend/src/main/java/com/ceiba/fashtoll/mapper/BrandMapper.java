package com.ceiba.fashtoll.mapper;

import com.ceiba.fashtoll.dto.BrandDTO;
import com.ceiba.fashtoll.entity.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandDTO toDTO(Brand brand) {
        if (brand == null) return null;
        BrandDTO dto = new BrandDTO();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        dto.setPictureUrl(brand.getPictureUrl());
        dto.setLinkOfficial(brand.getLinkOfficial());
        dto.setFollowers(brand.getFollowers());
        dto.setRating(brand.getRating());
        dto.setIsVerified(brand.getIsVerified());
        dto.setUserId(brand.getUser() != null ? brand.getUser().getId() : null);
        return dto;
    }

    public Brand toEntity(BrandDTO dto) {
        if (dto == null) return null;
        Brand brand = new Brand();
        brand.setId(dto.getId());
        brand.setName(dto.getName());
        brand.setPictureUrl(dto.getPictureUrl());
        brand.setLinkOfficial(dto.getLinkOfficial());
        brand.setFollowers(dto.getFollowers());
        brand.setRating(dto.getRating());
        brand.setIsVerified(dto.getIsVerified());
        return brand;
    }
}
