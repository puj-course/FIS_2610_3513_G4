package com.ceiba.fashtoll.security.auth;

import com.ceiba.fashtoll.security.auth.dtos.AuthResponse;
import com.ceiba.fashtoll.security.auth.dtos.LoginRequest;
import com.ceiba.fashtoll.security.auth.dtos.RegisterRequest;
import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.brand.BrandRepository;
import com.ceiba.fashtoll.worldModel.client.Client;
import com.ceiba.fashtoll.worldModel.client.ClientRepository;
import com.ceiba.fashtoll.utilities.enums.Role;
import com.ceiba.fashtoll.security.jwt.JwtProvider;
import com.ceiba.fashtoll.worldModel.user.User;
import com.ceiba.fashtoll.worldModel.user.UserRepository;
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

        String token;

        // ESTO ES PROVICIONAL, HAY QUE CAMBIARLO
        // Ahora en la base hay 2 admins, user: gonso, passwd: gonso123; y user:admin , passwd: admin123
        if (request.getRole() == Role.ADMIN) {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setName(request.getName());
            user.setRole(request.getRole());

            userRepository.save(user);

            token = jwtProvider.generateToken(user);

            return AuthResponse.builder()
                    .token(token)
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();
        }

        if (request.getRole() == Role.CLIENT) {
            Client client = new Client();
            client.setEmail(request.getEmail());
            client.setPassword(passwordEncoder.encode(request.getPassword()));
            client.setName(request.getName());
            client.setRole(request.getRole());
            client.setIsActive(true);

            clientRepository.save(client);

            token = jwtProvider.generateToken(client);

            return AuthResponse.builder()
                    .token(token)
                    .email(client.getEmail())
                    .role(client.getRole().name())
                    .build();
        } else if (request.getRole() == Role.BRAND) {
            Brand brand = new Brand();
            brand.setEmail(request.getEmail());
            brand.setPassword(passwordEncoder.encode(request.getPassword()));
            brand.setName(request.getName());
            brand.setRole(request.getRole());
            brand.setIsActive(true);
            brand.setPictureUrl(request.getPictureUrl());
            brand.setLinkOfficial(request.getLinkOfficial());

            brandRepository.save(brand);

            token = jwtProvider.generateToken(brand);

            return AuthResponse.builder()
                    .token(token)
                    .email(brand.getEmail())
                    .role(brand.getRole().name())
                    .build();
        }

        return null;
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
