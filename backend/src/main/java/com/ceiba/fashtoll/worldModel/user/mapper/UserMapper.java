package com.ceiba.fashtoll.worldModel.user.mapper;

import com.ceiba.fashtoll.worldModel.user.dto.UserCreateRequest;
import com.ceiba.fashtoll.worldModel.user.dto.UserResponse;
import com.ceiba.fashtoll.worldModel.user.dto.UserUpdateRequest;
import com.ceiba.fashtoll.worldModel.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) return null;
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        response.setIsActive(user.getIsActive());
        return response;
    }

    public User toEntity(UserCreateRequest request) {
        if (request == null) return null;
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        return user;
    }

    public void updateEntity(UserUpdateRequest request, User user) {
        if (request == null || user == null) return;
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setIsActive(request.getIsActive());
    }
}
