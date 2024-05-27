package com.example.producer.configuration.Producer;

import com.example.producer.models.Data.Transfer.TransferInfo;
import com.example.producer.models.Data.Transfer.TransferInfoDTO;
import com.example.producer.models.Data.Transfer.TransferRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StreamProducer implements StreamEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(StreamProducer.class);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final List<ObjectRecord<String, String>> records = new ArrayList<>();

    @Value("${redis.app_stream}")
    String streamKey;

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    @Scheduled(fixedRateString = "${redis.publish_rate}")
    public void transferData() {
        // Prepair for send
        TransferInfo transferInfo = transferRepository.generateTransfer();
        TransferInfoDTO transferInfoDTO = transferInfo.getTransferInfoDTO();

        String dataSend = transferInfoDTO.toJson();
        // Create stream's record
        ObjectRecord<String, String> record = StreamRecords.newRecord()
                .ofObject(dataSend)
                .withStreamKey(streamKey);

        records.add(record);

        this.redisTemplate
                .opsForStream()
                .add(record)
                .subscribe(System.out::println);
        atomicInteger.incrementAndGet();
    }


    @Scheduled(fixedRateString = "${redis.check_rate}")
    public void showResultUntilNow() {
        System.out.println("Total transfers: " + atomicInteger);
    }

    @Scheduled(fixedRateString = "${redis.backup_rate}")
    private void data_backup() {
        createStreamGroup();
        if (!atomicInteger.toString().equals("0")) {
            records.forEach(record -> {
                this.redisTemplate
                        .opsForStream()
                        .add(record)
                        .subscribe(System.out::println);
            });
        }
    }

    private void createStreamGroup() {
        System.out.println("Check redis stream existed");
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
