package fpt.example.db_protect.models_h2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface H2SessionsRepository extends JpaRepository<H2Sessions, Long> {
    H2Sessions findBySessionId(String sessionId);
    List<H2Sessions> findByStatus(String status);
}
