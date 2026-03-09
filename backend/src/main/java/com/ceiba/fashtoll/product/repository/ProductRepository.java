package com.ceiba.fashtoll.product.repository;

import com.ceiba.fashtoll.brand.entity.Brand;
import com.ceiba.fashtoll.product.entity.Product;
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
