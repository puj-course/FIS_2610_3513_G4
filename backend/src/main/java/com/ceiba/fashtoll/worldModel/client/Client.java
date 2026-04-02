package com.ceiba.fashtoll.worldModel.client;

import com.ceiba.fashtoll.worldModel.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client extends User{
    //esto es lo que lo diferencia de User
    private List<String> wishlists = new ArrayList<>();

    /* Cuando exista Wishlist
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Wishlist> wishlists = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "client_follows_brand",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "brand_id")
    )
    private Set<Brand> followedBrands = new HashSet<>();
    */
}
