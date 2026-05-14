package com.ceiba.fashtoll.searchEngine.repositories;

import com.ceiba.fashtoll.searchEngine.indexingComponent.SearchToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchTokenRepository extends JpaRepository<SearchToken, Long> {
    Optional<SearchToken> findByToken(String token);
    @Query("SELECT st.token, COUNT(st) FROM SearchToken st WHERE st.token = :token GROUP BY st.token")
    Pair<String, Long> countByToken(@Param("token") String token);
}
