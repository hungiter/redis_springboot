package jpa.learning.neo4j.repository;

import jpa.learning.neo4j.entity.Phone;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneRepository extends Neo4jRepository<Phone,String> {
    @Query("MATCH (p.Phone) RETURN n")
    List<Phone> getAll();

    @Query("MATCH (p:Phone) WHERE p.system = $system RETURN p")
    List<Phone> listByOS(String system);

    @Query("Match (p:Phone) WHERE p.price >= $minPrice AND p.price <= $maxPrice RETURN p SKIP $skip LIMIT 10")
    List<Phone> listPrice(int minPrice, int maxPrice,int skip);
}
