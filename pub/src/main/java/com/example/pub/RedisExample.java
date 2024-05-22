package com.example.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisExample implements CommandLineRunner {
    @Autowired
    private RedisTemplate template;

    @Override
    public void run(String... args) throws Exception {
        // Set giá trị của key "loda" là "hello redis"
//        template.opsForValue().set("example","hello world");

        // In ra màn hình Giá trị của key "loda" trong Redis
        System.out.println("Successful on start application");
    }
}

