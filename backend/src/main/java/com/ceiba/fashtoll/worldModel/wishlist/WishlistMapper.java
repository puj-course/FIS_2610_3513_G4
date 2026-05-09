package com.ceiba.fashtoll.worldModel.wishlist;

import com.ceiba.fashtoll.worldModel.product.dtos.ProductResponse;
import com.ceiba.fashtoll.worldModel.product.mappers.ProductMapper;
import com.ceiba.fashtoll.worldModel.wishlist.dtos.WishlistDetailsResponse;
import com.ceiba.fashtoll.worldModel.wishlist.dtos.WishlistResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WishlistMapper {

    private final ProductMapper productMapper;

    public WishlistResponse toResponse(Wishlist wishlist) {
        if (wishlist == null) return null;
        return new WishlistResponse(wishlist.getId(), wishlist.getName());
    }

    public WishlistDetailsResponse toDetailsResponse(Wishlist wishlist) {
        if (wishlist == null) return null;
        
        List<ProductResponse> products = wishlist.getProducts().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
                
        return new WishlistDetailsResponse(wishlist.getId(), wishlist.getName(), products);
    }
}
