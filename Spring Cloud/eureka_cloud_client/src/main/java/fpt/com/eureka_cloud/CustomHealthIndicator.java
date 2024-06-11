package fpt.com.eureka_cloud;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        // Custom health check logic
        boolean isHealthy = checkHealth();
        if (isHealthy) {
            return Health.up().withDetail("CustomService", "Available").build();
        } else {
            return Health.down().withDetail("CustomService", "Not Available").build();
        }
    }

    private boolean checkHealth() {
        // Implement your health check logic here
        return true;
    }
}
