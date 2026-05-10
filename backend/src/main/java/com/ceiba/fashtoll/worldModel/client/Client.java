package com.ceiba.fashtoll.worldModel.client;

import com.ceiba.fashtoll.worldModel.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.review.entity.BrandReview;
import com.ceiba.fashtoll.worldModel.review.entity.ProductReview;
import com.ceiba.fashtoll.worldModel.wishlist.Wishlist;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client extends User{
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wishlist> wishlists = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "client_follows_brand",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "brand_id")
    )
    private Set<Brand> followedBrands = new HashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BrandReview> brandReviews = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductReview> productReviews = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.wishlists.isEmpty()) {
            Wishlist defaultWishlist = new Wishlist();
            defaultWishlist.setName("Mis Favoritos");
            defaultWishlist.setClient(this);
            this.wishlists.add(defaultWishlist);
        }
    }
}

