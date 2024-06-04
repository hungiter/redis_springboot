package fpt.example.db_protect.controller;

import fpt.example.db_protect.services.SessionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriber")
public class TestController {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    SessionsService sessionsService;

    //    X-Required-Header: FPT-INF
    @GetMapping("/get/{id}")
    public String getById(@PathVariable String id) {
        return sessionsService.getSessionById(Long.parseLong(id));
    }

    @PostMapping("/get")
    public String getListOf2Minutes() {
        StringBuilder result = new StringBuilder();
        List<String> list = sessionsService.get2MinutesSession();
        list.forEach(result::append);
        return result.toString();
    }
}
