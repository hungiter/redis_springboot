package fpt.example.db_protect.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("sessions")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Sessions {
    String sessionId;
    String contract;
    String status;
}
