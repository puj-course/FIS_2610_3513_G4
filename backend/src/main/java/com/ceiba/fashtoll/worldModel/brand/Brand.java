package com.ceiba.fashtoll.worldModel.brand;

import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.review.entity.BrandReview;
import com.ceiba.fashtoll.worldModel.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand extends User {
    private Integer priority = 0;

    @Column(name = "picture_url", length = 500)
    private String pictureURL;

    @Column(name = "link_official", length = 255)
    private String linkOfficial;

    private Integer followers = 0;

    @Min(0) @Max(5)
    private Double rating = 0.0;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    @JsonIgnore // Para que jackon no serialice los productos de la marca
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BrandReview> reviews = new ArrayList<>();

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
