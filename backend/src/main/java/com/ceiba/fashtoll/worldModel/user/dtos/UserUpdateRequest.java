package com.ceiba.fashtoll.worldModel.user.dtos;

import com.ceiba.fashtoll.utilities.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @NotNull(message = "El rol es obligatorio")
    private Role role;

    @NotNull(message = "El estado de actividad es obligatorio")
    private Boolean isActive;
}
