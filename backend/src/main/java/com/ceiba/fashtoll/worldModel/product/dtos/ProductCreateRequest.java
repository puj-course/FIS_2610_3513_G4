package com.ceiba.fashtoll.worldModel.product.dtos;

import com.ceiba.fashtoll.utilities.enums.Color;
import com.ceiba.fashtoll.utilities.enums.Gender;
import com.ceiba.fashtoll.utilities.enums.GeneralFit;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

    private Long brandId;

    @NotNull(message = "El ID del tipo de producto es obligatorio")
    private Long productTypeId;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder los 150 caracteres")
    private String name;

    private String description;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal price;

    private GeneralFit generalFit;
    private Gender gender;
    private Color color;
    private Boolean available = true;

    @Size(max = 500)
    private String linkProduct;

    private List<String> imageUrls;
    private List<Long> tagIds;
}
