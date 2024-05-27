package com.example.consumer1.configuration;

import com.example.consumer1.models.Entity.TransferInfo;
import com.example.consumer1.models.Data.Transfer.TransferInfoDTO;
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
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class StreamConsumer implements StreamListener<String, ObjectRecord<String, String>> {
    private static final Logger log = LoggerFactory.getLogger(StreamConsumer.class);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    // Session dataHandle
    private final List<TransferInfo> transferInfos = new ArrayList<>();
    private final List<ObjectRecord<String, String>> records = new ArrayList<>();

    // Backup <- Root
    private final List<String> backupList = new ArrayList<>();
    private final List<String> localList = new ArrayList<>();

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Autowired
    private TransferInfoRepository transferInfoRepository;

    @Value("${redis.local_stream}")
    private String localStream;
    @Value("${redis.app_stream}")
    private String appStream;

    // Read all in stream listener
    @Override
    public void onMessage(ObjectRecord<String, String> record) {
        // Received Data
        String json = record.getValue();
        if (json.isEmpty()) {
            return;
        }
        TransferInfoDTO transferInfoDTO = TransferInfoDTO.fromJson(json);

        if (transferInfoDTO != null && forMe(transferInfoDTO) && !streamExist(json)) {
            // 1. Save records
            records.add(record);
            // 2. Add to local stream
            ObjectRecord<String, String> newRecord = StreamRecords.newRecord()
                    .ofObject(json)
                    .withStreamKey(localStream);
            this.redisTemplate
                    .opsForStream()
                    .add(newRecord)
                    .subscribe(recordId -> {
                        System.out.println("Add to [" + localStream + "]: " + recordId);
                    });
            // 3. TransferInfoDTO -> TransferInfo(Entity)
            TransferInfo transferInfo = new TransferInfo(transferInfoDTO.getFrom(), transferInfoDTO.getTo(), transferInfoDTO.getTransferId());
            // 4. Save to Entity
            transferInfos.add(transferInfo);
            // 5. Add to H2-database
            addData(transferInfo);
            atomicInteger.incrementAndGet();
        }
    }

    private boolean forMe(TransferInfoDTO transferInfoDTO) {
        return transferInfoDTO.getFrom().equalsIgnoreCase("c1") || transferInfoDTO.getTo().equalsIgnoreCase("c1");
    }

    private boolean streamExist(String value) {
        AtomicBoolean check = new AtomicBoolean(false);
        // Read all records from the Redis stream starting from the beginning
        Flux<ObjectRecord<String, String>> records = this.redisTemplate.opsForStream()
                .read(String.class, StreamOffset.fromStart(localStream));

        // Filter the records to find if any record contains the specified value
        var bol = records.filter(record -> (value.equals(record.getValue())))
                .hasElements()
                .block(); // Block to get the result synchronously
        check.set(Boolean.TRUE.equals(bol));
        return check.get();
    }

    private void addData(TransferInfo transferInfo) {
        this.transferInfoRepository.save(transferInfo);
    }

    // Show Result
    @Scheduled(fixedRateString = "${redis.check_rate}")
    public void showResultUntilNow() {
        createStreamGroup();
        othersBackup();
        System.out.println("Total Consumed: " + atomicInteger);
    }

    // Download from pub (Request)
    public void othersBackup() {
        System.out.print("Download Backup from Others -> ");
        backupList.clear();
        var records = this.redisTemplate.opsForStream()
                .read(String.class, StreamOffset.fromStart(appStream));
        records.subscribe(record -> {
            backupList.add(record.getValue());
        });
    }

    public void localBackup() {
        localList.clear();
        var records = this.redisTemplate.opsForStream()
                .read(String.class, StreamOffset.fromStart(localStream));
        records.subscribe(record -> {
            localList.add(record.getValue());
        });
    }

    public List<String> missingData() {
        localBackup();
        othersBackup();
        List<String> result = new ArrayList<>();

        for (String item : backupList) {
            if (!localList.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    // Utils
    private void createStreamGroup() {
        System.out.print("Create Stream-Group -> ");
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
