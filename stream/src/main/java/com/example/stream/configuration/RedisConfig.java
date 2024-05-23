package com.example.stream.configuration;

import com.example.stream.configuration.consumer.StreamConsumer;
import com.example.stream.models.Product;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);
    @Value("${redis.host:127.0.0.1}")
    String redisHost;
    @Value("${redis.port:6379}")
    int redisPort;
    @Value("${redis.channel_topic:channel-events}")
    String topic;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }


    private RedisTemplate<String, String> redisTemplate;

    @Bean
    @Primary
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        redisTemplate.afterPropertiesSet();

        this.redisTemplate = redisTemplate;
        createStreamGroup(purchaseGroup, purchaseGroup);
        return redisTemplate;
    }

    // Unknown
    @Bean
    public RedisProperties properties() {
        return new RedisProperties();
    }

    // Injects a StreamListener bean to handle Redis Stream messages
    @Value("${redis.stream_purchase}")
    private String purchaseGroup;

    @Autowired
    @Lazy
    private StreamListener<String, ObjectRecord<String, Product>> productListener;

    @Bean
    public Subscription subscription() throws UnknownHostException {
        // Set up options for the message listener container
        var options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1)) // Poll timeout of 1 second
                .targetType(Product.class) // The expected type of incoming messages
                .build();

        // Create the message listener container with a Redis connection factory and the specified options
        var listenerContainer = StreamMessageListenerContainer
                .create(redisConnectionFactory(), options);

        // Create and configure a subscription to automatically acknowledge messages
        var subscription = listenerContainer.receiveAutoAck(
                Consumer.from(purchaseGroup, InetAddress.getLocalHost().getHostName()), // Consumer identified by event group and host name
                StreamOffset.create(purchaseGroup, ReadOffset.lastConsumed()), // Start consuming from the last consumed offset
                productListener); // Processing logic for received messages

        // Start the listener container to begin receiving messages
        listenerContainer.start();

        // Return the configured subscription
        return subscription;
    }

    private void createStreamGroup(String key, String group) {
        try {
            this.redisTemplate.opsForStream().createGroup(key, group);
        } catch (RedisSystemException e) {
            var cause = e.getCause();
            if (cause != null) {
                log.info("Stream - redis group existed, skip group creation [{}]->[{}]", key, group);
            }
        }
    }

    // Listenner
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(topic);
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, channelTopic());
        return redisMessageListenerContainer;
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new StreamConsumer(), "onMessage");
    }
}
