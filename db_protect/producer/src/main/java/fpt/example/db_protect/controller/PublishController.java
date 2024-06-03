package fpt.example.db_protect.controller;

import fpt.example.db_protect.configuration.StreamProducer;
import fpt.example.db_protect.models.Sessions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publish")
public class PublishController {
    @Autowired
    StreamProducer streamProducer;

    @GetMapping("/add")
    public String addSession() {
        Sessions sessions = streamProducer.transferSession();
        return sessions.toString();
    }
}
