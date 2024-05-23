package com.example.stream.configuration.producer;

import com.example.stream.models.Product;
import com.example.stream.models.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StreamProducer implements StreamEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(StreamProducer.class);
    @Value("${redis.stream_purchase}")
    private String purchaseGroup;

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Scheduled(fixedRateString= "${redis.publish_rate}")
    public void publishProduct(){
        Product product = this.productRepository.getRandomProduct();
        ObjectRecord<String, Product> record = StreamRecords.newRecord()
                .ofObject(product)
                .withStreamKey(purchaseGroup);
        this.redisTemplate
                .opsForStream()
                .add(record)
                .subscribe(System.out::println);
        atomicInteger.incrementAndGet();
    }


    @Scheduled(fixedRate = 10000)
    public void showPublishedEventsSoFar() {
        System.out.println(
                "Total Events :: " + atomicInteger.get()
        );
    }

}
