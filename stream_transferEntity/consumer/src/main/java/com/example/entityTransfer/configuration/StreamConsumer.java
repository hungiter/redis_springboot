package com.example.entityTransfer.configuration;

import com.example.entityTransfer.entities.dtos.CreaturesDTO;
import com.example.entityTransfer.entities.models.Creatures;
import com.example.entityTransfer.entities.models_database.H2Creatures;
import com.example.entityTransfer.entities.models_database.H2CreaturesRepository;
import com.example.entityTransfer.entities.models_database.H2Tribes;
import com.example.entityTransfer.entities.models_database.H2TribesRepository;
import com.example.entityTransfer.entities.utils.EnumUtil;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class StreamConsumer implements StreamListener<String, ObjectRecord<String, Creatures>> {
    private static final Logger log = LoggerFactory.getLogger(StreamConsumer.class);
    private final AtomicInteger getValues = new AtomicInteger(0);
    private final AtomicInteger addValues = new AtomicInteger(0);
    ObjectRecord<String, Creatures> record; // check record exist

    // Reactive RedisTemplate
    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    // Environment's Values
    @Value("${redis.local_entity:local_entity}")
    String localStream;

    // Read all in stream listener
    @Override
    public void onMessage(ObjectRecord<String, Creatures> record) {
        Creatures creatures = record.getValue();
        this.record = record;
        System.out.printf("%n%s%n", creatures.creatureInfo().toString());
        // Received Data (record <- Each Value in Stream
        if (!isExisted()) {
            localUpdate(creatures);
            databaseUpdate(creatures);
            addValues.incrementAndGet();
        }

        getValues.incrementAndGet();
        System.out.printf("%nRecord's info -----%n  Old record(s): %s%n  New record(s): %s%nTotal record(s): %s%n", oldRecords(), newRecords(), totalRecords());
    }

    // Show Result
    @Scheduled(fixedRate = 10000, initialDelay = 10000)
    public void scheduledRoutine() {
        // System.out.println("\n-----------START ROUTINE-----------");
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
    private void localUpdate(Creatures creatures) {
        ObjectRecord<String, Creatures> localRecord = StreamRecords.newRecord().ofObject(creatures).withStreamKey(localStream).withId(record.getId());
        this.redisTemplate.opsForStream().add(localRecord).subscribe();
    }

    // Database's method
    @Autowired
    H2CreaturesRepository h2CreaturesRepository;

    @Autowired
    H2TribesRepository h2TribesRepository;

    private void databaseUpdate(Creatures creature) {
        CreaturesDTO dataDTO = creature.creatureInfo();
        addCreatures(dataDTO);
        updateTribesInfo(dataDTO);
    }

    private void addCreatures(CreaturesDTO creaturesDTO) {
        // Add new creatures
        H2Creatures creature = new H2Creatures(creaturesDTO.getType(), creaturesDTO.getColor(), creaturesDTO.getGender(), creaturesDTO.getSkills());
        h2CreaturesRepository.save(creature);
    }

    // Primary values: Atomic recommend use as environment's value
    AtomicLong tribesId = new AtomicLong(0);

    private void updateTribesInfo(CreaturesDTO dataDTO) {
        String creatureType = EnumUtil.convertEnumName(dataDTO.getColor()) + " " + EnumUtil.convertEnumName(dataDTO.getType());
        h2TribesRepository.findAll().forEach(h2Tribe -> {
            if (h2Tribe.getCreatureType().equals(creatureType)) {
                tribesId.set(h2Tribe.getId());
            }
        });

        if (tribesId.get() == 0) {
            H2Tribes saveTribe = new H2Tribes(creatureType, (dataDTO.getGender() ? 1 : 0), (dataDTO.getGender() ? 0 : 1));
            saveTribe.setTp(dataDTO.totalPower());
            h2TribesRepository.save(saveTribe);
        } else {
            if (h2TribesRepository.findById(String.valueOf(tribesId.get())).isPresent()) {
                H2Tribes currTribe = h2TribesRepository.findById(String.valueOf(tribesId.get())).get();
                currTribe.setMale(currTribe.getMale() + (dataDTO.getGender() ? 1 : 0));
                currTribe.setFemale(currTribe.getFemale() + (dataDTO.getGender() ? 0 : 1));
                currTribe.setTp(currTribe.getTp() + dataDTO.totalPower());
                h2TribesRepository.save(currTribe);
            }
        }
        tribesId.set(0);
    }

    // Functional method
    private boolean isExisted() {
        AtomicBoolean check = new AtomicBoolean(false);
        // Read all records from the Redis stream starting from the beginning
        Flux<ObjectRecord<String, Creatures>> records = this.redisTemplate.opsForStream().read(Creatures.class, StreamOffset.fromStart(localStream));

        // Filter the records to find if any record contains the specified value
        var bol = records.filter(record -> (this.record.getValue().equals(record.getValue()) & this.record.getId().equals(record.getId()))).hasElements().block(); // Block to get the result synchronously
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
