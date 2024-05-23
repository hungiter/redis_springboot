package com.example.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisExample implements CommandLineRunner {
//    @Autowired
//    private RedisTemplate template;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Stream's application started.");
    }
}

