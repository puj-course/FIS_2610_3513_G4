package com.ceiba.fashtoll.worldModel.client;

/*
 * ============================================================
 *  Clase de pruebas: ClientServiceTest
 *  Servicio bajo prueba: ClientService
 *
 *  Casos cubiertos:
 *    - CP-CLI-01: getAllClients — lista completa
 *    - CP-CLI-02: getClientById — ID existente
 *    - CP-CLI-03: getClientById — ID inexistente → excepción
 *    - CP-CLI-04: createClient — datos válidos
 *    - CP-CLI-05: updateClient — ID inexistente → excepción
 *    - CP-CLI-06: updateClient — nombre vacío no lanza excepción
 *    - CP-CLI-07: getProfile — ID existente
 *    - CP-CLI-08: deleteClient — ID existente
 *    - CP-CLI-09: getAllClients — repositorio vacío
 *    - CP-CLI-10: createClient — caso limite nombre de 100 caracteres
 * ============================================================
 */

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.security.auth.AuthService;
import com.ceiba.fashtoll.worldModel.client.dtos.*;
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
@DisplayName("Pruebas unitarias de ClientService")
class ClientServiceTest {

    // ─────────────────────────────────────────────────────────
    //  Mocks e inyección
    // ─────────────────────────────────────────────────────────
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ClientService clientService;

    // ─────────────────────────────────────────────────────────
    //  Constantes de prueba
    // ─────────────────────────────────────────────────────────
    private static final Long EXISTING_ID    = 1L;
    private static final Long NON_EXISTING_ID = 999L;
    private static final String CLIENT_NAME   = "Ana García";
    private static final String CLIENT_EMAIL  = "ana@fashtoll.com";

    // ─────────────────────────────────────────────────────────
    //  Métodos para construir entidades y DTOs
    // ─────────────────────────────────────────────────────────

    private Client buildClient(Long id, String name) {
        Client client = new Client();
        client.setId(id);
        client.setName(name);
        client.setEmail(CLIENT_EMAIL);
        return client;
    }

    private ClientResponse buildClientResponse(Long id, String name) {
        ClientResponse response = new ClientResponse();
        response.setId(id);
        response.setName(name);
        return response;
    }

    private ClientCreateRequest buildCreateRequest(Long userId, String name) {
        ClientCreateRequest req = new ClientCreateRequest();
        req.setUserId(userId);
        req.setName(name);
        return req;
    }

    private ClientUpdateRequest buildUpdateRequest(String name) {
        ClientUpdateRequest req = new ClientUpdateRequest();
        req.setName(name);
        return req;
    }

    private ClientProfileUpdateRequest buildProfileUpdateRequest(String name) {
        ClientProfileUpdateRequest req = new ClientProfileUpdateRequest();
        req.setName(name);
        return req;
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 1 — getAllClients()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("getAllClients()")
    class GetAllClientsTests {

        @Test
        @DisplayName("CP-CLI-01: Obtener todos los clientes retorna lista con dos elementos")
        void getAllClients_withTwoClients_returnsListOfTwo() {

            // --- Arrange ---
            // Se preparan dos clientes en el repositorio y sus respuestas mapeadas
            Client c1 = buildClient(1L, "Ana");
            Client c2 = buildClient(2L, "Luis");
            ClientResponse r1 = buildClientResponse(1L, "Ana");
            ClientResponse r2 = buildClientResponse(2L, "Luis");

            when(clientRepository.findAll()).thenReturn(Arrays.asList(c1, c2));
            when(clientMapper.toResponse(c1)).thenReturn(r1);
            when(clientMapper.toResponse(c2)).thenReturn(r2);

            // --- Act ---
            // Se invoca el método bajo prueba
            List<ClientResponse> result = clientService.getAllClients();

            // --- Assert ---
            // La lista debe tener exactamente 2 elementos con los datos correctos
            assertNotNull(result, "El resultado no debe ser nulo");
            assertEquals(2, result.size(), "Debe retornar exactamente 2 clientes");
            assertEquals("Ana",  result.get(0).getName());
            assertEquals("Luis", result.get(1).getName());
            verify(clientRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("CP-CLI-09: Lista de clientes cuando repositorio está vacío retorna lista vacía (no nula)")
        void getAllClients_withEmptyRepository_returnsEmptyList() {

            // --- Arrange ---
            // El repositorio no contiene ningún cliente
            when(clientRepository.findAll()).thenReturn(Collections.emptyList());

            // --- Act ---
            List<ClientResponse> result = clientService.getAllClients();

            // --- Assert ---
            // Debe retornarse lista vacía, no nula
            assertNotNull(result, "El resultado no debe ser nulo aunque el repositorio esté vacío");
            assertTrue(result.isEmpty(), "La lista debe estar vacía");
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 2 — getClientById()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("getClientById()")
    class GetClientByIdTests {

        @Test
        @DisplayName("CP-CLI-02: Obtener cliente por ID existente retorna ClientResponse correcto")
        void getClientById_withExistingId_returnsCorrectResponse() {

            // --- Arrange ---
            // El repositorio encuentra al cliente con el ID solicitado
            Client client = buildClient(EXISTING_ID, CLIENT_NAME);
            ClientResponse expected = buildClientResponse(EXISTING_ID, CLIENT_NAME);

            when(clientRepository.findById(EXISTING_ID)).thenReturn(Optional.of(client));
            when(clientMapper.toResponse(client)).thenReturn(expected);

            // --- Act ---
            ClientResponse result = clientService.getClientById(EXISTING_ID);

            // --- Assert ---
            // La respuesta debe contener el id y el nombre correctos
            assertNotNull(result);
            assertEquals(EXISTING_ID, result.getId());
            assertEquals(CLIENT_NAME, result.getName());
        }

        @Test
        @DisplayName("CP-CLI-03: Obtener cliente por ID inexistente lanza ResourceNotFoundException")
        void getClientById_withNonExistingId_throwsResourceNotFoundException() {

            // --- Arrange ---
            // El repositorio no encuentra ningún cliente con ese ID
            when(clientRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

            // --- Act & Assert ---
            // Debe lanzarse ResourceNotFoundException
            assertThrows(
                ResourceNotFoundException.class,
                () -> clientService.getClientById(NON_EXISTING_ID),
                "Debe lanzar ResourceNotFoundException cuando el cliente no existe"
            );
            verify(clientMapper, never()).toResponse(any());
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 3 — createClient()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("createClient()")
    class CreateClientTests {

        @Test
        @DisplayName("CP-CLI-04: Crear cliente con datos válidos persiste y retorna respuesta")
        void createClient_withValidRequest_savesAndReturnsResponse() {

            // --- Arrange ---
            // Se simula la conversión DTO → entidad → persistencia → DTO de respuesta
            ClientCreateRequest request  = buildCreateRequest(1L, "Carlos");
            Client mappedEntity          = buildClient(null, "Carlos");
            Client savedClient           = buildClient(10L, "Carlos");
            ClientResponse expectedResp  = buildClientResponse(10L, "Carlos");

            when(clientMapper.toEntity(request)).thenReturn(mappedEntity);
            when(clientRepository.save(mappedEntity)).thenReturn(savedClient);
            when(clientMapper.toResponse(savedClient)).thenReturn(expectedResp);

            // --- Act ---
            ClientResponse result = clientService.createClient(request);

            // --- Assert ---
            // Se verifica que se guardó y se mapeó correctamente
            assertNotNull(result);
            assertEquals("Carlos", result.getName());
            assertEquals(10L, result.getId());
            verify(clientRepository, times(1)).save(mappedEntity);
        }

        @Test
        @DisplayName("CP-CLI-10: Crear cliente con nombre de exactamente 100 caracteres (límite máximo) funciona correctamente")
        void createClient_withNameOf100Chars_returnsResponseSuccessfully() {

            // --- Arrange ---
            // El nombre tiene exactamente 100 caracteres (valor límite permitido)
            String maxName = "x".repeat(100);
            ClientCreateRequest request  = buildCreateRequest(2L, maxName);
            Client mappedEntity          = buildClient(null, maxName);
            Client savedClient           = buildClient(20L, maxName);
            ClientResponse expectedResp  = buildClientResponse(20L, maxName);

            when(clientMapper.toEntity(request)).thenReturn(mappedEntity);
            when(clientRepository.save(mappedEntity)).thenReturn(savedClient);
            when(clientMapper.toResponse(savedClient)).thenReturn(expectedResp);

            // --- Act ---
            ClientResponse result = clientService.createClient(request);

            // --- Assert ---
            // El servicio no rechaza el valor límite; la respuesta conserva el nombre completo
            assertNotNull(result);
            assertEquals(100, result.getName().length());
            verify(clientRepository, times(1)).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 4 — updateClient()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("updateClient()")
    class UpdateClientTests {

        @Test
        @DisplayName("CP-CLI-05: Actualizar cliente con ID inexistente lanza excepción y no llama a save()")
        void updateClient_withNonExistingId_throwsAndDoesNotSave() {

            // --- Arrange ---
            // El repositorio no encuentra el cliente
            when(clientRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
            ClientUpdateRequest request = buildUpdateRequest("Nuevo Nombre");

            // --- Act & Assert ---
            // Debe lanzar excepción y nunca persistir
            assertThrows(
                ResourceNotFoundException.class,
                () -> clientService.updateClient(NON_EXISTING_ID, request),
                "Debe lanzar ResourceNotFoundException cuando el cliente no existe"
            );
            verify(clientRepository, never()).save(any());
        }

        @Test
        @DisplayName("CP-CLI-06: Actualizar cliente con nombre vacío no lanza excepción (responsabilidad del controlador)")
        void updateClient_withEmptyName_doesNotThrowFromService() {

            // --- Arrange ---
            // El servicio acepta el string vacío; la validación Bean corresponde al controlador
            Client client          = buildClient(EXISTING_ID, "Ana");
            Client savedClient     = buildClient(EXISTING_ID, "");
            ClientUpdateRequest req = buildUpdateRequest("");
            ClientResponse resp     = buildClientResponse(EXISTING_ID, "");

            when(clientRepository.findById(EXISTING_ID)).thenReturn(Optional.of(client));
            when(clientRepository.save(client)).thenReturn(savedClient);
            when(clientMapper.toResponse(savedClient)).thenReturn(resp);

            // --- Act ---
            ClientResponse result = clientService.updateClient(EXISTING_ID, req);

            // --- Assert ---
            // El servicio retorna sin lanzar excepción
            assertNotNull(result);
            verify(clientRepository, times(1)).save(client);
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 5 — getProfile() y updateProfile()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("getProfile() / updateProfile()")
    class ProfileTests {

        @Test
        @DisplayName("CP-CLI-07: Obtener perfil de cliente existente retorna ClientProfileResponse con nombre y email")
        void getProfile_withExistingId_returnsProfileWithNameAndEmail() {

            // --- Arrange ---
            // Se espera que el perfil incluya nombre y email del cliente
            Client client = buildClient(EXISTING_ID, CLIENT_NAME);

            ClientProfileResponse expectedProfile = new ClientProfileResponse();
            expectedProfile.setName(CLIENT_NAME);
            expectedProfile.setEmail(CLIENT_EMAIL);

            when(clientRepository.findById(EXISTING_ID)).thenReturn(Optional.of(client));
            when(clientMapper.toProfileResponse(client)).thenReturn(expectedProfile);

            // --- Act ---
            ClientProfileResponse result = clientService.getProfile(EXISTING_ID);

            // --- Assert ---
            // El perfil contiene nombre y email exactamente como están en la entidad
            assertNotNull(result);
            assertEquals(CLIENT_NAME,  result.getName());
            assertEquals(CLIENT_EMAIL, result.getEmail());
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  GRUPO 6 — deleteClient()
    // ═══════════════════════════════════════════════════════════
    @Nested
    @DisplayName("deleteClient()")
    class DeleteClientTests {

        @Test
        @DisplayName("CP-CLI-08: Eliminar cliente existente invoca delete() exactamente una vez")
        void deleteClient_withExistingId_callsDeleteOnRepository() {

            // --- Arrange ---
            // El repositorio devuelve el cliente que será eliminado
            Client client = buildClient(EXISTING_ID, CLIENT_NAME);
            when(clientRepository.findById(EXISTING_ID)).thenReturn(Optional.of(client));

            // --- Act ---
            clientService.deleteClient(EXISTING_ID);

            // --- Assert ---
            // Verificación de lógica de negocio: delete() debe invocarse exactamente una vez
            verify(clientRepository, times(1)).delete(client);
        }
    }
}
