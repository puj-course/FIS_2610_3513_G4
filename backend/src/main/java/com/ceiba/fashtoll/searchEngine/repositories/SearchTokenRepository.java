package com.ceiba.fashtoll.searchEngine.repositories;

import com.ceiba.fashtoll.searchEngine.indexingComponent.SearchToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchTokenRepository extends JpaRepository<SearchToken, Long> {
    Optional<SearchToken> findByToken(String token);
    Optional<SearchToken> deleteByToken(String token);
}
