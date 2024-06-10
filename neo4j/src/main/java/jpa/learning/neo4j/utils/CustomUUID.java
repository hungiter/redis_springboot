package jpa.learning.neo4j.utils;

import org.springframework.data.neo4j.core.schema.IdGenerator;

import java.util.UUID;

public class CustomUUID implements IdGenerator<String> {
    @Override
    public String generateId(String primaryLabel, Object entity) {
        return UUID.randomUUID().toString();
    }
}
