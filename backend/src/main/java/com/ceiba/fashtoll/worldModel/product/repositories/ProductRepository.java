package com.ceiba.fashtoll.worldModel.product.repositories;

import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByBrand(Brand brand);

    List<Product> findByAvailableTrue();

    List<Product> findByAvailableFalse();

    List<Product> findByBrandId(Long brandId);
}
