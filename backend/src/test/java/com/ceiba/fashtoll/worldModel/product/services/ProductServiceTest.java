package com.ceiba.fashtoll.worldModel.product.services;

/*
 * ============================================================
 *  Clase de pruebas: ProductServiceTest
 *  Servicio bajo prueba: ProductService
 *
 *  Casos cubiertos:
 *    CP-PRD-01: getAllProducts — lista completa
 *    CP-PRD-02: getProductById — ID existente
 *    CP-PRD-03: getProductById — ID inexistente → excepción
 *    CP-PRD-04: deleteProduct — ID inexistente → excepción, sin notify
 *    CP-PRD-05: getProductsByBrand — lista filtrada
 *    CP-PRD-06: updateSimpleProduct — brandId inexistente → excepción, sin save
 *    CP-PRD-07: getAllProducts — repositorio vacío
 *    CP-PRD-08: getProductByBrand — productId inexistente → excepción
 *    CP-PRD-09: createSimpleBrandProduct — lógica Observer (notify CREATED)
 *    CP-PRD-10: deleteProduct — notify DELETED al eliminar
 * ============================================================
 */

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.brand.BrandRepository;
import com.ceiba.fashtoll.worldModel.product.Builder.ProductBuilder;
import com.ceiba.fashtoll.worldModel.product.Observer.EventType;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEvent;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEventPublisher;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductC_U_Request;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductResponse;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.mappers.ProductMapper;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductTypeRepository;
import com.ceiba.fashtoll.worldModel.tag.TagRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de ProductService")
class ProductServiceTest {

    // ─────────────────────────────────────────────────────────
    // Mocks
    // ─────────────────────────────────────────────────────────
    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductTypeRepository productTypeRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ProductEventPublisher productEventPublisher;

    @Mock
    private ObjectProvider<ProductBuilder> simpleBuilderProvider;

    @Mock
    private ObjectProvider<ProductBuilder> simpleJsonBuilderProvider;

    @Mock
    private ProductBuilder productBuilder;

    @Mock
    private ObjectProvider<ProductBuilder> completeBuilderProvider;

    @Mock
    private ProductBuilder completeProductBuilder;

    private ProductService productService;

    // ─────────────────────────────────────────────────────────
    // Constantes de prueba
    // ─────────────────────────────────────────────────────────
    private static final Long EXISTING_PRODUCT_ID = 1L;
    private static final Long NON_EXISTING_PRODUCT_ID = 888L;
    private static final Long EXISTING_BRAND_ID = 10L;
    private static final Long NON_EXISTING_BRAND_ID = 9999L;
    private static final String PRODUCT_NAME = "Camiseta Negra";

    // ─────────────────────────────────────────────────────────
    // Setup por cada test
    // ─────────────────────────────────────────────────────────
    @BeforeEach
    void setUp() {
        productService = new ProductService(
                productRepository,
                brandRepository,
                productTypeRepository,
                productMapper,
                tagRepository,
                productEventPublisher);

        injectField(productService, "simpleBuilderProvider", simpleBuilderProvider);
        injectField(productService, "simpleJsonBuilderProvider", simpleJsonBuilderProvider);
        injectField(productService, "completeBuilderProvider", completeBuilderProvider);
    }

    private void injectField(Object target, String fieldName, Object value) {
        try {
            var field = ProductService.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("No se pudo inyectar el campo: " + fieldName, e);
        }
    }

    // ─────────────────────────────────────────────────────────
    // Métodos para construir entidades y DTOs
    // ─────────────────────────────────────────────────────────

    private Product buildProduct(Long id, String name, Long brandId) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(50_000));
        product.setAvailable(true);
        product.setRating(0.0);
        if (brandId != null) {
            Brand brand = buildBrand(brandId, "BrandTest");
            product.setBrand(brand);
        }
        return product;
    }

    private Brand buildBrand(Long id, String name) {
        Brand brand = new Brand();
        brand.setId(id);
        brand.setName(name);
        return brand;
    }

    private ProductResponse buildProductResponse(Long id, String name, Long brandId) {
        ProductResponse resp = new ProductResponse();
        resp.setId(id);
        resp.setName(name);
        resp.setBrandId(brandId);
        resp.setPrice(BigDecimal.valueOf(50_000));
        resp.setAvailable(true);
        resp.setRating(0.0);
        return resp;
    }

    private ProductC_U_Request buildCreateRequest(Long brandId, String name, BigDecimal price) {
        ProductC_U_Request req = new ProductC_U_Request();
        req.setBrandId(brandId);
        req.setProductTypeId(1L);
        req.setName(name);
        req.setPrice(price);
        req.setAvailable(true);
        return req;
    }

    private ProductC_U_Request buildAdminUpdateRequest(Long brandId) {
        ProductC_U_Request req = new ProductC_U_Request();
        req.setBrandId(brandId);
        req.setName("Updated Name");
        req.setPrice(BigDecimal.valueOf(60_000));
        return req;
    }

    // ═══════════════════════════════════════════════════════════
    // GRUPO 1 — getAllProducts()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("getAllProducts()")
    class GetAllProductsTests {

        @Test
        @DisplayName("CP-PRD-01: Obtener todos los productos retorna lista con tres elementos")
        void getAllProducts_withThreeProducts_returnsListOfThree() {

            // --- Arrange ---
            // Se preparan tres productos con sus respuestas mapeadas
            Product p1 = buildProduct(1L, "Camiseta", EXISTING_BRAND_ID);
            Product p2 = buildProduct(2L, "Pantalón", EXISTING_BRAND_ID);
            Product p3 = buildProduct(3L, "Chaqueta", EXISTING_BRAND_ID);
            ProductResponse r1 = buildProductResponse(1L, "Camiseta", EXISTING_BRAND_ID);
            ProductResponse r2 = buildProductResponse(2L, "Pantalón", EXISTING_BRAND_ID);
            ProductResponse r3 = buildProductResponse(3L, "Chaqueta", EXISTING_BRAND_ID);

            when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));
            when(productMapper.toResponse(p1)).thenReturn(r1);
            when(productMapper.toResponse(p2)).thenReturn(r2);
            when(productMapper.toResponse(p3)).thenReturn(r3);

            // --- Act ---
            List<ProductResponse> result = productService.getAllProducts();

            // --- Assert ---
            // La lista debe tener exactamente 3 productos
            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals("Camiseta", result.get(0).getName());
            assertEquals("Pantalón", result.get(1).getName());
            assertEquals("Chaqueta", result.get(2).getName());
        }

        @Test
        @DisplayName("CP-PRD-07: Obtener productos cuando repositorio está vacío retorna lista vacía")
        void getAllProducts_withEmptyRepository_returnsEmptyList() {

            // --- Arrange ---
            // No hay productos en el repositorio
            when(productRepository.findAll()).thenReturn(Collections.emptyList());

            // --- Act ---
            List<ProductResponse> result = productService.getAllProducts();

            // --- Assert ---
            // Debe retornarse lista vacía, no nula
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // GRUPO 2 — getProductById()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("getProductById()")
    class GetProductByIdTests {

        @Test
        @DisplayName("CP-PRD-02: Obtener producto por ID existente retorna datos correctos")
        void getProductById_withExistingId_returnsCorrectResponse() {

            // --- Arrange ---
            // El repositorio encuentra el producto con el ID solicitado
            Product product = buildProduct(EXISTING_PRODUCT_ID, PRODUCT_NAME, EXISTING_BRAND_ID);
            ProductResponse expected = buildProductResponse(EXISTING_PRODUCT_ID, PRODUCT_NAME, EXISTING_BRAND_ID);

            when(productRepository.findById(EXISTING_PRODUCT_ID)).thenReturn(Optional.of(product));
            when(productMapper.toResponse(product)).thenReturn(expected);

            // --- Act ---
            ProductResponse result = productService.getProductById(EXISTING_PRODUCT_ID);

            // --- Assert ---
            // El resultado debe contener el id y nombre correctos
            assertNotNull(result);
            assertEquals(EXISTING_PRODUCT_ID, result.getId());
            assertEquals(PRODUCT_NAME, result.getName());
        }

        @Test
        @DisplayName("CP-PRD-03: Obtener producto por ID inexistente lanza ResourceNotFoundException")
        void getProductById_withNonExistingId_throwsResourceNotFoundException() {

            // --- Arrange ---
            // El repositorio no encuentra ningún producto con ese ID
            when(productRepository.findById(NON_EXISTING_PRODUCT_ID)).thenReturn(Optional.empty());

            // --- Act & Assert ---
            // Debe lanzarse ResourceNotFoundException sin invocar el mapper
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> productService.getProductById(NON_EXISTING_PRODUCT_ID),
                    "Debe lanzar ResourceNotFoundException cuando el producto no existe");
            verify(productMapper, never()).toResponse(any());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // GRUPO 3 — deleteProduct()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("deleteProduct()")
    class DeleteProductTests {

        @Test
        @DisplayName("CP-PRD-04: Eliminar producto inexistente lanza excepción y no notifica el publisher")
        void deleteProduct_withNonExistingId_throwsAndDoesNotNotify() {

            // --- Arrange ---
            // El repositorio no encuentra el producto; el Observer no debe dispararse
            when(productRepository.findById(NON_EXISTING_PRODUCT_ID)).thenReturn(Optional.empty());

            // --- Act & Assert ---
            // Debe lanzar excepción; el publisher nunca debe recibir una notificación
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> productService.deleteProduct(NON_EXISTING_PRODUCT_ID),
                    "Debe lanzar ResourceNotFoundException cuando el producto no existe");
            verify(productEventPublisher, never()).notify(any());
        }

        @Test
        @DisplayName("CP-PRD-10: Eliminar producto existente invoca publisher con EventType.DELETED")
        void deleteProduct_withExistingId_notifiesObserverWithDeletedEvent() {

            // --- Arrange ---
            // El producto existe; la eliminación debe disparar el evento DELETED
            Product product = buildProduct(5L, "Vestido", EXISTING_BRAND_ID);
            when(productRepository.findById(5L)).thenReturn(Optional.of(product));

            // Capturar el evento que se envía al publisher
            ArgumentCaptor<ProductEvent> eventCaptor = ArgumentCaptor.forClass(ProductEvent.class);

            // --- Act ---
            productService.deleteProduct(5L);

            // --- Assert ---
            // Se verifica que delete() y notify() fueron invocados exactamente una vez
            // y que el evento es de tipo DELETED
            verify(productRepository, times(1)).delete(product);
            verify(productEventPublisher, times(1)).notify(eventCaptor.capture());
            assertEquals(EventType.DELETED, eventCaptor.getValue().getType(),
                    "El tipo de evento debe ser DELETED al eliminar un producto");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // GRUPO 4 — getProductsByBrand() / getProductByBrand()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("getProductsByBrand() / getProductByBrand()")
    class GetProductsByBrandTests {

        @Test
        @DisplayName("CP-PRD-05: Obtener productos por marca retorna lista filtrada de dos productos")
        void getProductsByBrand_withExistingBrand_returnsFilteredList() {

            // --- Arrange ---
            // Dos productos pertenecen a la misma marca
            Product p1 = buildProduct(1L, "Top", EXISTING_BRAND_ID);
            Product p2 = buildProduct(2L, "Short", EXISTING_BRAND_ID);
            ProductResponse r1 = buildProductResponse(1L, "Top", EXISTING_BRAND_ID);
            ProductResponse r2 = buildProductResponse(2L, "Short", EXISTING_BRAND_ID);

            when(productRepository.findByBrandId(EXISTING_BRAND_ID)).thenReturn(Arrays.asList(p1, p2));
            when(productMapper.toResponse(p1)).thenReturn(r1);
            when(productMapper.toResponse(p2)).thenReturn(r2);

            // --- Act ---
            List<ProductResponse> result = productService.getProductsByBrand(EXISTING_BRAND_ID);

            // --- Assert ---
            // Se retornan exactamente los 2 productos de la marca solicitada
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(r -> EXISTING_BRAND_ID.equals(r.getBrandId())));
        }

        @Test
        @DisplayName("CP-PRD-08: Obtener producto de marca con productId inexistente lanza excepción (valor límite)")
        void getProductByBrand_withNonExistingProductId_throwsResourceNotFoundException() {

            // --- Arrange ---
            // El productId no existe en el repositorio (caso borde)
            when(productRepository.findById(NON_EXISTING_PRODUCT_ID)).thenReturn(Optional.empty());

            // --- Act & Assert ---
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> productService.getProductByBrand(EXISTING_BRAND_ID, NON_EXISTING_PRODUCT_ID),
                    "Debe lanzar ResourceNotFoundException cuando el productId no existe");
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 5 — updateSimpleProduct()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("updateSimpleProduct() — Admin update")
    class UpdateProductTests {

        @Test
        @DisplayName("CP-PRD-06: Actualizar producto con brandId inexistente lanza excepción y no invoca save()")
        void updateProduct_withNonExistingBrandId_throwsAndDoesNotSave() {

            // --- Arrange ---
            // El producto existe pero la marca referenciada en el request no existe
            Product existingProduct   = buildProduct(EXISTING_PRODUCT_ID, PRODUCT_NAME, EXISTING_BRAND_ID);
            ProductC_U_Request request = buildAdminUpdateRequest(NON_EXISTING_BRAND_ID);

            when(simpleBuilderProvider.getObject()).thenReturn(productBuilder);
            doThrow(new com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException("marca", "id",
                    NON_EXISTING_BRAND_ID)).when(productBuilder).associateBrand(eq(NON_EXISTING_BRAND_ID), anyBoolean());

            // --- Act & Assert ---
            // Debe lanzar excepción y nunca guardar el producto modificado
            assertThrows(
                ResourceNotFoundException.class,
                () -> productService.updateSimpleProduct(EXISTING_PRODUCT_ID, request),
                "Debe lanzar ResourceNotFoundException cuando la marca del request no existe"
            );
            verify(productRepository, never()).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 6 — createSimpleBrandProduct()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("createSimpleBrandProduct() — Lógica Observer (BRAND crea producto)")
    class CreateBrandProductTests {

        @Test
        @DisplayName("CP-PRD-09: Crear producto de marca notifica publisher con EventType.CREATED exactamente una vez")
        void createBrandProduct_withValidRequest_notifiesObserverWithCreatedEvent() {

            // --- Arrange ---
            // Se simula el flujo completo: builder → producto → save → notify
            ProductC_U_Request request = buildCreateRequest(EXISTING_BRAND_ID, PRODUCT_NAME,
                                                              BigDecimal.valueOf(75_000));
            Product builtProduct  = buildProduct(null, PRODUCT_NAME, EXISTING_BRAND_ID);
            Product savedProduct  = buildProduct(50L, PRODUCT_NAME, EXISTING_BRAND_ID);
            ProductResponse resp  = buildProductResponse(50L, PRODUCT_NAME, EXISTING_BRAND_ID);

            // Se configura el ObjectProvider para retornar el builder mock
            when(simpleBuilderProvider.getObject()).thenReturn(productBuilder);
            // El builder devuelve el producto construido
            when(productBuilder.getResult()).thenReturn(builtProduct);
            when(productRepository.save(builtProduct)).thenReturn(savedProduct);
            when(productMapper.toResponse(savedProduct)).thenReturn(resp);

            // Capturar el evento del publisher
            ArgumentCaptor<ProductEvent> eventCaptor = ArgumentCaptor.forClass(ProductEvent.class);

            // --- Act ---
            // Solo el rol BRAND ejecuta este método (restricción impuesta en el controlador)
            ProductResponse result = productService.createSimpleBrandProduct(EXISTING_BRAND_ID, request);

            // --- Assert ---
            // El producto debe haberse guardado y el observer debe ser notificado con
            // CREATED
            assertNotNull(result);
            assertEquals(50L, result.getId());
            verify(productRepository, times(1)).save(builtProduct);
            verify(productEventPublisher, times(1)).notify(eventCaptor.capture());
            assertEquals(EventType.CREATED, eventCaptor.getValue().getType(),
                    "El tipo de evento debe ser CREATED al crear un producto de marca");
        }
    }

    @Test
    @DisplayName("CP-PRD-11: createProduct - Crea producto de forma general")
    void createProduct_createsAndReturnsProduct() {
        ProductC_U_Request request = buildCreateRequest(EXISTING_BRAND_ID, PRODUCT_NAME, BigDecimal.valueOf(75_000));
        Product builtProduct = buildProduct(null, PRODUCT_NAME, EXISTING_BRAND_ID);
        Product savedProduct = buildProduct(50L, PRODUCT_NAME, EXISTING_BRAND_ID);
        ProductResponse resp = buildProductResponse(50L, PRODUCT_NAME, EXISTING_BRAND_ID);

        when(simpleBuilderProvider.getObject()).thenReturn(productBuilder);
        when(productBuilder.getResult()).thenReturn(builtProduct);
        when(productRepository.save(builtProduct)).thenReturn(savedProduct);
        when(productMapper.toResponse(savedProduct)).thenReturn(resp);

        ProductResponse result = productService.createSimpleProduct(request);

        assertNotNull(result);
        assertEquals(50L, result.getId());
        verify(productRepository, times(1)).save(builtProduct);
        verify(productEventPublisher, times(1)).notify(any(ProductEvent.class));
    }

    @Test
    @DisplayName("CP-PRD-12: updateBrandProduct - Actualiza producto de una marca")
    void updateBrandProduct_updatesAndReturnsProduct() {
        com.ceiba.fashtoll.worldModel.product.dtos.ProductC_U_Request request = new com.ceiba.fashtoll.worldModel.product.dtos.ProductC_U_Request();
        Product builtProduct = buildProduct(50L, PRODUCT_NAME, EXISTING_BRAND_ID);
        Product savedProduct = buildProduct(50L, PRODUCT_NAME, EXISTING_BRAND_ID);
        ProductResponse resp = buildProductResponse(50L, PRODUCT_NAME, EXISTING_BRAND_ID);

        when(simpleBuilderProvider.getObject()).thenReturn(productBuilder);
        when(productBuilder.getResult()).thenReturn(builtProduct);
        when(productRepository.save(builtProduct)).thenReturn(savedProduct);
        when(productMapper.toResponse(savedProduct)).thenReturn(resp);

        ProductResponse result = productService.updateSimpleBrandProduct(EXISTING_BRAND_ID, 50L, request);

        assertNotNull(result);
        verify(productRepository, times(1)).save(builtProduct);
        verify(productEventPublisher, times(1)).notify(any(ProductEvent.class));
    }

    @Test
    @DisplayName("CP-PRD-13: deleteBrandProduct - Elimina producto de una marca")
    void deleteBrandProduct_deletesProductAndNotifies() {
        Product product = buildProduct(EXISTING_PRODUCT_ID, PRODUCT_NAME, EXISTING_BRAND_ID);
        when(productRepository.findById(EXISTING_PRODUCT_ID)).thenReturn(Optional.of(product));

        productService.deleteBrandProduct(EXISTING_BRAND_ID, EXISTING_PRODUCT_ID);

        verify(productRepository, times(1)).delete(product);
        verify(productEventPublisher, times(1)).notify(any(ProductEvent.class));
    }

    @Test
    @DisplayName("CP-PRD-14: injectBrandProductFromJson - Inyecta productos")
    void injectBrandProductFromJson_injectsProducts() {
        Brand brand = buildBrand(EXISTING_BRAND_ID, "BrandTest");
        ProductC_U_Request request = buildCreateRequest(EXISTING_BRAND_ID, PRODUCT_NAME, BigDecimal.valueOf(75_000));
        List<ProductC_U_Request> requestList = Collections.singletonList(request);
        Product builtProduct = buildProduct(null, PRODUCT_NAME, EXISTING_BRAND_ID);
        Product savedProduct = buildProduct(50L, PRODUCT_NAME, EXISTING_BRAND_ID);

        when(brandRepository.findByName("BrandTest")).thenReturn(Optional.of(brand));
        when(simpleJsonBuilderProvider.getObject()).thenReturn(productBuilder);
        when(productBuilder.getResult()).thenReturn(builtProduct);
        when(productRepository.save(builtProduct)).thenReturn(savedProduct);

        productService.injectBrandProductFromJson("BrandTest", requestList);

        verify(productRepository, times(1)).save(builtProduct);
        verify(productEventPublisher, times(1)).notify(any(ProductEvent.class));
    }

    // ═══════════════════════════════════════════════════════════
    // GRUPO 7 — createCompleteProduct() / createCompleteBrandProduct()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("createCompleteProduct() / createCompleteBrandProduct() — Builder completo")
    class CreateCompleteProductTests {

        @Test
        @DisplayName("CP-PRD-15: Crear producto completo (admin) guarda y notifica EventType.CREATED")
        void createCompleteProduct_withValidRequest_savesAndNotifiesCreated() {

            // --- Arrange ---
            // Se usa el builder completo vía completeBuilderProvider
            ProductC_U_Request request = buildCreateRequest(EXISTING_BRAND_ID, PRODUCT_NAME, BigDecimal.valueOf(90_000));
            Product builtProduct = buildProduct(null, PRODUCT_NAME, EXISTING_BRAND_ID);
            Product savedProduct = buildProduct(20L, PRODUCT_NAME, EXISTING_BRAND_ID);
            ProductResponse resp = buildProductResponse(20L, PRODUCT_NAME, EXISTING_BRAND_ID);

            when(completeBuilderProvider.getObject()).thenReturn(completeProductBuilder);
            when(completeProductBuilder.getResult()).thenReturn(builtProduct);
            when(productRepository.save(builtProduct)).thenReturn(savedProduct);
            when(productMapper.toResponse(savedProduct)).thenReturn(resp);

            ArgumentCaptor<ProductEvent> eventCaptor = ArgumentCaptor.forClass(ProductEvent.class);

            // --- Act ---
            ProductResponse result = productService.createCompleteProduct(request);

            // --- Assert ---
            // El producto debe guardarse y el publisher debe recibir CREATED
            assertNotNull(result);
            assertEquals(20L, result.getId());
            verify(productRepository, times(1)).save(builtProduct);
            verify(productEventPublisher, times(1)).notify(eventCaptor.capture());
            assertEquals(EventType.CREATED, eventCaptor.getValue().getType(),
                    "El tipo de evento debe ser CREATED al crear un producto completo");
        }

        @Test
        @DisplayName("CP-PRD-16: Crear producto completo de marca asigna brandId y notifica EventType.CREATED")
        void createCompleteBrandProduct_withValidRequest_setsBrandIdAndNotifiesCreated() {

            // --- Arrange ---
            // brandId se asigna al request antes de pasarlo al director
            ProductC_U_Request request = buildCreateRequest(null, PRODUCT_NAME, BigDecimal.valueOf(90_000));
            Product builtProduct = buildProduct(null, PRODUCT_NAME, EXISTING_BRAND_ID);
            Product savedProduct = buildProduct(21L, PRODUCT_NAME, EXISTING_BRAND_ID);
            ProductResponse resp = buildProductResponse(21L, PRODUCT_NAME, EXISTING_BRAND_ID);

            when(completeBuilderProvider.getObject()).thenReturn(completeProductBuilder);
            when(completeProductBuilder.getResult()).thenReturn(builtProduct);
            when(productRepository.save(builtProduct)).thenReturn(savedProduct);
            when(productMapper.toResponse(savedProduct)).thenReturn(resp);

            ArgumentCaptor<ProductEvent> eventCaptor = ArgumentCaptor.forClass(ProductEvent.class);

            // --- Act ---
            ProductResponse result = productService.createCompleteBrandProduct(EXISTING_BRAND_ID, request);

            // --- Assert ---
            // El request debe tener el brandId asignado y el publisher debe ser notificado
            assertNotNull(result);
            assertEquals(21L, result.getId());
            assertEquals(EXISTING_BRAND_ID, request.getBrandId(),
                    "El brandId del request debe asignarse antes de invocar el director");
            verify(productRepository, times(1)).save(builtProduct);
            verify(productEventPublisher, times(1)).notify(eventCaptor.capture());
            assertEquals(EventType.CREATED, eventCaptor.getValue().getType(),
                    "El tipo de evento debe ser CREATED al crear un producto completo de marca");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // GRUPO 8 — updateCompleteProduct() / updateCompleteBrandProduct()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("updateCompleteProduct() / updateCompleteBrandProduct() — Builder completo")
    class UpdateCompleteProductTests {

        @Test
        @DisplayName("CP-PRD-17: Actualizar producto completo (admin) guarda y notifica al publisher")
        void updateCompleteProduct_withValidRequest_savesAndNotifies() {

            // --- Arrange ---
            ProductC_U_Request request = buildAdminUpdateRequest(EXISTING_BRAND_ID);
            Product builtProduct = buildProduct(EXISTING_PRODUCT_ID, "Updated Name", EXISTING_BRAND_ID);
            Product savedProduct = buildProduct(EXISTING_PRODUCT_ID, "Updated Name", EXISTING_BRAND_ID);
            ProductResponse resp = buildProductResponse(EXISTING_PRODUCT_ID, "Updated Name", EXISTING_BRAND_ID);

            when(completeBuilderProvider.getObject()).thenReturn(completeProductBuilder);
            when(completeProductBuilder.getResult()).thenReturn(builtProduct);
            when(productRepository.save(builtProduct)).thenReturn(savedProduct);
            when(productMapper.toResponse(savedProduct)).thenReturn(resp);

            // --- Act ---
            ProductResponse result = productService.updateCompleteProduct(EXISTING_PRODUCT_ID, request);

            // --- Assert ---
            // Debe invocarse save() y notify(); el nombre debe actualizarse
            assertNotNull(result);
            assertEquals("Updated Name", result.getName());
            verify(productRepository, times(1)).save(builtProduct);
            verify(productEventPublisher, times(1)).notify(any(ProductEvent.class));
        }

        @Test
        @DisplayName("CP-PRD-18: Actualizar producto completo de marca guarda y notifica EventType.UPDATED")
        void updateCompleteBrandProduct_withValidRequest_savesAndNotifiesUpdated() {

            // --- Arrange ---
            ProductC_U_Request request = buildAdminUpdateRequest(EXISTING_BRAND_ID);
            Product builtProduct = buildProduct(EXISTING_PRODUCT_ID, "Updated Name", EXISTING_BRAND_ID);
            Product savedProduct = buildProduct(EXISTING_PRODUCT_ID, "Updated Name", EXISTING_BRAND_ID);
            ProductResponse resp = buildProductResponse(EXISTING_PRODUCT_ID, "Updated Name", EXISTING_BRAND_ID);

            when(completeBuilderProvider.getObject()).thenReturn(completeProductBuilder);
            when(completeProductBuilder.getResult()).thenReturn(builtProduct);
            when(productRepository.save(builtProduct)).thenReturn(savedProduct);
            when(productMapper.toResponse(savedProduct)).thenReturn(resp);

            ArgumentCaptor<ProductEvent> eventCaptor = ArgumentCaptor.forClass(ProductEvent.class);

            // --- Act ---
            ProductResponse result = productService.updateCompleteBrandProduct(
                    EXISTING_BRAND_ID, EXISTING_PRODUCT_ID, request);

            // --- Assert ---
            // El evento debe ser UPDATED y save() debe invocarse exactamente una vez
            assertNotNull(result);
            verify(productRepository, times(1)).save(builtProduct);
            verify(productEventPublisher, times(1)).notify(eventCaptor.capture());
            assertEquals(EventType.UPDATED, eventCaptor.getValue().getType(),
                    "El tipo de evento debe ser UPDATED al actualizar un producto completo de marca");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // GRUPO 9 — Happy paths faltantes y edge cases adicionales
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("Paths adicionales: updateSimpleProduct, getProductByBrand, deleteBrandProduct, injectBrandProductFromJson")
    class AdditionalCoverageTests {

        @Test
        @DisplayName("CP-PRD-19: updateSimpleProduct — flujo exitoso guarda y notifica EventType.UPDATED")
        void updateSimpleProduct_withValidRequest_savesAndNotifiesUpdated() {

            // --- Arrange ---
            // Flujo exitoso: el builder construye el producto sin lanzar excepción
            ProductC_U_Request request = buildAdminUpdateRequest(EXISTING_BRAND_ID);
            Product builtProduct = buildProduct(EXISTING_PRODUCT_ID, "Updated Name", EXISTING_BRAND_ID);
            Product savedProduct = buildProduct(EXISTING_PRODUCT_ID, "Updated Name", EXISTING_BRAND_ID);
            ProductResponse resp = buildProductResponse(EXISTING_PRODUCT_ID, "Updated Name", EXISTING_BRAND_ID);

            when(simpleBuilderProvider.getObject()).thenReturn(productBuilder);
            when(productBuilder.getResult()).thenReturn(builtProduct);
            when(productRepository.save(builtProduct)).thenReturn(savedProduct);
            when(productMapper.toResponse(savedProduct)).thenReturn(resp);

            ArgumentCaptor<ProductEvent> eventCaptor = ArgumentCaptor.forClass(ProductEvent.class);

            // --- Act ---
            ProductResponse result = productService.updateSimpleProduct(EXISTING_PRODUCT_ID, request);

            // --- Assert ---
            // El producto actualizado debe retornarse con el nombre correcto y notificar UPDATED
            assertNotNull(result);
            assertEquals("Updated Name", result.getName());
            verify(productRepository, times(1)).save(builtProduct);
            verify(productEventPublisher, times(1)).notify(eventCaptor.capture());
            assertEquals(EventType.UPDATED, eventCaptor.getValue().getType(),
                    "El tipo de evento debe ser UPDATED al actualizar un producto simple");
        }

        @Test
        @DisplayName("CP-PRD-20: getProductByBrand — productId existente retorna datos correctos")
        void getProductByBrand_withExistingProductId_returnsCorrectResponse() {

            // --- Arrange ---
            // El producto existe; se retorna la respuesta mapeada
            Product product = buildProduct(EXISTING_PRODUCT_ID, PRODUCT_NAME, EXISTING_BRAND_ID);
            ProductResponse expected = buildProductResponse(EXISTING_PRODUCT_ID, PRODUCT_NAME, EXISTING_BRAND_ID);

            when(productRepository.findById(EXISTING_PRODUCT_ID)).thenReturn(Optional.of(product));
            when(productMapper.toResponse(product)).thenReturn(expected);

            // --- Act ---
            ProductResponse result = productService.getProductByBrand(EXISTING_BRAND_ID, EXISTING_PRODUCT_ID);

            // --- Assert ---
            // El resultado debe contener el id y nombre correctos del producto
            assertNotNull(result);
            assertEquals(EXISTING_PRODUCT_ID, result.getId());
            assertEquals(PRODUCT_NAME, result.getName());
        }

        @Test
        @DisplayName("CP-PRD-21: deleteBrandProduct — productId inexistente lanza excepción sin invocar delete()")
        void deleteBrandProduct_withNonExistingProductId_throwsAndDoesNotDelete() {

            // --- Arrange ---
            // El producto no existe; la lambda del orElseThrow debe activarse
            when(productRepository.findById(NON_EXISTING_PRODUCT_ID)).thenReturn(Optional.empty());

            // --- Act & Assert ---
            // Debe lanzar excepción; delete() y notify() nunca deben invocarse
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> productService.deleteBrandProduct(EXISTING_BRAND_ID, NON_EXISTING_PRODUCT_ID),
                    "Debe lanzar ResourceNotFoundException cuando el producto de la marca no existe"
            );
            verify(productRepository, never()).delete(any(Product.class));
            verify(productEventPublisher, never()).notify(any());
        }

        @Test
        @DisplayName("CP-PRD-22: injectBrandProductFromJson — brand inexistente lanza excepción sin guardar productos")
        void injectBrandProductFromJson_withNonExistingBrand_throwsResourceNotFoundException() {

            // --- Arrange ---
            // La marca no existe; la lambda del orElseThrow de brandRepository debe activarse
            ProductC_U_Request request = buildCreateRequest(null, PRODUCT_NAME, BigDecimal.valueOf(50_000));
            List<ProductC_U_Request> requestList = Collections.singletonList(request);

            when(brandRepository.findByName("BrandInexistente")).thenReturn(Optional.empty());

            // --- Act & Assert ---
            // Debe lanzar ResourceNotFoundException antes de guardar cualquier producto
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> productService.injectBrandProductFromJson("BrandInexistente", requestList),
                    "Debe lanzar ResourceNotFoundException cuando la marca no existe"
            );
            verify(productRepository, never()).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // GRUPO 10 — Constructor @Autowired (4-arg)
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("Constructor @Autowired de 4 argumentos")
    class PrimaryConstructorTests {

        @Test
        @DisplayName("CP-PRD-23: Constructor 4-arg crea instancia válida y getAllProducts() funciona correctamente")
        void primaryConstructor_createsValidInstance_getAllProductsWorks() {

            // --- Arrange ---
            // El constructor @Autowired asigna null a productTypeRepository y tagRepository;
            // se verifica que los métodos que no dependen de esos campos siguen funcionando
            ProductService serviceWith4Args = new ProductService(
                    productRepository, brandRepository, productMapper, productEventPublisher);

            Product p1 = buildProduct(1L, "Zapato", EXISTING_BRAND_ID);
            ProductResponse r1 = buildProductResponse(1L, "Zapato", EXISTING_BRAND_ID);

            when(productRepository.findAll()).thenReturn(Collections.singletonList(p1));
            when(productMapper.toResponse(p1)).thenReturn(r1);

            // --- Act ---
            List<ProductResponse> result = serviceWith4Args.getAllProducts();

            // --- Assert ---
            // El servicio instanciado con 4 args debe retornar los productos del repositorio
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Zapato", result.get(0).getName());
        }
    }
}
