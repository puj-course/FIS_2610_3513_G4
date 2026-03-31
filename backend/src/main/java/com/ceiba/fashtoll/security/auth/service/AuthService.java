package com.ceiba.fashtoll.security.auth.service;

import com.ceiba.fashtoll.security.auth.dto.AuthResponse;
import com.ceiba.fashtoll.security.auth.dto.LoginRequest;
import com.ceiba.fashtoll.security.auth.dto.RegisterRequest;
import com.ceiba.fashtoll.worldModel.brand.entity.Brand;
import com.ceiba.fashtoll.worldModel.brand.repository.BrandRepository;
import com.ceiba.fashtoll.worldModel.client.entity.Client;
import com.ceiba.fashtoll.worldModel.client.repository.ClientRepository;
import com.ceiba.fashtoll.utilities.enums.Role;
import com.ceiba.fashtoll.security.jwt.JwtProvider;
import com.ceiba.fashtoll.worldModel.user.entity.User;
import com.ceiba.fashtoll.worldModel.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final BrandRepository brandRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // como puedo registrar un admin?
        if (request.getRole() == Role.ADMIN) {
            throw new RuntimeException("No se permite el registro manual de administradores");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        if (request.getRole() == Role.CLIENT) {
            Client client = new Client();
            client.setUser(savedUser);
            client.setName(request.getName());
            clientRepository.save(client);
        } else if (request.getRole() == Role.BRAND) {
            Brand brand = new Brand();
            brand.setUser(savedUser);
            brand.setName(request.getName());
            brand.setPictureUrl(request.getPictureUrl());
            brand.setLinkOfficial(request.getLinkOfficial());
            brandRepository.save(brand);
        }

        String token = jwtProvider.generateToken(savedUser);
        return AuthResponse.builder()
                .token(token)
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtProvider.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
