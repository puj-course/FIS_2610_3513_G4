package com.ceiba.fashtoll.worldModel.user.dtos;

import com.ceiba.fashtoll.utilities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
    private Boolean isActive;
}
