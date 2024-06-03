package fpt.example.db_protect.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("sessions")
@Data
@NoArgsConstructor
public class Sessions {
    String sessionId;
    String contract;
    String status;

    public Sessions(String sessionId,String contract, String status) {
        this.sessionId = sessionId;
        this.contract = contract;
        this.status = status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getContract() {
        return contract;
    }

    public String getStatus() {
        return status;
    }

    public String sessionInfo() {
        return "Session's Info{" +
                "sessionId='" + sessionId + '\'' +
                ", contract='" + contract + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
