package fpt.com.eureka_cloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "microservicesA")
public interface MicroServicesA {
    @GetMapping("/greeting")
    String greeting();
}