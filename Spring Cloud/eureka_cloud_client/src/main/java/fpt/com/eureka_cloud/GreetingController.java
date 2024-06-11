package fpt.com.eureka_cloud;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.info.Info;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public interface GreetingController {
    @RequestMapping("/greeting")
    String greeting();

    @GetMapping("/actuator/info")
    Info info();
}