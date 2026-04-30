package com.ceiba.fashtoll.worldModel.user;

import com.ceiba.fashtoll.worldModel.user.dtos.PasswordChangeRequestDTO;
import com.ceiba.fashtoll.worldModel.user.dtos.UserCreateRequest;
import com.ceiba.fashtoll.worldModel.user.dtos.UserResponse;
import com.ceiba.fashtoll.worldModel.user.dtos.UserUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
        this.logger.info("Se devolvieron todos los usuarios");

        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));

        this.logger.info("Se devolvio el usuario '" + user.getUsername() + "' con id: " + user.getId());

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        User savedUser = userRepository.save(user);

        this.logger.info("Se creo el usuario '" + user.getUsername() + "' con id: " + savedUser.getId());

        return userMapper.toResponse(savedUser);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        userMapper.updateEntity(request, user);
        User savedUser = userRepository.save(user);

        this.logger.info("Se actualizo el usuario '" + user.getUsername() + "' con id: " + savedUser.getId());

        return userMapper.toResponse(savedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        userRepository.delete(user);

        this.logger.info("Se elimino el usuario '" + user.getUsername() + "' con id: " + user.getId());
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

        this.logger.info("Se actualizo la contraseña del usuario '" + user.getUsername() + "' con id: " + user.getId());
    }

    public void setUserActiveStatus(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setIsActive(active);
        userRepository.save(user);

        this.logger.info("Se actualizo el estado del usuario '" + user.getUsername() + "' con id: " + user.getId());
    }
}
