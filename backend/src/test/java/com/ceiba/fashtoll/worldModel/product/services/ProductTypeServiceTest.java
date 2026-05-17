package com.ceiba.fashtoll.worldModel.product.services;

import com.ceiba.fashtoll.worldModel.product.dtos.ProductTypeRequest;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductTypeResponse;
import com.ceiba.fashtoll.worldModel.product.entities.ProductType;
import com.ceiba.fashtoll.worldModel.product.mappers.ProductTypeMapper;
import com.ceiba.fashtoll.worldModel.product.repositories.ProductTypeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de ProductTypeService")
class ProductTypeServiceTest {

    @Mock
    private ProductTypeRepository productTypeRepository;

    @Mock
    private ProductTypeMapper productTypeMapper;

    @InjectMocks
    private ProductTypeService productTypeService;

    @Test
    @DisplayName("CP-PT-01: getAllProductTypes - Retorna lista")
    void getAllProductTypes_returnsList() {
        ProductType pt = new ProductType();
        pt.setId(1L);
        when(productTypeRepository.findAll()).thenReturn(Collections.singletonList(pt));
        when(productTypeMapper.toResponse(any())).thenReturn(new ProductTypeResponse());

        List<ProductTypeResponse> result = productTypeService.getAllProductTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("CP-PT-02: getProductTypeById - Retorna producto")
    void getProductTypeById_returnsProductType() {
        ProductType pt = new ProductType();
        pt.setId(1L);
        when(productTypeRepository.findById(1L)).thenReturn(Optional.of(pt));
        when(productTypeMapper.toResponse(any())).thenReturn(new ProductTypeResponse());

        ProductTypeResponse result = productTypeService.getProductTypeById(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("CP-PT-03: createProductType - Crea producto")
    void createProductType_createsProductType() {
        ProductTypeRequest req = new ProductTypeRequest();
        ProductType pt = new ProductType();
        pt.setId(1L);
        when(productTypeMapper.toEntity(any())).thenReturn(pt);
        when(productTypeRepository.save(any())).thenReturn(pt);
        when(productTypeMapper.toResponse(any())).thenReturn(new ProductTypeResponse());

        ProductTypeResponse result = productTypeService.createProductType(req);

        assertNotNull(result);
        verify(productTypeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("CP-PT-04: updateProductType - Actualiza producto")
    void updateProductType_updatesProductType() {
        ProductTypeRequest req = new ProductTypeRequest();
        ProductType pt = new ProductType();
        pt.setId(1L);
        when(productTypeRepository.findById(1L)).thenReturn(Optional.of(pt));
        when(productTypeMapper.updateEntity(any(), any())).thenReturn(true);
        when(productTypeRepository.save(any())).thenReturn(pt);
        when(productTypeMapper.toResponse(any())).thenReturn(new ProductTypeResponse());

        ProductTypeResponse result = productTypeService.updateProductType(1L, req);

        assertNotNull(result);
        verify(productTypeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("CP-PT-05: deleteProductType - Elimina producto")
    void deleteProductType_deletesProductType() {
        ProductType pt = new ProductType();
        pt.setId(1L);
        when(productTypeRepository.findById(1L)).thenReturn(Optional.of(pt));

        productTypeService.deleteProductType(1L);

        verify(productTypeRepository, times(1)).delete(pt);
    }

    @Test
    @DisplayName("CP-PT-06: injectProductTypeFromJson - Inyecta desde JSON")
    void injectProductTypeFromJson_savesMultiple() {
        ProductTypeRequest req = new ProductTypeRequest();
        List<ProductTypeRequest> reqs = Collections.singletonList(req);
        when(productTypeMapper.toEntity(any())).thenReturn(new ProductType());

        productTypeService.injectProductTypeFromJson(reqs);

        verify(productTypeRepository, times(1)).save(any());
    }
}
