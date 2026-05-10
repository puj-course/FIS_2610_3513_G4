package com.ceiba.fashtoll.worldModel.review.mapper;

import com.ceiba.fashtoll.worldModel.review.dto.ReviewResponse;
import com.ceiba.fashtoll.worldModel.review.entity.BrandReview;
import com.ceiba.fashtoll.worldModel.review.entity.ProductReview;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toResponse(BrandReview review) {
        if (review == null) return null;
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setClientId(review.getClient().getId());
        response.setClientName(review.getClient().getName());
        response.setTargetId(review.getBrand().getId());
        response.setComment(review.getComment());
        response.setRating(review.getRating());
        response.setCreatedAt(review.getCreatedAt());
        response.setUpdatedAt(review.getUpdatedAt());
        return response;
    }

    public ReviewResponse toResponse(ProductReview review) {
        if (review == null) return null;
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setClientId(review.getClient().getId());
        response.setClientName(review.getClient().getName());
        response.setTargetId(review.getProduct().getId());
        response.setComment(review.getComment());
        response.setRating(review.getRating());
        response.setCreatedAt(review.getCreatedAt());
        response.setUpdatedAt(review.getUpdatedAt());
        return response;
    }
}
