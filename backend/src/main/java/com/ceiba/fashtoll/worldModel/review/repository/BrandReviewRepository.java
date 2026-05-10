package com.ceiba.fashtoll.worldModel.review.repository;

import com.ceiba.fashtoll.worldModel.review.entity.BrandReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandReviewRepository extends JpaRepository<BrandReview, Long> {
    List<BrandReview> findByClientId(Long clientId);
    Optional<BrandReview> findByClientIdAndBrandId(Long clientId, Long brandId);
    List<BrandReview> findByBrandId(Long brandId);
    boolean existsByClientIdAndBrandId(Long clientId, Long brandId);
}
