package com.ceiba.fashtoll.worldModel.user;

import com.ceiba.fashtoll.worldModel.user.dtos.PasswordChangeRequestDTO;
import com.ceiba.fashtoll.worldModel.user.dtos.UserCreateRequest;
import com.ceiba.fashtoll.worldModel.user.dtos.UserResponse;
import com.ceiba.fashtoll.worldModel.user.dtos.UserUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("CP-USR-01: getAllUsers - Retorna lista")
    void getAllUsers_returnsList() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.toResponse(any())).thenReturn(new UserResponse());

        List<UserResponse> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("CP-USR-02: getUserById - Retorna usuario")
    void getUserById_returnsUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(any())).thenReturn(new UserResponse());

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("CP-USR-03: createUser - Crea usuario")
    void createUser_createsUser() {
        UserCreateRequest req = new UserCreateRequest();
        req.setPassword("pass");
        User user = new User();
        user.setId(1L);
        user.setPassword("pass");
        when(userMapper.toEntity(any())).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toResponse(any())).thenReturn(new UserResponse());

        UserResponse result = userService.createUser(req);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("CP-USR-04: updateUser - Actualiza usuario")
    void updateUser_updatesUser() {
        UserUpdateRequest req = new UserUpdateRequest();
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toResponse(any())).thenReturn(new UserResponse());

        UserResponse result = userService.updateUser(1L, req);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("CP-USR-05: deleteUser - Elimina usuario")
    void deleteUser_deletesUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("CP-USR-06: changePassword - Cambia contraseña")
    void changePassword_changesPassword() {
        PasswordChangeRequestDTO req = new PasswordChangeRequestDTO();
        req.setCurrentPassword("oldPass");
        req.setNewPassword("newPass");

        User user = new User();
        user.setId(1L);
        user.setPassword("encodedOldPass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        userService.changePassword(1L, req);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("CP-USR-07: setUserActiveStatus - Actualiza estado")
    void setUserActiveStatus_updatesStatus() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.setUserActiveStatus(1L, false);

        verify(userRepository, times(1)).save(user);
    }
}
