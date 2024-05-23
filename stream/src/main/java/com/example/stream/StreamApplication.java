package com.example.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@SpringBootApplication
@EnableScheduling
public class StreamApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StreamApplication.class);
        app.run(args);
    }
}
