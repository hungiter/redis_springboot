package com.example.stream_review.configuration.Producer;

import com.example.stream_review.models.GamePayment;
import com.example.stream_review.models.GamePaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StreamProducer implements StreamEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(StreamProducer.class);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Value("${redis.app_stream}")
    String streamKey;

    @Autowired
    GamePaymentRepository paymentRepository;

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    @Scheduled(fixedRateString = "${redis.publish_rate}")
    public void paymentGenerate() {
        GamePayment gamePayment = paymentRepository.generateRandomPayment();
        ObjectRecord<String, GamePayment> record = StreamRecords.newRecord()
                .ofObject(gamePayment)
                .withStreamKey(streamKey);
        this.redisTemplate
                .opsForStream()
                .add(record)
                .subscribe(System.out::println);
        atomicInteger.incrementAndGet();
    }

    @Scheduled(fixedRateString = "${redis.check_rate}")
    public void showResultUntilNow() {
        log.info("Total payments: {}", atomicInteger);
    }
}
