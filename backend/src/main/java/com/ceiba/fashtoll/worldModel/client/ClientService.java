package com.ceiba.fashtoll.worldModel.client;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.security.auth.AuthService;
import com.ceiba.fashtoll.security.auth.dtos.RegisterRequest;
import com.ceiba.fashtoll.utilities.enums.Role;
import com.ceiba.fashtoll.worldModel.client.dtos.*;
import com.ceiba.fashtoll.worldModel.user.User;
import com.ceiba.fashtoll.worldModel.user.UserService;
import com.ceiba.fashtoll.worldModel.user.dtos.PasswordChangeRequestDTO;
import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.brand.BrandRepository;
import com.ceiba.fashtoll.worldModel.brand.BrandMapper;
import com.ceiba.fashtoll.worldModel.brand.dtos.BrandPublicResponse;
import com.ceiba.fashtoll.worldModel.wishlist.Wishlist;
import com.ceiba.fashtoll.worldModel.wishlist.WishlistRepository;
import com.ceiba.fashtoll.worldModel.wishlist.WishlistMapper;
import com.ceiba.fashtoll.worldModel.wishlist.dtos.WishlistRequest;
import com.ceiba.fashtoll.worldModel.wishlist.dtos.WishlistResponse;
import com.ceiba.fashtoll.worldModel.wishlist.dtos.WishlistDetailsResponse;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final AuthService authService;
    private final UserService userService;
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;
    private final ProductRepository productRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper, AuthService authService,
            UserService userService, BrandRepository brandRepository, BrandMapper brandMapper,
            WishlistRepository wishlistRepository, WishlistMapper wishlistMapper, ProductRepository productRepository) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.authService = authService;
        this.userService = userService;
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
        this.wishlistRepository = wishlistRepository;
        this.wishlistMapper = wishlistMapper;
        this.productRepository = productRepository;
    }

    public List<ClientResponse> getAllClients() {
        this.logger.info("Se devolvieron todos los clientes");

        return clientRepository.findAll().stream()
                .map(clientMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ClientResponse getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", id));

        this.logger.info("Se devolvio el cliente '" + client.getName() + "' con id: " + id);

        return clientMapper.toResponse(client);
    }

    @Transactional
    public ClientResponse createClient(ClientCreateRequest request) {
        Client client = clientMapper.toEntity(request);
        Client savedClient = clientRepository.save(client);

        this.logger.info("Se creo el cliente '" + savedClient.getName() + "' con id: " + savedClient.getId());

        return clientMapper.toResponse(savedClient);
    }

    @Transactional
    public ClientResponse updateClient(Long id, ClientUpdateRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", id));

        clientMapper.updateEntityFromAdmin(request, client);
        Client savedClient = clientRepository.save(client);

        this.logger.info("Se actualizo el cliente '" + savedClient.getName() + "' con id: " + id);

        return clientMapper.toResponse(savedClient);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", id));
        clientRepository.delete(client);

        this.logger.info("Se elimino el cliente '" + client.getName() + "' con id: " + id);
    }

    public ClientProfileResponse getProfile(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", id));

        this.logger.info("Se devolvio el perfil del cliente '" + client.getName() + "' con id: " + id);

        return clientMapper.toProfileResponse(client);
    }

    @Transactional
    public ClientProfileResponse updateProfile(Long id, ClientProfileUpdateRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", id));

        clientMapper.updateEntityFromProfile(request, client);
        Client savedClient = clientRepository.save(client);

        this.logger.info("Se actualizo el perfil del cliente '" + savedClient.getName() + "' con id: " + id);

        return clientMapper.toProfileResponse(savedClient);
    }

    public ResponseEntity<Void> changePassword(Authentication authentication, PasswordChangeRequestDTO request) {
        User user = (User) authentication.getPrincipal();
        userService.changePassword(user.getId(), request);
        return ResponseEntity.noContent().build();
    }

    public void injectClientsFromJSON(List<ClientDTO> clientDTOs) {
        for (ClientDTO clientDTO : clientDTOs) {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(clientDTO.email());
            registerRequest.setPassword(clientDTO.password());
            registerRequest.setRole(Role.categorize(clientDTO.role()));
            registerRequest.setName(clientDTO.name());

            this.authService.clientRegister(registerRequest);
        }
    }

    @Transactional
    public void followBrand(Long clientId, Long brandId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", clientId));
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("marca", "id", brandId));

        if (!client.getFollowedBrands().contains(brand)) {
            client.getFollowedBrands().add(brand);
            brand.setFollowers(brand.getFollowers() + 1);
            clientRepository.save(client);
            brandRepository.save(brand);
            this.logger
                    .info("El cliente '" + client.getName() + "' comenzo a seguir la marca '" + brand.getName() + "'");
        }
    }

    @Transactional
    public void unfollowBrand(Long clientId, Long brandId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", clientId));
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("marca", "id", brandId));

        if (client.getFollowedBrands().contains(brand)) {
            client.getFollowedBrands().remove(brand);
            brand.setFollowers(Math.max(0, brand.getFollowers() - 1));
            clientRepository.save(client);
            brandRepository.save(brand);
            this.logger.info("El cliente '" + client.getName() + "' dejo de seguir la marca '" + brand.getName() + "'");
        }
    }

    public List<BrandPublicResponse> getFollowedBrands(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", clientId));

        this.logger.info("Se devolvieron las marcas seguidas por el cliente '" + client.getName() + "'");

        return client.getFollowedBrands().stream()
                .map(brandMapper::toPublicResponse)
                .collect(Collectors.toList());
    }

    public List<WishlistResponse> getWishlists(Long clientId) {
        List<Wishlist> wishlists = wishlistRepository.findByClientId(clientId);
        return wishlists.stream()
                .map(wishlistMapper::toResponse)
                .collect(Collectors.toList());
    }

    public WishlistDetailsResponse getWishlist(Long clientId, Long wishlistId) {
        Wishlist wishlist = wishlistRepository.findByIdAndClientId(wishlistId, clientId)
                .orElseThrow(() -> new ResourceNotFoundException("lista de deseos", "id", wishlistId));
        return wishlistMapper.toDetailsResponse(wishlist);
    }

    @Transactional
    public WishlistResponse createWishlist(Long clientId, WishlistRequest request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", clientId));

        Wishlist wishlist = new Wishlist();
        wishlist.setClient(client);
        wishlist.setName(request.getName());

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        this.logger.info("El cliente con id " + clientId + " creo una lista de deseos llamada '"
                + savedWishlist.getName() + "'");

        return wishlistMapper.toResponse(savedWishlist);
    }

    @Transactional
    public WishlistResponse updateWishlist(Long clientId, Long wishlistId, WishlistRequest request) {
        Wishlist wishlist = wishlistRepository.findByIdAndClientId(wishlistId, clientId)
                .orElseThrow(() -> new ResourceNotFoundException("lista de deseos", "id", wishlistId));

        wishlist.setName(request.getName());
        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        this.logger.info("El cliente con id " + clientId + " actualizo la lista de deseos con id " + wishlistId);

        return wishlistMapper.toResponse(savedWishlist);
    }

    @Transactional
    public void deleteWishlist(Long clientId, Long wishlistId) {
        Wishlist wishlist = wishlistRepository.findByIdAndClientId(wishlistId, clientId)
                .orElseThrow(() -> new ResourceNotFoundException("lista de deseos", "id", wishlistId));

        Wishlist defaultWishlist = wishlistRepository.findFirstByClientIdOrderByIdAsc(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("lista de deseos por defecto", "cliente", clientId));

        if (wishlist.getId().equals(defaultWishlist.getId())) {
            throw new UnauthorizedException("eliminar", "la lista de deseos por defecto");
        }

        wishlistRepository.delete(wishlist);
        this.logger.info("El cliente con id " + clientId + " elimino la lista de deseos con id " + wishlistId);
    }

    @Transactional
    public void addToDefaultWishlist(Long clientId, Long productId) {
        Wishlist defaultWishlist = wishlistRepository.findFirstByClientIdOrderByIdAsc(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("lista de deseos por defecto", "cliente", clientId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("producto", "id", productId));

        if (!defaultWishlist.getProducts().contains(product)) {
            defaultWishlist.getProducts().add(product);
            wishlistRepository.save(defaultWishlist);
            this.logger.info("El cliente con id " + clientId + " guardo el producto con id " + productId
                    + " en su lista por defecto");
        }
    }

    @Transactional
    public void addToWishlist(Long clientId, Long wishlistId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByIdAndClientId(wishlistId, clientId)
                .orElseThrow(() -> new ResourceNotFoundException("lista de deseos", "id", wishlistId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("producto", "id", productId));

        if (!wishlist.getProducts().contains(product)) {
            wishlist.getProducts().add(product);
            wishlistRepository.save(wishlist);
            this.logger.info("El cliente con id " + clientId + " guardo el producto con id " + productId
                    + " en la lista de deseos con id " + wishlistId);
        }
    }

    @Transactional
    public void removeFromWishlist(Long clientId, Long wishlistId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByIdAndClientId(wishlistId, clientId)
                .orElseThrow(() -> new ResourceNotFoundException("lista de deseos", "id", wishlistId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("producto", "id", productId));

        if (wishlist.getProducts().contains(product)) {
            wishlist.getProducts().remove(product);
            wishlistRepository.save(wishlist);
            this.logger.info("El cliente con id " + clientId + " elimino el producto con id " + productId
                    + " de la lista de deseos con id " + wishlistId);
        }
    }
}
