package fpt.study.exportDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableRedisRepositories
public class ExportDbApplication {
    //http://localhost:8080/h2-console
    public static void main(String[] args) {
        SpringApplication.run(ExportDbApplication.class, args);
    }
}
