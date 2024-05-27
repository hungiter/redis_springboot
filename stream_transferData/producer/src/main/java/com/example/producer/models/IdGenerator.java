package com.example.producer.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.Random;

@ToString
@AllArgsConstructor
public class IdGenerator {
    private static final Random random = new Random();

    public String generateId() {
        long timestamp = Instant.now().toEpochMilli();
        String randomString = getRandomString(); // length of random string can be adjusted
        return "RND" + timestamp + randomString;
    }

    private static String getRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }
        return randomString.toString();
    }
}