package com.ceiba.fashtoll.searchEngine.tag.dto;

import com.ceiba.fashtoll.utilities.enums.TagType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagRequest {
    @NotBlank(message = "El nombre de la etiqueta es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String name;

    @NotNull(message = "El tipo de etiqueta es obligatorio")
    private TagType type;
}
