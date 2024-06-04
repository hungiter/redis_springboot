package fpt.example.db_protect.models_h2;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Sessions")
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

    public H2Sessions() {
    }

    public H2Sessions(String sessionId, String contract, String status) {
        this.sessionId = sessionId;
        this.status = status;
        this.contract = contract;
    }

    public Long getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }



    public void initTime() {
        setCreateAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }

}
