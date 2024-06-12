package fpt.com.eureka_cloud.controller;

import org.springframework.boot.actuate.info.Info;
import org.springframework.web.bind.annotation.GetMapping;

public interface InfoController {
    @GetMapping("/actuator/info")
    Info info();
}
