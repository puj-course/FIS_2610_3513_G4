package com.ceiba.fashtoll.worldModel.tag.repository;

import com.ceiba.fashtoll.worldModel.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
