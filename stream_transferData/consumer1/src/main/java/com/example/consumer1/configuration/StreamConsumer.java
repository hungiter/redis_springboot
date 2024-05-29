package com.example.consumer1.configuration;

import com.example.consumer1.helper.StreamDataHelper;
import com.example.consumer1.helper.StreamToDatabase;
import com.example.consumer1.models.Entity.TransferInfo;
import com.example.consumer1.models.Entity.TransferInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class StreamConsumer implements StreamListener<String, ObjectRecord<String, String>> {
    private static final Logger log = LoggerFactory.getLogger(StreamConsumer.class);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    // Session dataHandle
    ObjectRecord<String, String> record;
    private final List<TransferInfo> transferInfos = new ArrayList<>();

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Autowired
    private TransferInfoRepository transferInfoRepository;

    @Value("${redis.local_stream}")
    private String localStream;
    @Value("${redis.app_stream}")
    private String appStream;
    @Value("${transfer.port_name}")
    private String port_name;

    // Read all in stream listener
    @Override
    public void onMessage(ObjectRecord<String, String> record) {
        // Received Data (record <- Each Value in Stream
        this.record = record;
    }

    // Show Result
    @Scheduled(fixedRateString = "${redis.check_rate}",initialDelay = 10000)
    public void scheduledRoutine() {
        System.out.println("\n---------SCHEDULED ROUTINE---------");
        // Check -> Create if missing stream
        createStreamGroup();
        // Data backup from "producer/other consumer"
        streamDataHelper(this.record).addMissingValue();
    }

    public StreamDataHelper streamDataHelper(ObjectRecord<String, String> record) {
        StreamToDatabase streamToDatabase = new StreamToDatabase(transferInfoRepository, transferInfos);
        return new StreamDataHelper(redisTemplate, localStream, appStream, port_name, record, streamToDatabase);
    }

    private void createStreamGroup() {
        System.out.println("Create Stream's Group");
        try {
            this.redisTemplate.opsForStream().createGroup(localStream, localStream);
        } catch (RedisSystemException e) {
            var cause = e.getCause();
            if (cause != null) {
                System.out.println("Stream - redis group existed, skip group creation [" + localStream + "]->[" + localStream + "]");
            }
        }
    }

    // Database
    //    @Scheduled(fixedRateString = "${spring.h2.update_rate}")
    //    public void updateH2Database() {
    //        // Get current values in database - maybe set time from -> to
    //        List<TransferInfo> h2 = new ArrayList<>();
    //        transferInfoRepository.findAll().forEach(h2::add);
    //
    //        // Get missing value - not in database
    //        List<TransferInfo> missing = new ArrayList<>(transferInfos);
    //        missing.removeAll(h2);
    //
    //        // Add missing data into database
    //        if (!atomicInteger.toString().equals("0")) {
    //            missing.forEach(transferInfo -> {
    //                if (!transferInfoRepository.existsById(transferInfo.getId().toString())) {
    //                    transferInfoRepository.save(transferInfo);
    //                }
    //            });
    //        }
    //    }
}
