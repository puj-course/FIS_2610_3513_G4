package com.ceiba.fashtoll.worldModel.client;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.DuplicatedResourceException;
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
import com.ceiba.fashtoll.worldModel.review.dto.ReviewRequest;
import com.ceiba.fashtoll.worldModel.review.dto.ReviewResponse;
import com.ceiba.fashtoll.worldModel.review.entity.BrandReview;
import com.ceiba.fashtoll.worldModel.review.entity.ProductReview;
import com.ceiba.fashtoll.worldModel.review.mapper.ReviewMapper;
import com.ceiba.fashtoll.worldModel.review.repository.BrandReviewRepository;
import com.ceiba.fashtoll.worldModel.review.repository.ProductReviewRepository;
import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.UnauthorizedException;
import com.ceiba.fashtoll.utilities.sms.SmsService;
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
        private final BrandReviewRepository brandReviewRepository;
        private final ProductReviewRepository productReviewRepository;
        private final ReviewMapper reviewMapper;
        private final SmsService smsService;

        @Autowired
        public ClientService(ClientRepository clientRepository, ClientMapper clientMapper, AuthService authService,
                        UserService userService, BrandRepository brandRepository, BrandMapper brandMapper,
                        WishlistRepository wishlistRepository, WishlistMapper wishlistMapper,
                        ProductRepository productRepository,
                        BrandReviewRepository brandReviewRepository, ProductReviewRepository productReviewRepository,
                        ReviewMapper reviewMapper, SmsService smsService) {
                this.clientRepository = clientRepository;
                this.clientMapper = clientMapper;
                this.authService = authService;
                this.userService = userService;
                this.brandRepository = brandRepository;
                this.brandMapper = brandMapper;
                this.wishlistRepository = wishlistRepository;
                this.wishlistMapper = wishlistMapper;
                this.productRepository = productRepository;
                this.brandReviewRepository = brandReviewRepository;
                this.productReviewRepository = productReviewRepository;
                this.reviewMapper = reviewMapper;
                this.smsService = smsService;
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

        // ==================== PERFIL DE CLIENTE ====================

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

        // ==================== SEGUIMIENTO DE MARCAS ====================

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
                                        .info("El cliente '" + client.getName() + "' comenzo a seguir la marca '"
                                                        + brand.getName() + "'");

                        // Notificación SMS a la marca (solo si tiene número registrado)
                        if (brand.getPhoneNumber() != null && !brand.getPhoneNumber().isBlank()) {
                                String message = String.format(
                                                "*¡Notificación de Fashtoll!* 👗✨\n\n" +
                                                "👤 *%s* ahora sigue tu marca.\n" +
                                                "📈 Ya tienes *%d* seguidores.",
                                                client.getName(),
                                                brand.getFollowers());
                                smsService.sendSms(brand.getPhoneNumber(), message);
                        }
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
                        this.logger.info("El cliente '" + client.getName() + "' dejo de seguir la marca '"
                                        + brand.getName() + "'");
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

        // ==================== LISTAS DE DESEOS ====================

        public List<WishlistResponse> getWishlists(Long clientId) {
                List<Wishlist> wishlists = wishlistRepository.findByClientId(clientId);
                return wishlists.stream()
                                .map(wishlistMapper::toResponse)
                                .collect(Collectors.toList());
        }

        public WishlistDetailsResponse getDefaultWishlist(Long clientId) {
                Wishlist defualtWishlist = wishlistRepository.findFirstByClientIdOrderByIdAsc(clientId)
                        .orElseThrow(() -> new ResourceNotFoundException("lista de deseos por defecto", "cliente", clientId));
                return wishlistMapper.toDetailsResponse(defualtWishlist);
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
                this.logger.info(
                                "El cliente con id " + clientId + " actualizo la lista de deseos con id " + wishlistId);

                return wishlistMapper.toResponse(savedWishlist);
        }

        @Transactional
        public void deleteWishlist(Long clientId, Long wishlistId) {
                Wishlist wishlist = wishlistRepository.findByIdAndClientId(wishlistId, clientId)
                                .orElseThrow(() -> new ResourceNotFoundException("lista de deseos", "id", wishlistId));

                Wishlist defaultWishlist = wishlistRepository.findFirstByClientIdOrderByIdAsc(clientId)
                                .orElseThrow(() -> new ResourceNotFoundException("lista de deseos por defecto",
                                                "cliente", clientId));

                if (wishlist.getId().equals(defaultWishlist.getId())) {
                        throw new UnauthorizedException("eliminar", "la lista de deseos por defecto");
                }

                wishlistRepository.delete(wishlist);
                this.logger.info("El cliente con id " + clientId + " elimino la lista de deseos con id " + wishlistId);
        }

        @Transactional
        public void addToDefaultWishlist(Long clientId, Long productId) {
                Wishlist defaultWishlist = wishlistRepository.findFirstByClientIdOrderByIdAsc(clientId)
                                .orElseThrow(() -> new ResourceNotFoundException("lista de deseos por defecto",
                                                "cliente", clientId));

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
        public void removeFromDefaultWishlist(Long clientId, Long productId) {
                Wishlist defaultWishlist = wishlistRepository.findFirstByClientIdOrderByIdAsc(clientId)
                        .orElseThrow(() -> new ResourceNotFoundException("lista de deseos por defecto",
                                "cliente", clientId));

                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("producto", "id", productId));

                if (defaultWishlist.getProducts().contains(product)) {
                        defaultWishlist.getProducts().remove(product);
                        wishlistRepository.save(defaultWishlist);
                        this.logger.info("El cliente con id " + clientId + " elimino el producto con id " + productId
                                + " de la lista de deseos default");
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

        // ==================== RESEÑAS DE MARCAS ====================

        public List<ReviewResponse> getBrandReviews(Long clientId) {
                clientRepository.findById(clientId)
                                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", clientId));

                List<BrandReview> reviews = brandReviewRepository.findByClientId(clientId);
                this.logger.info("Se devolvieron las reseñas de marcas del cliente con id " + clientId);

                return reviews.stream()
                                .map(reviewMapper::toResponse)
                                .collect(Collectors.toList());
        }

        public ReviewResponse getBrandReview(Long clientId, Long brandId) {
                BrandReview review = brandReviewRepository.findByClientIdAndBrandId(clientId, brandId)
                                .orElseThrow(() -> new ResourceNotFoundException("reseña de marca", "brandId",
                                                brandId));

                this.logger.info("Se devolvio la reseña del cliente con id " + clientId + " sobre la marca con id "
                                + brandId);

                return reviewMapper.toResponse(review);
        }

        @Transactional
        public ReviewResponse postBrandReview(Long clientId, Long brandId, ReviewRequest request) {
                Client client = clientRepository.findById(clientId)
                                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", clientId));
                Brand brand = brandRepository.findById(brandId)
                                .orElseThrow(() -> new ResourceNotFoundException("marca", "id", brandId));

                if (brandReviewRepository.existsByClientIdAndBrandId(clientId, brandId)) {
                        throw new DuplicatedResourceException("reseña de marca",
                                        "cliente " + clientId + " y marca " + brandId);
                }

                BrandReview review = new BrandReview();
                review.setClient(client);
                review.setBrand(brand);
                review.setComment(request.getComment());
                review.setRating(request.getRating());

                BrandReview savedReview = brandReviewRepository.save(review);
                recalculateBrandRating(brand);

                this.logger.info("El cliente con id " + clientId + " publico una reseña sobre la marca con id "
                                + brandId);

                // Notificación SMS a la marca (solo si tiene número registrado)
                if (brand.getPhoneNumber() != null && !brand.getPhoneNumber().isBlank()) {
                        String message = String.format(
                                "*¡Notificación de Fashtoll!* 👗✨\n\n" +
                                "⭐ *%s* ha publicado una reseña sobre tu marca:\n\n" +
                                "📝 _\"%s\"_\n" +
                                "🏅 Calificación: *%d/5*\n" +
                                "📊 Tu nuevo rating es *%.1f*",
                                client.getName(),
                                request.getComment() != null && !request.getComment().isBlank() ? request.getComment() : "Sin comentario",
                                request.getRating(),
                                brand.getRating()
                        );
                        smsService.sendSms(brand.getPhoneNumber(), message);
                }

                return reviewMapper.toResponse(savedReview);
        }

        @Transactional
        public ReviewResponse updateBrandReview(Long clientId, Long brandId, ReviewRequest request) {
                Brand brand = brandRepository.findById(brandId)
                                .orElseThrow(() -> new ResourceNotFoundException("marca", "id", brandId));

                BrandReview review = brandReviewRepository.findByClientIdAndBrandId(clientId, brandId)
                                .orElseThrow(() -> new ResourceNotFoundException("reseña de marca", "brandId",
                                                brandId));

                review.setComment(request.getComment());
                review.setRating(request.getRating());

                BrandReview savedReview = brandReviewRepository.save(review);
                recalculateBrandRating(brand);

                this.logger.info("El cliente con id " + clientId + " actualizo su reseña sobre la marca con id "
                                + brandId);

                return reviewMapper.toResponse(savedReview);
        }

        @Transactional
        public void deleteBrandReview(Long clientId, Long brandId) {
                Brand brand = brandRepository.findById(brandId)
                                .orElseThrow(() -> new ResourceNotFoundException("marca", "id", brandId));

                BrandReview review = brandReviewRepository.findByClientIdAndBrandId(clientId, brandId)
                                .orElseThrow(() -> new ResourceNotFoundException("reseña de marca", "brandId",
                                                brandId));

                brandReviewRepository.delete(review);
                recalculateBrandRating(brand);

                this.logger.info("El cliente con id " + clientId + " elimino su reseña sobre la marca con id "
                                + brandId);
        }

        // ==================== RESEÑAS DE PRODUCTOS ====================

        public List<ReviewResponse> getProductReviews(Long clientId) {
                clientRepository.findById(clientId)
                                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", clientId));

                List<ProductReview> reviews = productReviewRepository.findByClientId(clientId);
                this.logger.info("Se devolvieron las reseñas de productos del cliente con id " + clientId);

                return reviews.stream()
                                .map(reviewMapper::toResponse)
                                .collect(Collectors.toList());
        }

        public ReviewResponse getProductReview(Long clientId, Long productId) {
                ProductReview review = productReviewRepository.findByClientIdAndProductId(clientId, productId)
                                .orElseThrow(() -> new ResourceNotFoundException("reseña de producto", "productId",
                                                productId));

                this.logger.info("Se devolvio la reseña del cliente con id " + clientId + " sobre el producto con id "
                                + productId);

                return reviewMapper.toResponse(review);
        }

        @Transactional
        public ReviewResponse postProductReview(Long clientId, Long productId, ReviewRequest request) {
                Client client = clientRepository.findById(clientId)
                                .orElseThrow(() -> new ResourceNotFoundException("cliente", "id", clientId));
                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new ResourceNotFoundException("producto", "id", productId));

                if (productReviewRepository.existsByClientIdAndProductId(clientId, productId)) {
                        throw new DuplicatedResourceException("reseña de producto",
                                        "cliente " + clientId + " y producto " + productId);
                }

                ProductReview review = new ProductReview();
                review.setClient(client);
                review.setProduct(product);
                review.setComment(request.getComment());
                review.setRating(request.getRating());

                ProductReview savedReview = productReviewRepository.save(review);
                recalculateProductRating(product);

                this.logger.info("El cliente con id " + clientId + " publico una reseña sobre el producto con id "
                                + productId);

                // Notificación SMS a la marca dueña del producto (solo si tiene número registrado)
                Brand brandOwner = product.getBrand();
                if (brandOwner != null && brandOwner.getPhoneNumber() != null && !brandOwner.getPhoneNumber().isBlank()) {
                        String message = String.format(
                                "*¡Notificación de Fashtoll!* 👗✨\n\n" +
                                "🛍️ *%s* ha reseñado tu producto *%s*:\n\n" +
                                "📝 _\"%s\"_\n" +
                                "🏅 Calificación: *%d/5*\n" +
                                "📊 Nuevo rating del producto: *%.1f*",
                                client.getName(),
                                product.getName(),
                                request.getComment() != null && !request.getComment().isBlank() ? request.getComment() : "Sin comentario",
                                request.getRating(),
                                product.getRating()
                        );
                        smsService.sendSms(brandOwner.getPhoneNumber(), message);
                }

                return reviewMapper.toResponse(savedReview);
        }

        @Transactional
        public ReviewResponse updateProductReview(Long clientId, Long productId, ReviewRequest request) {
                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new ResourceNotFoundException("producto", "id", productId));

                ProductReview review = productReviewRepository.findByClientIdAndProductId(clientId, productId)
                                .orElseThrow(() -> new ResourceNotFoundException("reseña de producto", "productId",
                                                productId));

                review.setComment(request.getComment());
                review.setRating(request.getRating());

                ProductReview savedReview = productReviewRepository.save(review);
                recalculateProductRating(product);

                this.logger.info("El cliente con id " + clientId + " actualizo su reseña sobre el producto con id "
                                + productId);

                return reviewMapper.toResponse(savedReview);
        }

        @Transactional
        public void deleteProductReview(Long clientId, Long productId) {
                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new ResourceNotFoundException("producto", "id", productId));

                ProductReview review = productReviewRepository.findByClientIdAndProductId(clientId, productId)
                                .orElseThrow(() -> new ResourceNotFoundException("reseña de producto", "productId",
                                                productId));

                productReviewRepository.delete(review);
                recalculateProductRating(product);

                this.logger.info("El cliente con id " + clientId + " elimino su reseña sobre el producto con id "
                                + productId);
        }

        // ==================== RESEÑAS PÚBLICAS ====================

        public List<ReviewResponse> getReviewsForBrand(Long brandId) {
                brandRepository.findById(brandId)
                                .orElseThrow(() -> new ResourceNotFoundException("marca", "id", brandId));

                List<BrandReview> reviews = brandReviewRepository.findByBrandId(brandId);
                this.logger.info("Se devolvieron las reseñas publicas de la marca con id " + brandId);

                return reviews.stream()
                                .map(reviewMapper::toResponse)
                                .collect(Collectors.toList());
        }

        public List<ReviewResponse> getReviewsForProduct(Long productId) {
                productRepository.findById(productId)
                                .orElseThrow(() -> new ResourceNotFoundException("producto", "id", productId));

                List<ProductReview> reviews = productReviewRepository.findByProductId(productId);
                this.logger.info("Se devolvieron las reseñas publicas del producto con id " + productId);

                return reviews.stream()
                                .map(reviewMapper::toResponse)
                                .collect(Collectors.toList());
        }

        // ==================== RECALCULAR CALIFICACION DE MARCA Y PRODUCTO
        // ====================

        private void recalculateBrandRating(Brand brand) {
                List<BrandReview> reviews = brandReviewRepository.findByBrandId(brand.getId());
                int count = reviews.size();
                brand.setReviewCount(count);
                if (count == 0) {
                        brand.setRating(0.0);
                } else {
                        double avg = reviews.stream()
                                        .mapToInt(BrandReview::getRating)
                                        .average()
                                        .orElse(0.0);
                        brand.setRating(Math.round(avg * 10.0) / 10.0);
                }
                brandRepository.save(brand);
        }

        private void recalculateProductRating(Product product) {
                List<ProductReview> reviews = productReviewRepository.findByProductId(product.getId());
                int count = reviews.size();
                product.setReviewCount(count);
                if (count == 0) {
                        product.setRating(0.0);
                } else {
                        double avg = reviews.stream()
                                        .mapToInt(ProductReview::getRating)
                                        .average()
                                        .orElse(0.0);
                        product.setRating(Math.round(avg * 10.0) / 10.0);
                }
                productRepository.save(product);
        }
}
