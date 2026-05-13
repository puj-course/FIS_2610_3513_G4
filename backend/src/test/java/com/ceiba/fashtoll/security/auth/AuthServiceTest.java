package com.ceiba.fashtoll.security.auth;

import com.ceiba.fashtoll.security.auth.dtos.AuthResponse;
import com.ceiba.fashtoll.security.auth.dtos.LoginRequest;
import com.ceiba.fashtoll.security.auth.dtos.RegisterRequest;
import com.ceiba.fashtoll.security.jwt.JwtProvider;
import com.ceiba.fashtoll.utilities.enums.Role;
import com.ceiba.fashtoll.worldModel.brand.BrandRepository;
import com.ceiba.fashtoll.worldModel.client.ClientRepository;
import com.ceiba.fashtoll.worldModel.user.User;
import com.ceiba.fashtoll.worldModel.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de AuthService")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("CP-AUTH-01: register - Registra un cliente exitosamente")
    void register_client_returnsAuthResponse() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("test@test.com");
        req.setPassword("pass");
        req.setName("Test");
        req.setRole(Role.CLIENT);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(jwtProvider.generateToken(any())).thenReturn("token");

        AuthResponse resp = authService.register(req);

        assertNotNull(resp);
        verify(clientRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("CP-AUTH-02: register - Registra una marca exitosamente")
    void register_brand_returnsAuthResponse() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("brand@test.com");
        req.setPassword("pass");
        req.setName("Brand");
        req.setRole(Role.BRAND);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(jwtProvider.generateToken(any())).thenReturn("token");

        AuthResponse resp = authService.register(req);

        assertNotNull(resp);
        verify(brandRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("CP-AUTH-03: login - Inicia sesión exitosamente")
    void login_returnsAuthResponse() {
        LoginRequest req = new LoginRequest();
        req.setEmail("test@test.com");
        req.setPassword("pass");

        User user = new User();
        user.setEmail("test@test.com");
        user.setRole(Role.CLIENT);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtProvider.generateToken(any())).thenReturn("token");

        AuthResponse resp = authService.login(req);

        assertNotNull(resp);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("CP-AUTH-04: clientRegister - Registra cliente interno")
    void clientRegister_savesClient() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("test@test.com");
        req.setPassword("pass");
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");

        authService.clientRegister(req);

        verify(clientRepository, times(1)).save(any());
    }
}
