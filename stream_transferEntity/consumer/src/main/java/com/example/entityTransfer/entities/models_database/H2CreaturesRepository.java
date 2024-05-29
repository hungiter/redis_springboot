package com.example.entityTransfer.entities.models_database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface H2CreaturesRepository extends CrudRepository<H2Creatures, String> {
}
