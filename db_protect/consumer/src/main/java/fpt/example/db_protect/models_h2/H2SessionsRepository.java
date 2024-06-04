package fpt.example.db_protect.models_h2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface H2SessionsRepository extends JpaRepository<H2Sessions, Long> {
    H2Sessions findBySessionId(String sessionId);

    List<H2Sessions> findByStatus(String status);

    //    @Query(value = "SELECT * FROM Sessions s WHERE s.create_at <= :currentTime", nativeQuery = true)
    @Query(value = "SELECT * FROM Sessions s WHERE s.create_at >= DATEADD('MINUTE', -2, CURRENT_TIMESTAMP) AND s.create_at <= CURRENT_TIMESTAMP", nativeQuery = true)
    List<H2Sessions> findEntitiesWithTimeLessThan2Minutes();
}