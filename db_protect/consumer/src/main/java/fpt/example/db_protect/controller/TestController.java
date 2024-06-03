package fpt.example.db_protect.controller;

import fpt.example.db_protect.models_h2.H2Sessions;
import fpt.example.db_protect.services.SessionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriber")
public class TestController {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    SessionsService sessionsService;

    @GetMapping("/get/{id}")
    public H2Sessions getById(@PathVariable String id) {
        return sessionsService.getSessionById(Long.parseLong(id));
    }
}
