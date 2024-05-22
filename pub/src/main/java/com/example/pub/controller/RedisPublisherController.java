package com.example.pub.controller;

import com.example.pub.configuration.RedisMessagePublisher;
import com.example.pub.models.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/redis/pub")
public class RedisPublisherController {
    private static final Logger logger = LoggerFactory.getLogger(RedisPublisherController.class);

    @Autowired
    private RedisMessagePublisher messagePublisher;

    @Autowired
    private ProductRespository productRespository;

    @Autowired
    private SessionRespository sessionRespository;

    @PostMapping("/publisher")
    public void publish(@RequestBody Message message) {
        logger.info(">> publisher:{}", message);
        messagePublisher.publish(message.toString());
    }

    @GetMapping("/products")
    public List<String> getProducts() {
        List<String> results = new ArrayList<>();
        productRespository.findAll().forEach(product -> results.add(product.toString()));
        return results;
    }

    @PostMapping("/product_test")
    public void testProduct(@RequestBody Product product) {
        productRespository.save(product);
        messagePublisher.publish(" [Added Test Product ]");
    }

    @PostMapping("/product")
    public void newProduct(@RequestBody Product product) {
        String result = productAdd(product);
        messagePublisher.publish(" [Added " + result + "]");
    }

    @PutMapping("/product")
    public void updateProduct(@RequestBody Product product) {
        String result = productUpdate(product);
    }

    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable("id") String id) {
        String result = productDelete(id);
        messagePublisher.publish("[Delete product: " + result + "]");
    }

    @DeleteMapping("/products")
    public void deleteProducts() {
        productRespository.deleteAll();
        messagePublisher.publish("[Deleted all products]");
    }

    @PostMapping("/session")
    public void newSession() {
        Session session = new Session();
        sessionRespository.save(session);
        messagePublisher.publish(" [Added " + session + "]");
    }

    @GetMapping("/sessions")
    public List<String> getSessions() {
        Iterable<Session> sessions = sessionRespository.findAll();
        List<String> results = new ArrayList<>();
        sessions.forEach(session -> {
            results.add(session.toString());
        });
        messagePublisher.publish(" [Retrieved Session's data]");
        return results;
    }

    // Redis Process
    private @NotNull Optional<Product> getProduct(String id) {
        return productRespository.findById(id);
    }

    private String productAdd(@NotNull Product product) {
        Product newItem = new Product(product.getName(), product.getPrice(), product.getQuantity());
        productRespository.save(newItem);
        logger.info(">> List Product: {}", productRespository.findAll());

        return newItem.toString();
    }

    private String productUpdate(@NotNull Product product) {
        Optional<Product> item = getProduct(product.getId());
        if (item.isPresent()) {
            productRespository.save(product);
            Product itemValue = item.get();
            String updateLog = productUpdate_Log(itemValue, product);
            logger.info(">> Updated: {}", "Product[" + product.getId() + "]{" + updateLog + "}");
            return "Success";
        } else {
            return "Failed (Product haven't existed)";
        }
    }

    private String productUpdate_Log(Product raw, Product changed) {
        String log = "";
        int rawPrice = raw.getPrice();
        String rawName = raw.getName();
        int rawQuantity = raw.getQuantity();
        int changedPrice = changed.getPrice();
        String changedName = changed.getName();
        int changedQuantity = changed.getQuantity();

        log += (rawPrice != changedPrice) ? "[Price: " + rawPrice + " -> " + changedPrice + "]" : "";
        log += (rawQuantity != changedQuantity) ? "[Quantity: " + rawQuantity + " -> " + changedQuantity + "]" : "";
        log += (!Objects.equals(rawName, changedName)) ? "[Name: " + rawName + " -> " + changedName + "]" : "";
        return (log.isEmpty()) ? "[Not changed]" : log;
    }

    private @NotNull String productDelete(String id) {
        Optional<Product> product = getProduct(id);
        if (product.isPresent()) {
            productRespository.delete(product.get());
            logger.info(">> Deleted: {}", product.get().toString());
            return "Success";
        } else {
            return "Failed (Product haven't existed)";
        }
    }
}
