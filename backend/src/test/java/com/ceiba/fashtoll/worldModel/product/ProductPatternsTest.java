package com.ceiba.fashtoll.worldModel.product;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.utilities.enums.Color;
import com.ceiba.fashtoll.utilities.enums.Gender;
import com.ceiba.fashtoll.utilities.enums.GeneralFit;
import com.ceiba.fashtoll.worldModel.brand.Brand;
import com.ceiba.fashtoll.worldModel.brand.BrandRepository;
import com.ceiba.fashtoll.worldModel.product.Builder.ProductDetails;
import com.ceiba.fashtoll.worldModel.product.Builder.builders.SimpleJsonProductBuilder;
import com.ceiba.fashtoll.worldModel.product.Builder.builders.SimpleProductBuilder;
import com.ceiba.fashtoll.worldModel.product.Observer.EventType;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEvent;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductEventPublisher;
import com.ceiba.fashtoll.worldModel.product.Observer.ProductObserver;
import com.ceiba.fashtoll.worldModel.product.entities.Product;
import com.ceiba.fashtoll.worldModel.product.entities.ProductType;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductRepository;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de Patrones de Diseño (Product)")
class ProductPatternsTest {

    @Mock
    private BrandRepository brandRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductTypeRepository productTypeRepository;
    @Mock
    private ProductObserver observer;

    private SimpleProductBuilder simpleProductBuilder;
    private SimpleJsonProductBuilder simpleJsonProductBuilder;
    private ProductEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        simpleProductBuilder = new SimpleProductBuilder(brandRepository, productRepository, productTypeRepository);
        simpleJsonProductBuilder = new SimpleJsonProductBuilder(brandRepository, productTypeRepository);
        eventPublisher = new ProductEventPublisher();
    }

    @Test
    @DisplayName("Observer: Suscribir, notificar y desuscribir funciona correctamente")
    void observer_fullCycle_works() {
        Product product = new Product();
        ProductEvent event = new ProductEvent(product, EventType.CREATED);

        eventPublisher.subscribe(observer);
        assertEquals(1, eventPublisher.getObserverCount());

        // Suscribir de nuevo no debería duplicar
        eventPublisher.subscribe(observer);
        assertEquals(1, eventPublisher.getObserverCount());

        eventPublisher.notify(event);
        verify(observer, times(1)).onProductEvent(event);

        eventPublisher.unsubscribe(observer);
        assertEquals(0, eventPublisher.getObserverCount());

        eventPublisher.notify(event);
        verify(observer, times(1)).onProductEvent(event); // Sigue siendo 1 porque ya se desuscribió
    }

    @Test
    @DisplayName("Builder: SimpleProductBuilder construye producto completo")
    void simpleProductBuilder_buildsProduct() {
        simpleProductBuilder.reset();

        Brand brand = new Brand();
        brand.setId(1L);
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        ProductType type = new ProductType();
        type.setId(2L);
        when(productTypeRepository.findById(2L)).thenReturn(Optional.of(type));

        ProductDetails details = new ProductDetails("Remera", "Desc", java.math.BigDecimal.valueOf(100.0), true, 4.5,
                LocalDateTime.now());

        simpleProductBuilder.associateBrand(1L);
        simpleProductBuilder.putProductDetails(details);
        simpleProductBuilder.putOfficialLink("http://link.com");
        simpleProductBuilder.putProductType(2L);
        simpleProductBuilder.putEnums(GeneralFit.REGULAR, Gender.UNISEX, Color.BLACK);
        simpleProductBuilder.putImagesURLs(Collections.singletonList("img.jpg"));
        simpleProductBuilder.putTags(Collections.singletonList(3L));

        Product result = simpleProductBuilder.getResult();

        assertNotNull(result);
        assertEquals("Remera", result.getName());
        assertEquals(brand, result.getBrand());
        assertEquals(type, result.getProductType());
        assertEquals(GeneralFit.REGULAR, result.getGeneralFit());
        assertEquals("http://link.com", result.getLinkProduct());
    }

    @Test
    @DisplayName("Builder: SimpleProductBuilder lanza excepción si marca no existe")
    void simpleProductBuilder_throwsException_whenBrandNotFound() {
        simpleProductBuilder.reset();
        when(brandRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> simpleProductBuilder.associateBrand(99L));
    }

    @Test
    @DisplayName("Builder: adminUpdateProduct carga producto existente")
    void simpleProductBuilder_adminUpdateProduct_loadsExisting() {
        Product existing = new Product();
        existing.setId(5L);
        when(productRepository.findById(5L)).thenReturn(Optional.of(existing));

        simpleProductBuilder.adminUpdateProduct(5L);
        assertEquals(existing, simpleProductBuilder.getResult());
    }

    @Test
    @DisplayName("Builder: SimpleJsonProductBuilder construye producto desde JSON logic")
    void simpleJsonProductBuilder_buildsProduct() {
        simpleJsonProductBuilder.reset();

        Brand brand = new Brand();
        simpleJsonProductBuilder.setBrand(brand);

        ProductType type = new ProductType();
        type.setId(2L);
        when(productTypeRepository.findById(2L)).thenReturn(Optional.of(type));

        ProductDetails details = new ProductDetails("Jean", "Desc", java.math.BigDecimal.valueOf(200.0), true, 4.0,
                LocalDateTime.now());

        simpleJsonProductBuilder.putProductDetails(details);
        simpleJsonProductBuilder.putOfficialLink("ignore");
        simpleJsonProductBuilder.putProductType(2L);
        simpleJsonProductBuilder.putEnums(GeneralFit.OVERSIZED, Gender.MALE, Color.BLUE);

        Product result = simpleJsonProductBuilder.getResult();

        assertNotNull(result);
        assertEquals("Jean", result.getName());
        assertEquals(brand, result.getBrand());
        assertEquals(type, result.getProductType());
    }
}
