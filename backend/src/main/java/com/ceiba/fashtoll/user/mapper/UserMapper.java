package com.ceiba.fashtoll.user.mapper;

import com.ceiba.fashtoll.user.dto.UserDTO;
import com.ceiba.fashtoll.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setIsActive(user.getIsActive());
        return dto;
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setCreatedAt(dto.getCreatedAt());
        user.setIsActive(dto.getIsActive());
        return user;
    }
}
