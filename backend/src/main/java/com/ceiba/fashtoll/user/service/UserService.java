package com.ceiba.fashtoll.user.service;

import com.ceiba.fashtoll.user.dto.UserDTO;
import com.ceiba.fashtoll.user.entity.User;
import com.ceiba.fashtoll.user.mapper.UserMapper;
import com.ceiba.fashtoll.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        return userMapper.toDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        if (user.getIsActive() == null) user.setIsActive(true);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public UserDTO updateUser(Long id, UserDTO updatedUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        user.setEmail(updatedUserDTO.getEmail());
        user.setRole(updatedUserDTO.getRole());
        user.setCreatedAt(updatedUserDTO.getCreatedAt());
        user.setIsActive(updatedUserDTO.getIsActive());
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        userRepository.delete(user);
    }
}
