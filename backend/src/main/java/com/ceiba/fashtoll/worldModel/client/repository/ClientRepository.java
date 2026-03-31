package com.ceiba.fashtoll.worldModel.client.repository;

import com.ceiba.fashtoll.worldModel.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
