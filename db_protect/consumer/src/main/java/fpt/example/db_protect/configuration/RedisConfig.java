package fpt.example.db_protect.configuration;

import fpt.example.db_protect.models.Sessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.Subscription;


import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;


@Configuration
@EnableCaching
public class RedisConfig {
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);
    @Value("${redis.host:127.0.0.1}")
    String redisHost;

    @Value("${redis.port:6379}")
    int redisPort;

    @Value("${redis.entity_home:entity_home}")
    String parentStream;


    RedisTemplate<Object, Object> redisTemplate;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        // Tạo Standalone Connection tới Redis
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        redisTemplate.afterPropertiesSet();
        this.redisTemplate = redisTemplate;
        createStreamGroup(parentStream, parentStream);
        return redisTemplate;
    }

    @Bean
    public RedisProperties properties() {
        return new RedisProperties();
    }

    // Stream Consumer Config -------------- Start
    @Autowired
    @Lazy
    public StreamListener<String, ObjectRecord<String, Sessions>> entityListener;

    @Bean
    public Subscription subscription() {
        var listenerContainer = streamMessageListenerContainer();

        Subscription subscription = listenerContainer.receive(StreamOffset.fromStart(parentStream), entityListener);
        listenerContainer.start();
        return subscription;
    }

    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, Sessions>> streamMessageListenerContainer() {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, Sessions>> options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofMillis(100))
                .targetType(Sessions.class)
                .build();

        return StreamMessageListenerContainer
                .create(redisConnectionFactory(), options);
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
    // Stream Consumer Config -------------- End


    // Redis Cache Config -------------- Start
    @Value("${redis.itemCache:5}")
    int itemTTL;
    @Value("${redis.queryCache:3}")
    int queryTTL;

    @Bean
    public CacheManager cacheManager() {
        return RedisCacheManager
                .builder(redisConnectionFactory())
                .cacheDefaults(defaultCacheConfiguration())
                .withCacheConfiguration("itemCache",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(itemTTL)))
                .withCacheConfiguration("queryCache",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(queryTTL)))
                .build();
    }

    private RedisCacheConfiguration defaultCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60))  // TTL for cache
                .disableCachingNullValues();  // Do not cache null values
    }
    // Redis Cache Config -------------- End
}
