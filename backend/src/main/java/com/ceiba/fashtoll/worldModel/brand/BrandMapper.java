package com.ceiba.fashtoll.worldModel.brand;

import com.ceiba.fashtoll.worldModel.brand.dtos.*;
import com.ceiba.fashtoll.worldModel.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BrandMapper {

    private final ReviewMapper reviewMapper;

    public BrandResponse toResponse(Brand brand) {
        if (brand == null) return null;
        BrandResponse response = new BrandResponse();
        response.setId(brand.getId());
        response.setName(brand.getName());
        response.setPictureURL(brand.getPictureURL());
        response.setLinkOfficial(brand.getLinkOfficial());
        response.setFollowers(brand.getFollowers());
        response.setRating(brand.getRating());
        response.setReviewCount(brand.getReviewCount());
        response.setIsVerified(brand.getIsVerified());

        if (brand.getReviews() != null) {
            response.setReviews(
                    brand.getReviews().stream()
                            .map(reviewMapper::toResponse)
                            .collect(Collectors.toList())
            );
        } else {
            response.setReviews(new ArrayList<>());
        }

        return response;
    }

    public BrandPublicResponse toPublicResponse(Brand brand) {
        if (brand == null) return null;
        BrandPublicResponse response = new BrandPublicResponse();
        response.setId(brand.getId());
        response.setName(brand.getName());
        response.setEmail(brand.getEmail());
        response.setPictureURL(brand.getPictureURL());
        response.setLinkOfficial(brand.getLinkOfficial());
        response.setFollowers(brand.getFollowers());
        response.setRating(brand.getRating());
        response.setReviewCount(brand.getReviewCount());
        response.setIsVerified(brand.getIsVerified());

        if (brand.getReviews() != null) {
            response.setReviews(
                    brand.getReviews().stream()
                            .map(reviewMapper::toResponse)
                            .collect(Collectors.toList())
            );
        } else {
            response.setReviews(new ArrayList<>());
        }

        return response;
    }

    public BrandProfileResponse toProfileResponse(Brand brand) {
        if (brand == null) return null;
        BrandProfileResponse response = new BrandProfileResponse();
        response.setName(brand.getName());
        response.setEmail(brand.getEmail());
        response.setPictureURL(brand.getPictureURL());
        response.setLinkOfficial(brand.getLinkOfficial());
        response.setFollowers(brand.getFollowers());
        response.setRating(brand.getRating());
        response.setReviewCount(brand.getReviewCount());
        response.setIsVerified(brand.getIsVerified());

        if (brand.getReviews() != null) {
            response.setReviews(
                    brand.getReviews().stream()
                            .map(reviewMapper::toResponse)
                            .collect(Collectors.toList())
            );
        } else {
            response.setReviews(new ArrayList<>());
        }

        return response;
    }

    public Brand toEntity(BrandCreateRequest request) {
        if (request == null) return null;
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setPictureURL(request.getPictureURL());
        brand.setLinkOfficial(request.getLinkOfficial());

        return brand;
    }

    public void updateEntityFromAdmin(BrandAdminUpdateRequest request, Brand brand) {
        if (request == null || brand == null) return;
        brand.setName(request.getName());
        brand.setPictureURL(request.getPictureURL());
        brand.setLinkOfficial(request.getLinkOfficial());
        brand.setIsVerified(request.getIsVerified());
    }
    public void updateEntityFromProfile(BrandProfileUpdateRequest request, Brand brand) {
        if (request == null || brand == null) return;
        brand.setName(request.getName());
        brand.setPictureURL(request.getPictureURL());
        brand.setLinkOfficial(request.getLinkOfficial());
    }
}

