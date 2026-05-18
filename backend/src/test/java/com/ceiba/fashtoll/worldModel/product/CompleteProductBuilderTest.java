package com.ceiba.fashtoll.worldModel.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.utilities.enums.*;
import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.brand.BrandRepository;
import com.ceiba.fashtoll.worldModel.product.Builder.ProductDetails;
import com.ceiba.fashtoll.worldModel.product.Builder.builders.CompleteProductBuilder;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.entities.ProductType;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductTypeRepository;
import com.ceiba.fashtoll.worldModel.tag.Tag;
import com.ceiba.fashtoll.worldModel.tag.TagRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de CompleteProductBuilder, uno de los builders del patron usado en Product")
class CompleteProductBuilderTest {

    @Mock private BrandRepository brandRepository;
    @Mock private ProductRepository productRepository;
    @Mock private ProductTypeRepository productTypeRepository;
    @Mock private TagRepository tagRepository;

    @InjectMocks
    private CompleteProductBuilder builder;

    // -------------------------------------------------------------------------
    // CAMINO 1: FLUJO EXITOSO (CONSTRUCCIÓN COMPLETA)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("CompleteProductBuilder: Flujo exitoso construye el producto con todas sus propiedades")
    void buildProduct_happyPath_createsFullProduct() {
        Brand mockBrand = new Brand();
        mockBrand.setId(1L);
        mockBrand.setName("Nike");

        ProductType mockType = new ProductType();
        mockType.setId(2L);
        mockType.setName("Calzado");

        Tag mockTag = new Tag();
        mockTag.setName("RUNNING");

        when(brandRepository.findById(1L)).thenReturn(Optional.of(mockBrand));
        when(productTypeRepository.findById(2L)).thenReturn(Optional.of(mockType));
        when(tagRepository.findByName("RUNNING")).thenReturn(Optional.of(mockTag));

        LocalDateTime now = LocalDateTime.now();
        ProductDetails details = new ProductDetails("Air Max", "Zapatos pro", BigDecimal.valueOf(600), true, 4.5, now);

        // Act (Ejecutar la cadena del Builder)
        builder.reset();
        builder.associateBrand(1L, false);
        builder.putProductDetails(details);
        builder.putOfficialLink("https://nike.com");
        builder.putProductType(2L);
        builder.putEnums(GeneralFit.SLIM, Gender.UNISEX, Color.BLACK);
        builder.putImagesURLs(List.of("url1.png"));
        builder.putTags(List.of("RUNNING"));
        builder.setBrand(new Brand()); // Llama al método vacío para cobertura

        Product finalProduct = builder.getResult();

        // Assert (Verificar estado final del objeto construido)
        assertNotNull(finalProduct);
        assertEquals("Air Max", finalProduct.getName());
        assertEquals("Zapatos pro", finalProduct.getDescription());
        assertEquals(BigDecimal.valueOf(600), finalProduct.getPrice());
        assertTrue(finalProduct.getAvailable());
        assertEquals(4.5, finalProduct.getRating());
        assertEquals(now, finalProduct.getLastTimeEdited());
        assertEquals("https://nike.com", finalProduct.getLinkProduct());
        assertEquals(mockBrand, finalProduct.getBrand());
        assertEquals(mockType, finalProduct.getProductType());
        assertEquals(GeneralFit.SLIM, finalProduct.getGeneralFit());
        assertEquals(Gender.UNISEX, finalProduct.getGender());
        assertEquals(Color.BLACK, finalProduct.getColor());
        assertEquals(1, finalProduct.getImages().size());
        assertEquals(1, finalProduct.getTags().size());
    }

    // -------------------------------------------------------------------------
    // CAMINO 2: CASOS BIFURCACIONES NULAS (IF STATEMENTS)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("CompleteProductBuilder: Parámetros nulos en ID de marca o tipo no ejecutan búsquedas")
    void buildProduct_nullIds_ignoredSafely() {
        builder.reset();

        // No configuramos 'when' porque los métodos deben saltarse el bloque interno al ver nulos
        assertDoesNotThrow(() -> builder.associateBrand(null, false));
        assertDoesNotThrow(() -> builder.putProductType(null));

        Product product = builder.getResult();
        assertNull(product.getBrand());
        assertNull(product.getProductType());
    }

    // -------------------------------------------------------------------------
    // CAMINO 3: MANEJO DE EXCEPCIONES (RESOURCE NOT FOUND)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("CompleteProductBuilder: Lanza ResourceNotFoundException si la Marca no existe")
    void associateBrand_notFound_throwsException() {
        when(brandRepository.findById(99L)).thenReturn(Optional.empty());

        builder.reset();
        assertThrows(ResourceNotFoundException.class, () -> builder.associateBrand(99L, false));
    }

    @Test
    @DisplayName("CompleteProductBuilder: Lanza ResourceNotFoundException si el Tipo de Producto no existe")
    void putProductType_notFound_throwsException() {
        when(productTypeRepository.findById(99L)).thenReturn(Optional.empty());

        builder.reset();
        assertThrows(ResourceNotFoundException.class, () -> builder.putProductType(99L));
    }

    @Test
    @DisplayName("CompleteProductBuilder: Lanza ResourceNotFoundException si el Tag no existe")
    void putTags_notFound_throwsException() {
        when(tagRepository.findByName("INVALID")).thenReturn(Optional.empty());

        builder.reset();
        assertThrows(ResourceNotFoundException.class, () -> builder.putTags(List.of("INVALID")));
    }

    // -------------------------------------------------------------------------
    // CAMINO 4: MÉTODO DE ACTUALIZACIÓN (updateProductID)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("CompleteProductBuilder: updateProductID carga un producto existente con éxito")
    void updateProductID_happyPath_replacesResult() {
        Product existingProduct = new Product();
        existingProduct.setId(500L);
        existingProduct.setName("Producto Viejo");

        when(productRepository.findById(500L)).thenReturn(Optional.of(existingProduct));

        builder.updateProductID(500L);

        assertEquals(existingProduct, builder.getResult());
    }

    @Test
    @DisplayName("CompleteProductBuilder: updateProductID lanza excepción si el producto a actualizar no existe")
    void updateProductID_notFound_throwsException() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> builder.updateProductID(999L));
    }
}
