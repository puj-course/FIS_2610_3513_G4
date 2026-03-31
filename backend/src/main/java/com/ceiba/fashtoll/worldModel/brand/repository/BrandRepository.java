package com.ceiba.fashtoll.worldModel.brand.repository;

import com.ceiba.fashtoll.worldModel.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
