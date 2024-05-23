package com.example.stream_review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StreamReviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamReviewApplication.class, args);
	}

}
