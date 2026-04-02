package com.ceiba.fashtoll.worldModel.brand;

import com.ceiba.fashtoll.worldModel.brand.dtos.*;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandResponse toResponse(Brand brand) {
        if (brand == null) return null;
        BrandResponse response = new BrandResponse();
        response.setId(brand.getId());
        response.setName(brand.getName());
        response.setPictureUrl(brand.getPictureUrl());
        response.setLinkOfficial(brand.getLinkOfficial());
        response.setFollowers(brand.getFollowers());
        response.setRating(brand.getRating());
        response.setIsVerified(brand.getIsVerified());
        response.setUserId(brand.getUser() != null ? brand.getUser().getId() : null);
        return response;
    }

    public BrandPublicResponse toPublicResponse(Brand brand) {
        if (brand == null) return null;
        BrandPublicResponse response = new BrandPublicResponse();
        response.setName(brand.getName());
        response.setEmail(brand.getUser() != null ? brand.getUser().getEmail() : null);
        response.setPictureUrl(brand.getPictureUrl());
        response.setLinkOfficial(brand.getLinkOfficial());
        response.setFollowers(brand.getFollowers());
        response.setRating(brand.getRating());
        response.setIsVerified(brand.getIsVerified());
        return response;
    }

    public BrandProfileResponse toProfileResponse(Brand brand) {
        if (brand == null) return null;
        BrandProfileResponse response = new BrandProfileResponse();
        response.setName(brand.getName());
        response.setEmail(brand.getUser() != null ? brand.getUser().getEmail() : null);
        response.setPictureUrl(brand.getPictureUrl());
        response.setLinkOfficial(brand.getLinkOfficial());
        response.setFollowers(brand.getFollowers());
        response.setRating(brand.getRating());
        response.setIsVerified(brand.getIsVerified());
        return response;
    }

    public Brand toEntity(BrandCreateRequest request) {
        if (request == null) return null;
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setPictureUrl(request.getPictureUrl());
        brand.setLinkOfficial(request.getLinkOfficial());
        return brand;
    }

    public void updateEntityFromAdmin(BrandAdminUpdateRequest request, Brand brand) {
        if (request == null || brand == null) return;
        brand.setName(request.getName());
        brand.setPictureUrl(request.getPictureUrl());
        brand.setLinkOfficial(request.getLinkOfficial());
        brand.setIsVerified(request.getIsVerified());
    }
    public void updateEntityFromProfile(BrandProfileUpdateRequest request, Brand brand) {
        if (request == null || brand == null) return;
        brand.setName(request.getName());
        brand.setPictureUrl(request.getPictureUrl());
        brand.setLinkOfficial(request.getLinkOfficial());
    }
}
