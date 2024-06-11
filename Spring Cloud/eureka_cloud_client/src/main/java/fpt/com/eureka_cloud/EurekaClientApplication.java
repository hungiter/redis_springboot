package fpt.com.eureka_cloud;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class EurekaClientApplication implements GreetingController {
    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String appName;

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    @Override
    public String greeting() {
        return String.format("Hello from '%s'!", eurekaClient.getApplication(appName).getName());
    }

    @Override
    public Info info() {
        Info.Builder builder = new Info.Builder();
        Map<String, Object> details = new HashMap<String, Object>() {{
            put("Application's name", "INF's Eureka");
            put("Description", "Eureka's Client service application");
            put("Version", "1.0.0");
			put("Author's name", "Zion Nguyễn");
			put("Build's time", LocalDate.now().toString());
            // Add more key-value pairs as needed
        }};
        return builder.withDetails(details).build();
    }
}
