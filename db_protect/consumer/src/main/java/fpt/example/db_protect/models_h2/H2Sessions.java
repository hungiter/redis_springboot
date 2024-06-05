package fpt.example.db_protect.models_h2;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Sessions")
@Data
@Getter
@Setter
@NoArgsConstructor
public class H2Sessions implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sessionId", nullable = false)
    private String sessionId;

    @Column(name = "contract", nullable = false)
    private String contract;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "createAt", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    public H2Sessions(String sessionId, String contract, String status) {
        this.sessionId = sessionId;
        this.status = status;
        this.contract = contract;
    }


	// Self-defined
    public void initTime() {
        setCreateAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }

}
