package com.example.stream_review.configuration;

import com.example.stream_review.models.GamePayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StreamConsumer implements StreamListener<String, ObjectRecord<String, GamePayment>> {
    private static final Logger log = LoggerFactory.getLogger(StreamConsumer.class);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    public void onMessage(ObjectRecord<String, GamePayment> payment) {
        System.out.println(payment.getValue().toString());
        this.redisTemplate
                .opsForZSet()
                .incrementScore("rich-rank", payment.getValue().playerName(), payment.getValue().cardPrice())
                .subscribe();
        this.redisTemplate
                .opsForZSet()
                .incrementScore("popular-game", payment.getValue().gameTitle(), payment.getValue().cardPrice())
                .subscribe();
        this.redisTemplate
                .opsForZSet()
                .incrementScore("supplier-profit", payment.getValue().cardSupplier(), payment.getValue().cardPrice())
                .subscribe();
        atomicInteger.incrementAndGet();
    }

    @Scheduled(fixedRateString = "${redis.check_rate}")
    public void showResultUntilNow() {
        log.info("Total Consumed: {}", atomicInteger);
    }
}
