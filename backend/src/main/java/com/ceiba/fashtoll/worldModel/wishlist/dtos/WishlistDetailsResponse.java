package com.ceiba.fashtoll.worldModel.wishlist.dtos;

import com.ceiba.fashtoll.worldModel.product.dtos.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistDetailsResponse {
    private Long id;
    private String name;
    private List<ProductResponse> products;
}
