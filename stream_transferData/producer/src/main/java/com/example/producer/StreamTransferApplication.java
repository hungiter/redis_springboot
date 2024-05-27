package com.example.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StreamTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamTransferApplication.class, args);
	}

}
