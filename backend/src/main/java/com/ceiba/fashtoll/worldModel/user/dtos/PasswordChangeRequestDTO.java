package com.ceiba.fashtoll.worldModel.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequestDTO {
    private String currentPassword;
    private String newPassword;
}
