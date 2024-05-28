package com.example.consumer1.helper;

import com.example.consumer1.models.Data.Transfer.TransferInfoDTO;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class StreamDataHelper {
    ReactiveRedisTemplate<String, String> redisTemplate;
    String localStream;
    String appStream;
    String portName;
    StreamToDatabase streamToDatabase;

    ObjectRecord<String, String> record;
    boolean isBackup = false;

    public StreamDataHelper(ReactiveRedisTemplate<String, String> redisTemplate, String localStream, String appStream, String portName, ObjectRecord<String, String> record, StreamToDatabase streamToDatabase) {
        this.redisTemplate = redisTemplate;
        this.localStream = localStream;
        this.appStream = appStream;
        this.portName = portName;
        this.record = record;
        this.streamToDatabase = streamToDatabase;
    }

    /*-----------------------------------------Listen Value-----------------------------------------*/

    private void addToLocal(ObjectRecord<String, String> record) {
        ObjectRecord<String, String> newRecord = StreamRecords.newRecord()
                .ofObject(record.getValue()).withId(record.getId())
                .withStreamKey(localStream);
        this.redisTemplate
                .opsForStream()
                .add(newRecord)
                .subscribe(recordId -> {
                    System.out.println((isBackup ? "  Backup -> " : "Listen -> ") + "Add to [" + localStream + "]: " + recordId);
                });
    }

    private boolean validate() {
        TransferInfoDTO transferInfoDTO = TransferInfoDTO.fromJson(this.record.getValue());
        assert transferInfoDTO != null;
        return !isExisted() & isMine(transferInfoDTO);
    }

    private boolean isMine(TransferInfoDTO transferInfoDTO) {
        return transferInfoDTO.getFrom().equalsIgnoreCase(portName) || transferInfoDTO.getTo().equalsIgnoreCase(portName);
    }

    private boolean isExisted() {
        AtomicBoolean check = new AtomicBoolean(false);
        // Read all records from the Redis stream starting from the beginning
        Flux<ObjectRecord<String, String>> records = this.redisTemplate.opsForStream()
                .read(String.class, StreamOffset.fromStart(localStream));

        // Filter the records to find if any record contains the specified value
        var bol = records.filter(record -> (this.record.getValue().equals(record.getValue()) & this.record.getId().equals(record.getId())))
                .hasElements()
                .block(); // Block to get the result synchronously
        check.set(Boolean.TRUE.equals(bol));
        return check.get();
    }


    /*-----------------------------------------Backup Value-----------------------------------------*/
    public void addMissingValue() {
        this.isBackup = true;
        AtomicBoolean backup = new AtomicBoolean(false);
        AtomicInteger count = new AtomicInteger(0);

        System.out.println("- Backup process"); // maybe request
        missingData().forEach(record -> {
            TransferInfoDTO transferInfoDTO = TransferInfoDTO.fromJson(record.getValue());
            assert transferInfoDTO != null;
            this.record = record;
            if (validate()) {
                addToLocal(record);
                streamToDatabase.transferInfo_update(transferInfoDTO);
                backup.set(true);
                count.incrementAndGet();
            }
        });

        if (backup.get()) {
            System.out.println("- Backup success: " + count + " record(s) updated.");
        } else {
            System.out.println(" (up-to-date)");
        }
    }

    public List<ObjectRecord<String, String>> missingData() {
        List<ObjectRecord<String, String>> backupList = othersBackup();
        List<ObjectRecord<String, String>> localList = localBackup();
        List<ObjectRecord<String, String>> result = new ArrayList<>();

        for (ObjectRecord<String, String> item : backupList) {
            if (!localList.contains(item)) {
                result.add(item);
            }
        }

        return result;
    }

    public List<ObjectRecord<String, String>> othersBackup() {
        List<ObjectRecord<String, String>> backupList = new ArrayList<>();
        var backupRecords = this.redisTemplate.opsForStream()
                .read(String.class, StreamOffset.fromStart(appStream));
        // Countdown - Delay cuz async function
        CountDownLatch latch = new CountDownLatch(1);
        Flux.from(backupRecords)
                .doOnNext(backupList::add)
                .doOnComplete(latch::countDown)
                .subscribe();
        try {
            latch.await();  // Wait for all records to be processed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for backup records to be processed");
        }
        return backupList;
    }

    public List<ObjectRecord<String, String>> localBackup() {
        List<ObjectRecord<String, String>> localList = new ArrayList<>();
        var localRecords = this.redisTemplate.opsForStream()
                .read(String.class, StreamOffset.fromStart(localStream));
        // Countdown - Delay cuz async function
        CountDownLatch latch = new CountDownLatch(1);
        Flux.from(localRecords)
                .doOnNext(localList::add)
                .doOnComplete(latch::countDown)
                .subscribe();
        try {
            latch.await();  // Wait for all records to be processed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for backup records to be processed");
        }
        return localList;
    }

}
