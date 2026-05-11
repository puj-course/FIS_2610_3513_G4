package com.ceiba.fashtoll.worldModel.wishlist.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WishlistRequest {
    @NotBlank(message = "El nombre de la lista es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;
}
