package com.ceiba.fashtoll.worldModel.product.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDetails(
        String name,
        String description,
        BigDecimal price,
        Boolean available,
        Double rating,
        LocalDateTime createdAt
) {}
