package com.ceiba.fashtoll.worldModel.product.repositories;

import com.ceiba.fashtoll.searchEngine.dtos.QueryFilters;
import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findByBrand(Brand brand);

    List<Product> findByAvailableTrue();

    List<Product> findByAvailableFalse();

    List<Product> findByBrandId(Long brandId);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.tokens t WHERE t.token IN :words")
    List<Product> findBySearchTokens(@Param("words") List<String> words);

    // Spring Expression Language syntaxis :#{#nombre_parametro.nombre_atributo}
    // : indica que es un parametro, # indica que es una expresion de SpEl, {} delimita la expresion
    // no era necesario esto, solo era necesario heredar JpaSpecificationExecutor
    //@Query("SELECT p FROM Product p WHERE p.productType.name == :#{#filters.productType()}")
    //List<Product> findByFilter(@Param("words") List<String> words, @Param("filters") QueryFilters filters);
}
