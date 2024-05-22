package com.example.pub.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

// Fixed value = 60s
@RedisHash(timeToLive = 60L)
//@RedisHash
public class Session {
    @Id
    private String id;
    @TimeToLive
    private Long expirationInSeconds;

    public Session(String id, Long expirationInSeconds) {
        this.id = id;
        this.expirationInSeconds = expirationInSeconds;
    }

    public Session() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getExpirationInSeconds() {
        return expirationInSeconds;
    }

    public void setExpirationInSeconds(Long expirationInSeconds) {
        this.expirationInSeconds = expirationInSeconds;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", expirationInSeconds=" + expirationInSeconds +
                '}';
    }
}
