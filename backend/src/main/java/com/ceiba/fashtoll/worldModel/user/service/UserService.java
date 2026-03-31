package com.ceiba.fashtoll.worldModel.user.service;

import com.ceiba.fashtoll.worldModel.user.dto.PasswordChangeRequestDTO;
import com.ceiba.fashtoll.worldModel.user.dto.UserCreateRequest;
import com.ceiba.fashtoll.worldModel.user.dto.UserResponse;
import com.ceiba.fashtoll.worldModel.user.dto.UserUpdateRequest;
import com.ceiba.fashtoll.worldModel.user.entity.User;
import com.ceiba.fashtoll.worldModel.user.mapper.UserMapper;
import com.ceiba.fashtoll.worldModel.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        userMapper.updateEntity(request, user);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        userRepository.delete(user);
    }

    @Transactional
    public void changePassword(Long userId, PasswordChangeRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void setUserActiveStatus(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setIsActive(active);
        userRepository.save(user);
    }
}
