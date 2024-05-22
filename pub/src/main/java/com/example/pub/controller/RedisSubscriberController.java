package com.example.pub.controller;

import com.example.pub.configuration.RedisMessagePublisher;
import com.example.pub.configuration.RedisMessageSubscriber;
import com.example.pub.models.ProductRespository;
import com.example.pub.models.SessionRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/redis/sub")
public class RedisSubscriberController {
    private static final Logger logger = LoggerFactory.getLogger(RedisSubscriberController.class);

    @Autowired
    private RedisMessagePublisher messagePublisher;

    @Autowired
    private ProductRespository productRespository;

    @Autowired
    private SessionRespository sessionRespository;

    @GetMapping("/subscriber")
    public List<String> getMessage() {
        return RedisMessageSubscriber.messageList;
    }
}
