package com.example.sub.controller;


import com.example.sub.configuration.RedisMessageSubscriber;
import com.example.sub.models.Message;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/redis/sub")
public class RedisController {
    private static Logger logger = LoggerFactory.getLogger(RestController.class);


    @GetMapping("/subscriber")
    public List<String> getMessage() {
        return RedisMessageSubscriber.messageList;
    }
}
