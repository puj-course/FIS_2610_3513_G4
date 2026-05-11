package com.ceiba.fashtoll.worldModel.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByClientId(Long clientId);
    Optional<Wishlist> findByIdAndClientId(Long id, Long clientId);
    Optional<Wishlist> findFirstByClientIdOrderByIdAsc(Long clientId);
}
