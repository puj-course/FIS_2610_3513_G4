package com.ceiba.fashtoll.worldModel.brand.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandCreateRequest {
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "El nombre de la marca es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String name;

    @Size(max = 500, message = "La URL de la imagen no puede exceder los 500 caracteres")
    private String pictureUrl;

    @Size(max = 255, message = "El link oficial no puede exceder los 255 caracteres")
    private String linkOfficial;
}
