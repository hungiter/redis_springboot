package fpt.example.db_protect.models_h2;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
    private String createAt;

    @Column(name = "updatedAt", nullable = false)
    private String updatedAt;

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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void initTime() {
        String now = dateTimeFormatter(LocalDateTime.now());
        setCreateAt(now);
        setUpdatedAt(now);
    }

    private String dateTimeFormatter(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
