package com.ceiba.fashtoll.worldModel.review.repository;

import com.ceiba.fashtoll.worldModel.review.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findByClientId(Long clientId);
    Optional<ProductReview> findByClientIdAndProductId(Long clientId, Long productId);
    List<ProductReview> findByProductId(Long productId);
    boolean existsByClientIdAndProductId(Long clientId, Long productId);
}
