package com.example.stream.controller;

import com.example.stream.configuration.producer.StreamProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stream")
public class RedisStreamController {
    private static final Logger log = LoggerFactory.getLogger(RedisStreamController.class);

    @Autowired
    StreamProducer streamProducer;

    @PostMapping("/producer")
    public ResponseEntity<Void> intStreamProduct() {

        try {
            streamProducer.publishProduct();
            log.info("Add random products >> success");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
