package jpa.learning.neo4j.controller;

import jpa.learning.neo4j.entity.Phone;
import jpa.learning.neo4j.entity.Query;
import jpa.learning.neo4j.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phone")
public class PhoneController {
    @Autowired
    PhoneService phoneService;

    @GetMapping("/create")
    public String createPhone() {
        return phoneService.createPhone().toString();
    }

    @GetMapping("/get")
    public List<Phone> getAll() {
        return phoneService.getAll();
    }

    @GetMapping("/get/ios")
    public List<Phone> listIOS() {
        return phoneService.listByOS("ios");
    }

    @GetMapping("/get/kaios")
    public List<Phone> listKaiOS() {
        return phoneService.listByOS("kaios");
    }

    @GetMapping("/get/android")
    public List<Phone> listAndroid() {
        return phoneService.listByOS("android");
    }

    @GetMapping("/get/os/{system}")
    public List<Phone> getbyOS(@PathVariable String system) {
        return phoneService.listByOS(system);
    }

    @PostMapping("/get/price")
    public List<Phone> getByPrice(@RequestBody Query query) {
        int n1 = query.getMinPrice();
        int n2 = query.getMaxPrice();
        int page = query.getViewPage();
        try{
            return phoneService.listByPrice(n1, n2, page);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch phones by price range");
        }
    }
}
