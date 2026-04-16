package com.ceiba.fashtoll.worldModel.product.repositories;

import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByBrand(Brand brand);

    List<Product> findByAvailableTrue();

    List<Product> findByAvailableFalse();

    List<Product> findByBrandId(Long brandId);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.tokens t WHERE t.token IN :words")
    List<Product> findBySearchTokens(@Param("words") List<String> words);
}
