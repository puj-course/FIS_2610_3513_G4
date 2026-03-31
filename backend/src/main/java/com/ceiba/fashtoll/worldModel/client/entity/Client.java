package com.ceiba.fashtoll.worldModel.client.entity;

import com.ceiba.fashtoll.worldModel.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    private Long id; // Se asignará el mismo id que el de User

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String name;

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
