package com.ceiba.fashtoll.searchEngine.tag.repository;

import com.ceiba.fashtoll.searchEngine.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
