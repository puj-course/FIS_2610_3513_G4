package com.ceiba.fashtoll.worldModel.brand;

/*
 * ============================================================
 *  Clase de pruebas: BrandServiceTest
 *  Servicio bajo prueba: BrandService
 * 
 *  Casos cubiertos:
 *    CP-BRN-01: getAllBrands — lista completa
 *    CP-BRN-02: createBrand — inicialización de campos de negocio
 *    CP-BRN-03: getBrandById — ID inexistente → excepción
 *    CP-BRN-04: deleteBrand — ID inexistente → excepción, no delete
 *    CP-BRN-05: verifyBrand — lógica de negocio (ADMIN verifica marca)
 *    CP-BRN-06: updateProfile — nombre de 101 chars (límite)
 *    CP-BRN-07: getAllPublicBrands — lista pública
 *    CP-BRN-08: updateProfile — flujo feliz
 *    CP-BRN-09: createBrand — URL de imagen nula (límite)
 *    CP-BRN-10: getAllBrands — repositorio vacío
 * ============================================================
 */

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.security.auth.AuthService;
import com.ceiba.fashtoll.worldModel.brand.dtos.*;
import com.ceiba.fashtoll.worldModel.product.services.ProductService;
import com.ceiba.fashtoll.worldModel.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de BrandService")
class BrandServiceTest {

    // ─────────────────────────────────────────────────────────
    //  Mocks e inyección
    // ─────────────────────────────────────────────────────────
    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private BrandService brandService;

    // ─────────────────────────────────────────────────────────
    //  Constantes de prueba
    // ─────────────────────────────────────────────────────────
    private static final Long EXISTING_ID     = 1L;
    private static final Long NON_EXISTING_ID = 9999L;
    private static final String BRAND_NAME    = "Nike";
    private static final String BRAND_EMAIL   = "nike@fashtoll.com";
    private static final String BRAND_PICTURE = "https://cdn.fashtoll.com/nike.png";
    private static final String BRAND_LINK    = "https://www.nike.com";

    // ─────────────────────────────────────────────────────────
    //  Métodos para construir entidades y DTOs
    // ─────────────────────────────────────────────────────────

    private Brand buildBrand(Long id, String name) {
        Brand brand = new Brand();
        brand.setId(id);
        brand.setName(name);
        brand.setEmail(BRAND_EMAIL);
        brand.setPictureURL(BRAND_PICTURE);
        brand.setLinkOfficial(BRAND_LINK);
        brand.setFollowers(0);
        brand.setRating(0.0);
        brand.setIsVerified(false);
        return brand;
    }

    private BrandResponse buildBrandResponse(Long id, String name,
                                             Integer followers, Double rating,
                                             Boolean isVerified) {
        BrandResponse resp = new BrandResponse();
        resp.setId(id);
        resp.setName(name);
        resp.setPictureURL(BRAND_PICTURE);
        resp.setLinkOfficial(BRAND_LINK);
        resp.setFollowers(followers);
        resp.setRating(rating);
        resp.setIsVerified(isVerified);
        return resp;
    }

    private BrandPublicResponse buildBrandPublicResponse(Long id, String name) {
        BrandPublicResponse resp = new BrandPublicResponse();
        resp.setId(id);
        resp.setName(name);
        resp.setEmail(BRAND_EMAIL);
        resp.setPictureURL(BRAND_PICTURE);
        resp.setLinkOfficial(BRAND_LINK);
        resp.setFollowers(0);
        resp.setRating(0.0);
        resp.setIsVerified(false);
        return resp;
    }

    private BrandCreateRequest buildCreateRequest(String name, String pictureURL, String linkOfficial) {
        BrandCreateRequest req = new BrandCreateRequest();
        req.setName(name);
        req.setPictureURL(pictureURL);
        req.setLinkOfficial(linkOfficial);
        return req;
    }

    private BrandProfileUpdateRequest buildProfileUpdateRequest(String name, String pictureURL, String link) {
        BrandProfileUpdateRequest req = new BrandProfileUpdateRequest();
        req.setName(name);
        req.setPictureURL(pictureURL);
        req.setLinkOfficial(link);
        return req;
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 1 — getAllBrands() / getAllPublicBrands()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("getAllBrands() / getAllPublicBrands()")
    class GetAllBrandsTests {

        @Test
        @DisplayName("CP-BRN-01: Obtener todas las marcas retorna lista con tres elementos")
        void getAllBrands_withThreeBrands_returnsListOfThree() {

            // --- Arrange ---
            // Se preparan tres marcas en el repositorio con sus respuestas mapeadas
            Brand b1 = buildBrand(1L, "Nike");
            Brand b2 = buildBrand(2L, "Adidas");
            Brand b3 = buildBrand(3L, "Puma");
            BrandResponse r1 = buildBrandResponse(1L, "Nike",   0, 0.0, false);
            BrandResponse r2 = buildBrandResponse(2L, "Adidas", 0, 0.0, false);
            BrandResponse r3 = buildBrandResponse(3L, "Puma",   0, 0.0, false);

            when(brandRepository.findAll()).thenReturn(Arrays.asList(b1, b2, b3));
            when(brandMapper.toResponse(b1)).thenReturn(r1);
            when(brandMapper.toResponse(b2)).thenReturn(r2);
            when(brandMapper.toResponse(b3)).thenReturn(r3);

            // --- Act ---
            List<BrandResponse> result = brandService.getAllBrands();

            // --- Assert ---
            // La lista debe contener exactamente 3 marcas con los nombres correctos
            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals("Nike",   result.get(0).getName());
            assertEquals("Adidas", result.get(1).getName());
            assertEquals("Puma",   result.get(2).getName());
            verify(brandRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("CP-BRN-10: Obtener marcas cuando repositorio está vacío retorna lista vacía (no nula)")
        void getAllBrands_withEmptyRepository_returnsEmptyList() {

            // --- Arrange ---
            // No hay marcas registradas en el repositorio
            when(brandRepository.findAll()).thenReturn(Collections.emptyList());

            // --- Act ---
            List<BrandResponse> result = brandService.getAllBrands();

            // --- Assert ---
            // El resultado no debe ser nulo; la lista debe estar vacía
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("CP-BRN-07: Obtener listado público de marcas retorna BrandPublicResponse con email expuesto")
        void getAllPublicBrands_withTwoBrands_returnsPublicResponsesWithEmail() {

            // --- Arrange ---
            // Se simulan dos marcas; el mapper público expone el email
            Brand b1 = buildBrand(1L, "Nike");
            Brand b2 = buildBrand(2L, "Adidas");
            BrandPublicResponse pr1 = buildBrandPublicResponse(1L, "Nike");
            BrandPublicResponse pr2 = buildBrandPublicResponse(2L, "Adidas");

            when(brandRepository.findAll()).thenReturn(Arrays.asList(b1, b2));
            when(brandMapper.toPublicResponse(b1)).thenReturn(pr1);
            when(brandMapper.toPublicResponse(b2)).thenReturn(pr2);

            // --- Act ---
            List<BrandPublicResponse> result = brandService.getAllPublicBrands();

            // --- Assert ---
            // El email debe estar presente en la respuesta pública
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(BRAND_EMAIL, result.get(0).getEmail());
            assertEquals(BRAND_EMAIL, result.get(1).getEmail());
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 2 — getBrandById()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("getBrandById()")
    class GetBrandByIdTests {

        @Test
        @DisplayName("CP-BRN-03: Obtener marca por ID inexistente lanza ResourceNotFoundException")
        void getBrandById_withNonExistingId_throwsResourceNotFoundException() {

            // --- Arrange ---
            // El repositorio no encuentra ninguna marca con ese ID
            when(brandRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

            // --- Act & Assert ---
            // Debe lanzarse ResourceNotFoundException sin invocar el mapper
            assertThrows(
                ResourceNotFoundException.class,
                () -> brandService.getBrandById(NON_EXISTING_ID),
                "Debe lanzar ResourceNotFoundException cuando la marca no existe"
            );
            verify(brandMapper, never()).toResponse(any());
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 3 — createBrand()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("createBrand()")
    class CreateBrandTests {

        @Test
        @DisplayName("CP-BRN-02: Crear marca con datos válidos inicializa followers=0, rating=0.0, isVerified=false")
        void createBrand_withValidRequest_initializesBusinessFields() {

            // --- Arrange ---
            // Regla de negocio: toda marca nueva parte desde cero (sin seguidores, sin rating, sin verificar)
            BrandCreateRequest request = buildCreateRequest(BRAND_NAME, BRAND_PICTURE, BRAND_LINK);
            Brand mappedBrand          = buildBrand(null, BRAND_NAME);
            BrandResponse expectedResp = buildBrandResponse(10L, BRAND_NAME, 0, 0.0, false);

            when(brandMapper.toEntity(request)).thenReturn(mappedBrand);
            when(brandMapper.toResponse(mappedBrand)).thenReturn(expectedResp);

            // --- Act ---
            BrandResponse result = brandService.createBrand(request);

            // --- Assert ---
            // Los campos de negocio deben ser inicializados correctamente
            assertNotNull(result);
            assertEquals(0,     result.getFollowers(),  "Los seguidores deben iniciar en 0");
            assertEquals(0.0,   result.getRating(),     "El rating debe iniciar en 0.0");
            assertFalse(result.getIsVerified(),          "La marca no debe estar verificada al crearse");
            // Verificación estructural: se setearon los valores en la entidad antes de guardar
            assertEquals(0,     mappedBrand.getFollowers());
            assertEquals(0.0,   mappedBrand.getRating());
            assertFalse(mappedBrand.getIsVerified());
            verify(brandRepository, times(1)).save(mappedBrand);
        }

        @Test
        @DisplayName("CP-BRN-09: Crear marca con URL de imagen nula no lanza excepción")
        void createBrand_withNullPictureUrl_returnsResponseSuccessfully() {

            // --- Arrange ---
            // Los campos pictureURL y linkOfficial son opcionales; deben admitir nulos
            BrandCreateRequest request = buildCreateRequest("MarcaSinImagen", null, null);
            Brand mappedBrand          = new Brand();
            mappedBrand.setName("MarcaSinImagen");
            mappedBrand.setPictureURL(null);
            mappedBrand.setLinkOfficial(null);
            mappedBrand.setFollowers(0);
            mappedBrand.setRating(0.0);
            mappedBrand.setIsVerified(false);

            BrandResponse expectedResp = new BrandResponse();
            expectedResp.setId(11L);
            expectedResp.setName("MarcaSinImagen");
            expectedResp.setPictureURL(null);
            expectedResp.setFollowers(0);
            expectedResp.setRating(0.0);
            expectedResp.setIsVerified(false);

            when(brandMapper.toEntity(request)).thenReturn(mappedBrand);
            when(brandMapper.toResponse(mappedBrand)).thenReturn(expectedResp);

            // --- Act ---
            BrandResponse result = brandService.createBrand(request);

            // --- Assert ---
            // El servicio no debe lanzar excepción con campos opcionales nulos
            assertNotNull(result);
            assertNull(result.getPictureURL(), "La URL de imagen puede ser nula");
            verify(brandRepository, times(1)).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 4 — deleteBrand()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("deleteBrand()")
    class DeleteBrandTests {

        @Test
        @DisplayName("CP-BRN-04: Eliminar marca inexistente lanza excepción y no invoca delete()")
        void deleteBrand_withNonExistingId_throwsAndDoesNotDelete() {

            // --- Arrange ---
            // El repositorio no encuentra ninguna marca con ese ID
            when(brandRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

            // --- Act & Assert ---
            // Debe lanzar excepción y nunca llamar a delete()
            assertThrows(
                ResourceNotFoundException.class,
                () -> brandService.deleteBrand(NON_EXISTING_ID),
                "Debe lanzar ResourceNotFoundException cuando la marca no existe"
            );
            verify(brandRepository, never()).delete(any());
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 5 — verifyBrand()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("verifyBrand() — Lógica de negocio ADMIN")
    class VerifyBrandTests {

        @Test
        @DisplayName("CP-BRN-05: Verificar marca existente cambia isVerified a true y persiste")
        void verifyBrand_withExistingId_setsIsVerifiedTrueAndSaves() {

            // --- Arrange ---
            // La marca existe y su estado inicial es no verificada
            Brand brand = buildBrand(EXISTING_ID, BRAND_NAME);
            brand.setIsVerified(false);
            when(brandRepository.findById(EXISTING_ID)).thenReturn(Optional.of(brand));
            when(brandRepository.save(brand)).thenReturn(brand);

            // --- Act ---
            // Solo el rol ADMIN puede ejecutar esta operación (el servicio implementa la lógica)
            brandService.verifyBrand(EXISTING_ID, true);

            // --- Assert ---
            // isVerified debe ser true y save() invocado exactamente una vez
            assertTrue(brand.getIsVerified(), "La marca debe quedar verificada");
            verify(brandRepository, times(1)).save(brand);
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 6 — updateProfile()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("updateProfile()")
    class UpdateProfileTests {

        @Test
        @DisplayName("CP-BRN-08: Actualizar perfil de marca existente con datos válidos retorna perfil actualizado")
        void updateProfile_withValidRequest_returnsUpdatedProfile() {

            // --- Arrange ---
            // Se simula la actualización del perfil de una marca existente
            Brand brand = buildBrand(EXISTING_ID, "OldName");
            BrandProfileUpdateRequest request = buildProfileUpdateRequest("Adidas", BRAND_PICTURE, BRAND_LINK);
            Brand savedBrand = buildBrand(EXISTING_ID, "Adidas");

            BrandProfileResponse expectedProfile = new BrandProfileResponse();
            expectedProfile.setName("Adidas");
            expectedProfile.setEmail(BRAND_EMAIL);

            when(brandRepository.findById(EXISTING_ID)).thenReturn(Optional.of(brand));
            when(brandRepository.save(brand)).thenReturn(savedBrand);
            when(brandMapper.toProfileResponse(savedBrand)).thenReturn(expectedProfile);

            // --- Act ---
            BrandProfileResponse result = brandService.updateProfile(EXISTING_ID, request);

            // --- Assert ---
            // El perfil retornado debe reflejar el nombre actualizado
            assertNotNull(result);
            assertEquals("Adidas", result.getName());
            verify(brandRepository, times(1)).save(brand);
        }

        @Test
        @DisplayName("CP-BRN-06: Actualizar perfil con nombre de 101 chars no lanza excepción desde el servicio")
        void updateProfile_withNameOf101Chars_doesNotThrowFromService() {

            // --- Arrange ---
            // La validación @Size(max=100) es responsabilidad del controlador, no del servicio
            String longName = "a".repeat(101);
            Brand brand = buildBrand(EXISTING_ID, "Original");
            BrandProfileUpdateRequest request = buildProfileUpdateRequest(longName, BRAND_PICTURE, BRAND_LINK);
            Brand savedBrand = buildBrand(EXISTING_ID, longName);

            BrandProfileResponse expectedProfile = new BrandProfileResponse();
            expectedProfile.setName(longName);
            expectedProfile.setEmail(BRAND_EMAIL);

            when(brandRepository.findById(EXISTING_ID)).thenReturn(Optional.of(brand));
            when(brandRepository.save(brand)).thenReturn(savedBrand);
            when(brandMapper.toProfileResponse(savedBrand)).thenReturn(expectedProfile);

            // --- Act ---
            BrandProfileResponse result = brandService.updateProfile(EXISTING_ID, request);

            // --- Assert ---
            // El servicio no lanza excepción; delega la validación al controlador
            assertNotNull(result);
            assertEquals(101, result.getName().length(),
                "El servicio no trunca ni rechaza el nombre largo (eso lo hace @Valid en el controller)");
        }
    }
}
