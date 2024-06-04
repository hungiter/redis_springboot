package fpt.example.db_protect.configuration;

import fpt.example.db_protect.models.Sessions;
// Library
import fpt.example.db_protect.models_h2.H2Sessions;
import fpt.example.db_protect.models_h2.H2SessionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class StreamConsumer implements StreamListener<String, ObjectRecord<String, Sessions>> {
    private static final Logger log = LoggerFactory.getLogger(StreamConsumer.class);
    private final AtomicInteger getValues = new AtomicInteger(0);
    private final AtomicInteger addValues = new AtomicInteger(0);
    ObjectRecord<String, Sessions> record; // check record exist

    // Reactive RedisTemplate
    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    // Environment's Values
    @Value("${redis.local_entity:local_entity}")
    String localStream;


    // Read all in stream listener
    @Override
    public void onMessage(ObjectRecord<String, Sessions> record) {
        Sessions session = record.getValue();
        this.record = record;
//        System.out.printf("%n%s%n", session.sessionInfo());

        if (!sessionExisted()) {
            localUpdate();
            addSession(session);
            addValues.incrementAndGet();
        }

        getValues.incrementAndGet();
        System.out.printf("%nRecord's info -----%n  Old record(s): %s%n  New record(s): %s%nTotal record(s): %s%n", oldRecords(), newRecords(), totalRecords());
    }

    // Show Result
    @Scheduled(fixedRate = 10000, initialDelay = 10000)
    public void scheduledRoutine() {
        createStreamGroup();
    }

    // Check -> Create if missing stream
    private void createStreamGroup() {
        try {
            this.redisTemplate.opsForStream().createGroup(localStream, localStream);
            // System.out.println("------ Stream's group create ------");
        } catch (RedisSystemException e) {
            var cause = e.getCause();
            if (cause != null) {
                log.info("Stream's group create failed - redis group existed");
            }
        }
    }

    // Stream's method
    private void localUpdate() {
        ObjectRecord<String, Sessions> sessionRecord = StreamRecords.newRecord().ofObject(this.record.getValue()).withStreamKey(localStream).withId(record.getId());
        this.redisTemplate.opsForStream().add(sessionRecord).subscribe();
    }

    // Database's method
    @Autowired
    H2SessionsRepository h2SessionsRepository;

    private void addSession(Sessions sessions) {
        // Add new Sessions
        H2Sessions session = new H2Sessions(sessions.getSessionId(), sessions.getContract(),sessions.getStatus());
        session.initTime();
        h2SessionsRepository.save(session);
        System.out.println(session);
    }

    // Primary values: Atomic recommend use as environment's value

    // Functional method
    private boolean sessionExisted() {
        AtomicBoolean check = new AtomicBoolean(false);
        // Read all records from the Redis stream starting from the beginning
        Flux<ObjectRecord<String, Sessions>> records = this.redisTemplate.opsForStream().read(Sessions.class, StreamOffset.fromStart(localStream));
        // Filter the records to find if any record contains the specified value
        var bol = records.filter(record -> (this.record.getValue().equals(record.getValue()))).hasElements().block(); // Block to get the result synchronously
        check.set(Boolean.TRUE.equals(bol));
        return check.get();
    }

    private int maxLength() {
        return Math.max(String.valueOf(getValues.get()).length(), String.valueOf(addValues.get()).length());
    }

    private String totalRecords() {
        return formattedNumber(getValues.intValue());
    }

    private String oldRecords() {
        return formattedNumber(getValues.intValue() - addValues.intValue());
    }

    private String newRecords() {
        return formattedNumber(addValues.intValue());
    }

    private String formattedNumber(int value) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumIntegerDigits(maxLength());
        decimalFormat.setGroupingUsed(false);
        return decimalFormat.format(value);
    }
}
