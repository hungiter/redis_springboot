package com.example.stream.configuration.consumer;

import com.example.stream.models.Product;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StreamConsumer implements StreamListener<String, ObjectRecord<String, Product>> {
    private static final Logger log = LoggerFactory.getLogger(StreamConsumer.class);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);


    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    @SneakyThrows
    public void onMessage(ObjectRecord<String, Product> record) {
        System.out.printf("comsumed-%s%n", record.getValue());
        this.redisTemplate
                .opsForZSet()
                .incrementScore("revenue", record.getValue().getCategory().toString(), record.getValue().getPrice())
                .subscribe();
        atomicInteger.incrementAndGet();
    }

    @Scheduled(fixedRate = 10000)
    public void showPublishedEventsSoFar() {
        System.out.println(
                "Total Consumed :: " + atomicInteger.get()
        );
    }
}
