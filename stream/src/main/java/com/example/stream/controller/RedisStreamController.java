package com.example.stream.controller;


import com.example.stream.configuration.RedisMessagePublisher;
import com.example.stream.configuration.RedisMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stream")
public class RedisStreamController {
    private static final Logger log = LoggerFactory.getLogger(RedisStreamController.class);
    @Autowired
    RedisMessagePublisher messagePublisher;

    @GetMapping("/flow")
    public String showStreamFlow() {
        int size = RedisMessageSubscriber.messageList.size();
        StringBuilder result = new StringBuilder();
        int curr = 0;
        while (curr < size) {
            result.append(RedisMessageSubscriber.messageList.get(curr)).append("\n");
            curr++;
        }
        return result.toString();
    }

    @PostMapping("/producer")
    public void intStreamProduct() {
        messagePublisher.publish("[Add Producer's list]");
    }

    @PostMapping("/producer/add")
    public void addStreamProduct() {
        messagePublisher.publish("[Add Producer]");
    }

    @PostMapping("/consumer/add")
    public void addStreamConsumer() {
        messagePublisher.publish("[Add Consumer]");
    }
}
