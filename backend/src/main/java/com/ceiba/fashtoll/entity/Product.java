package com.ceiba.fashtoll.entity;

import com.ceiba.fashtoll.enums.Color;
import com.ceiba.fashtoll.enums.Gender;
import com.ceiba.fashtoll.enums.GeneralFit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    @JsonIgnore // para que jackson no serialice el objeto marca
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productType;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "general_fit")
    private GeneralFit generalFit;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Color color;

    private Boolean available = true;

    @Min(0) @Max(5)
    private Double rating = 0.0;

    @Column(name = "link_product", length = 500)
    private String linkProduct;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    /* Cuando exista ProductImage y Tag
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "product_tags",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
    */
}
