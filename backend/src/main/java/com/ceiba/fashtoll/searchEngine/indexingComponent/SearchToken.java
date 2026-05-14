package com.ceiba.fashtoll.searchEngine.indexingComponent;

import com.ceiba.fashtoll.worldModel.product.entities.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "search_token")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SearchToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true, nullable = false)
    private String token; // Aquí se guarda la raíz, ej: "zapat"

    @ManyToMany(mappedBy = "tokens")
    private Set<Product> products = new HashSet<>();
}
