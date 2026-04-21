package com.ceiba.fashtoll.security.auth;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.DuplicatedResourceException;
import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.UnauthorizedException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
            throw new UnauthorizedException("registro","admin's");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicatedResourceException("email","la base de datos de usuarios");
        }

        String token;

        if (request.getRole() == Role.CLIENT) {
            Client client = new Client();
            client.setEmail(request.getEmail());
            client.setPassword(passwordEncoder.encode(request.getPassword()));
            client.setName(request.getName());
            client.setRole(request.getRole());
            client.setIsActive(true);

            clientRepository.save(client);

            token = jwtProvider.generateToken(client);

            this.logger.info("El cliente '" + client.getName() + "' con email '" + client.getEmail() + "' se registro correctamente");

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
            brand.setPictureURL(request.getPictureURL());
            brand.setLinkOfficial(request.getLinkOfficial());

            brandRepository.save(brand);

            token = jwtProvider.generateToken(brand);

            this.logger.info("La marca '" + brand.getName() + "' con email '" + brand.getEmail() + "' se registro correctamente");

            return AuthResponse.builder()
                    .token(token)
                    .email(brand.getEmail())
                    .role(brand.getRole().name())
                    .build();
        }

        return null;
    }

    public void brandRegister (RegisterRequest request){
        Brand brand = new Brand();
        brand.setEmail(request.getEmail());
        brand.setPassword(passwordEncoder.encode(request.getPassword()));
        brand.setName(request.getName());
        brand.setRole(request.getRole());
        brand.setIsActive(true);
        brand.setPictureURL(request.getPictureURL());
        brand.setLinkOfficial(request.getLinkOfficial());

        brandRepository.save(brand);
    }

    public void clientRegister (RegisterRequest request){
        Client client = new Client();
        client.setEmail(request.getEmail());
        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setName(request.getName());
        client.setRole(request.getRole());

        clientRepository.save(client);
    }

    public void adminRegister (RegisterRequest request){
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole(request.getRole());

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("email", "la base de datos de usuarios"));

        String token = jwtProvider.generateToken(user);

        this.logger.info("El usuario '" + user.getName() + "' con email '" + user.getEmail() + "' inicio sesion.");

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
