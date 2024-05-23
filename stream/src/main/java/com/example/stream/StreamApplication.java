package com.example.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class StreamApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StreamApplication.class);
        app.run(args);
    }
}
