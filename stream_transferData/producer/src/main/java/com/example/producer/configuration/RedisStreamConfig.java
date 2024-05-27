package com.example.producer.configuration;

import com.example.producer.models.Data.Transfer.TransferInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisStreamConfig {
    private static final Logger log = LoggerFactory.getLogger(RedisStreamConfig.class);
    @Value("${redis.host:127.0.0.1}")
    String redisHost;

    @Value("${redis.port:6379}")
    int redisPort;

    @Value("${redis.app_stream}")
    String streamKey;

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
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        redisTemplate.afterPropertiesSet();
        this.redisTemplate = redisTemplate;
        createStreamGroup(streamKey, streamKey);
        return redisTemplate;
    }

    @Bean
    public RedisProperties properties() {
        return new RedisProperties();
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
}
