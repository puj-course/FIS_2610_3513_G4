package com.ceiba.fashtoll.worldModel.product;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import java.math.BigDecimal;
import java.util.List;
import com.ceiba.fashtoll.utilities.enums.*;
import com.ceiba.fashtoll.worldModel.product.Builder.ProductBuilder;
import com.ceiba.fashtoll.worldModel.product.Builder.ProductDetails;
import com.ceiba.fashtoll.worldModel.product.Builder.ProductDirector;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductC_U_Request;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias del patron builder a la clase Product Director")
class ProductDirectorTest {

    @Mock
    private ProductBuilder productBuilder;

    @InjectMocks
    private ProductDirector productDirector;

    @Test
    @DisplayName("ProductDirector: makeCompleteProduct orquesta la secuencia completa del Builder")
    void makeCompleteProduct_executesCorrectSequence() {
        ProductC_U_Request request = new ProductC_U_Request();
        request.setAvailable(true);
        request.setName("Chaqueta Impermeable");
        request.setDescription("Ideal para climas fríos");
        request.setPrice(BigDecimal.valueOf(250000));
        request.setBrandId(5L);
        request.setLinkProduct("https://url-de-prueba.com");
        request.setProductTypeId(2L);
        request.setGeneralFit(GeneralFit.SLIM);
        request.setGender(Gender.MALE);
        request.setColor(Color.BLACK);
        request.setImageUrls(List.of("url1", "url2"));
        request.setTags(List.of("WINTER", "SPORTS"));

        productDirector.makeCompleteProduct(request);

        verify(productBuilder, times(1)).reset();
        verify(productBuilder, times(1)).associateBrand(eq(5L), eq(true));
        verify(productBuilder, times(1)).putOfficialLink(eq("https://url-de-prueba.com"));
        verify(productBuilder, times(1)).putProductType(eq(2L));
        verify(productBuilder, times(1)).putEnums(eq(GeneralFit.SLIM), eq(Gender.MALE), eq(Color.BLACK));
        verify(productBuilder, times(1)).putImagesURLs(anyList());
        verify(productBuilder, times(1)).putTags(anyList());
        verify(productBuilder, times(1)).putProductDetails(any(ProductDetails.class));
    }

    @Test
    @DisplayName("ProductDirector: adminUpdateCompleteProduct ejecuta la secuencia de actualización con ID de producto")
    void adminUpdateCompleteProduct_executesCorrectSequence() {
        Long testProductId = 100L;

        ProductC_U_Request request = new ProductC_U_Request();
        request.setAvailable(true);
        request.setName("Gorra Edición Especial");
        request.setDescription("Diseño exclusivo para coleccionistas");
        request.setPrice(BigDecimal.valueOf(120000));
        request.setBrandId(18L);
        request.setLinkProduct("https://url-de-prueba.com/gorra");
        request.setProductTypeId(1L);
        request.setGeneralFit(GeneralFit.COMPRESSION);
        request.setGender(Gender.UNISEX);
        request.setColor(Color.BLACK);
        request.setImageUrls(List.of("img1.png"));
        request.setTags(List.of("PREMIUM"));

        productDirector.adminUpdateCompleteProduct(testProductId, request);

        verify(productBuilder, times(1)).reset();
        verify(productBuilder, times(1)).updateProductID(eq(testProductId));
        verify(productBuilder, times(1)).putProductDetails(any(ProductDetails.class));
        verify(productBuilder, times(1)).putEnums(eq(GeneralFit.COMPRESSION), eq(Gender.UNISEX), eq(Color.BLACK));
        verify(productBuilder, times(1)).putOfficialLink(eq("https://url-de-prueba.com/gorra"));
        verify(productBuilder, times(1)).putProductType(eq(1L));
        verify(productBuilder, times(1)).associateBrand(eq(18L), eq(true));
        verify(productBuilder, times(1)).putImagesURLs(anyList());
        verify(productBuilder, times(1)).putTags(anyList());
    }

    @Test
    @DisplayName("ProductDirector: updateCompleteProduct ejecuta la secuencia con IDs independientes de marca y producto")
    void updateCompleteProduct_executesCorrectSequence() {
        Long testBrandId = 44L;
        Long testProductId = 200L;

        ProductC_U_Request request = new ProductC_U_Request();
        request.setAvailable(false);
        request.setName("Zapatillas Running");
        request.setDescription("Amortiguación premium para asfalto");
        request.setPrice(BigDecimal.valueOf(550000));
        request.setLinkProduct("https://url-de-prueba.com/zapatillas");
        request.setProductTypeId(3L);
        request.setGeneralFit(GeneralFit.RELAXED);
        request.setGender(Gender.UNISEX);
        request.setColor(Color.WHITE);
        request.setImageUrls(List.of("zapato.png"));
        request.setTags(List.of("RUNNING", "PRO"));

        productDirector.updateCompleteProduct(testBrandId, testProductId, request);

        verify(productBuilder, times(1)).reset();
        verify(productBuilder, times(1)).updateProductID(eq(testProductId));
        verify(productBuilder, times(1)).associateBrand(eq(testBrandId), eq(true));
        verify(productBuilder, times(1)).putProductDetails(any(ProductDetails.class));
        verify(productBuilder, times(1)).putEnums(eq(GeneralFit.RELAXED), eq(Gender.UNISEX), eq(Color.WHITE));
        verify(productBuilder, times(1)).putOfficialLink(eq("https://url-de-prueba.com/zapatillas"));
        verify(productBuilder, times(1)).putProductType(eq(3L));
        verify(productBuilder, times(1)).putImagesURLs(anyList());
        verify(productBuilder, times(1)).putTags(anyList());
    }
}