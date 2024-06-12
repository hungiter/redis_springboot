package fpt.com.eureka_cloud.controller;

import fpt.com.eureka_cloud.client.MicroServicesA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @Autowired
    private MicroServicesA microServicesA;

    @GetMapping("/greeting")
    public String greeting() {
        return microServicesA.greeting();
    }
}
