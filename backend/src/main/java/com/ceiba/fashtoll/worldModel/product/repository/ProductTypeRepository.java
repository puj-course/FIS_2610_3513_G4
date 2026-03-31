package com.ceiba.fashtoll.worldModel.product.repository;

import com.ceiba.fashtoll.worldModel.product.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
}
