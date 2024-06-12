package fpt.study.exportDB.models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Sessions,String> {
    @Query("SELECT COUNT(s) FROM Sessions s WHERE s.status = :status")
    long countByStatus(@Param("status") String status);

    @Query("SELECT s FROM Sessions s WHERE s.status = :status")
    Page<Sessions> findByStatus(@Param("status") String status, Pageable pageable);

    @Query("SELECT s FROM Sessions s WHERE s.sessionId  = :sessionId")
    Optional<Sessions> findBySessionID(@Param("sessionId") String session_id);


}
