package com.ceiba.fashtoll.dto;

import com.ceiba.fashtoll.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private Role role;
    private LocalDateTime createdAt;
    private Boolean isActive;
}
