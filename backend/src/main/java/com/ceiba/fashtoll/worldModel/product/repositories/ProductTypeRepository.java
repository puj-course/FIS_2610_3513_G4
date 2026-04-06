package com.ceiba.fashtoll.worldModel.product.repositories;

import com.ceiba.fashtoll.worldModel.product.entities.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
}
