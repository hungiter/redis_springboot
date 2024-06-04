package fpt.example.db_protect.configuration;

import fpt.example.db_protect.models.SessionRepository;
import fpt.example.db_protect.models.Sessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StreamProducer implements StreamEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(StreamProducer.class);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Value("${redis.entity_home}")
    String streamKey;


    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    // Initialize
    private final AtomicInteger sessionId = new AtomicInteger(1);
    ObjectRecord<String, Sessions> record;

    @Override
//    @Scheduled(fixedRate = 10000)
    public void transferData() {
        atomicInteger.incrementAndGet();
    }

    @Lazy
    SessionRepository sessionRepository = new SessionRepository();

    @Override
    public Sessions transferSession() {
        Sessions session = sessionRepository.getSession(sessionId.get());


        this.record = StreamRecords.newRecord().ofObject(session).withStreamKey(streamKey);
        if (!isExisted()) {
            System.out.println((sessionId.get()) + "." + session.sessionInfo());
            this.redisTemplate.opsForStream().add(this.record).subscribe();
            sessionId.incrementAndGet();
            return session;
        }

        return new Sessions("", "", "");
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

    private boolean isExisted() {
        AtomicBoolean check = new AtomicBoolean(false);
        // Read all records from the Redis stream starting from the beginning
        Flux<ObjectRecord<String, Sessions>> records = this.redisTemplate.opsForStream().read(Sessions.class, StreamOffset.fromStart(streamKey));

        // Filter the records to find if any record contains the specified value
        var bol = records.filter(record -> (this.record.getValue().equals(record.getValue()))).hasElements().block(); // Block to get the result synchronously
        check.set(Boolean.TRUE.equals(bol));
        return check.get();
    }

}
