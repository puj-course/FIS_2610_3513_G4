package com.ceiba.fashtoll.worldModel.tag;

import com.ceiba.fashtoll.utilities.enums.TagType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tags")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la etiqueta es obligatorio")
    @Size(max = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TagType type;
}
