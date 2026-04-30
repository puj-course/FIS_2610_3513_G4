package com.ceiba.fashtoll.searchEngine.indexingComponent;

import com.ceiba.fashtoll.worldModel.product.entities.Product;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "search_token")
public class SearchToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token; // Aquí se guarda la raíz, ej: "zapat"

    @ManyToMany(mappedBy = "tokens")
    private Set<Product> products = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String word) {
        this.token = word;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
