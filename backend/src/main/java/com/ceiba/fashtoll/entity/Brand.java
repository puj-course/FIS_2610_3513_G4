package com.ceiba.fashtoll.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    @Id
    private Long id; // Se asignará el mismo id que el de User

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "El nombre de la marca es obligatorio")
    @Size(max = 100)
    private String name;

    @Column(name = "picture_url", length = 500)
    private String pictureUrl;

    @Column(name = "link_official", length = 255)
    private String linkOfficial;

    private Integer followers = 0;

    @Min(0) @Max(5)
    private Double rating = 0.0;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    @JsonIgnore // Para que jackon no serialice los productos de la marca
    private List<Product> products = new ArrayList<>();

    /* Cuando exista Tag
    @ManyToMany
    @JoinTable(
            name = "brand_tags",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
    */
}
