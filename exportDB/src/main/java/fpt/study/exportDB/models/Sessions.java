package fpt.study.exportDB.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Sessions")
public class Sessions {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "s_id")
    String sessionId;
    @Column(name = "contract")
    String contract;
    @Column(name = "status")
    String status;

    public Sessions(String sessionId, String contract) {
        this.sessionId = sessionId;
        this.contract = contract;
        this.status = "OPEN";
    }

    public Sessions(String sessionId, String contract, String status) {
        this.sessionId = sessionId;
        this.contract = contract;
        this.status = status;
    }
}
