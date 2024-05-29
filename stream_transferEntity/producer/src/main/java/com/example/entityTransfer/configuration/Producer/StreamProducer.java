package com.example.entityTransfer.configuration.Producer;

import com.example.entityTransfer.entities.models.Creatures;
import com.example.entityTransfer.entities.models.CreaturesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StreamProducer implements StreamEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(StreamProducer.class);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Value("${redis.entity_home}")
    String streamKey;

    @Autowired
    CreaturesRepository repository;

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    @Scheduled(fixedRate = 10000)
    public void transferData() {
        Creatures creatures = repository.getRandomCreatures();
        var record = StreamRecords.newRecord().ofObject(creatures).withStreamKey(streamKey);
        System.out.println((atomicInteger.get() + 1) + "." + creatures.creatureInfo().toString());
        this.redisTemplate.opsForStream().add(record).subscribe();
        atomicInteger.incrementAndGet();
    }

    @Scheduled(fixedRate = 50000, initialDelay = 50000)
    public void showResultUntilNow() {
        System.out.println("Total transfers: " + atomicInteger);
    }

    private void createStreamGroup() {
        try {
            this.redisTemplate.opsForStream().createGroup(streamKey, streamKey);
        } catch (RedisSystemException e) {
            var cause = e.getCause();
            if (cause != null) {
                log.info("Stream - redis group existed, skip group creation [{}]->[{}]", streamKey, streamKey);
            }
        }
    }
}
