package jpa.learning.neo4j.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisConfig {
    @Value("${redis.host:127.0.0.1}")
    String host;

    @Value("${redis.port:6379}")
    int port;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        // Tạo Standalone Connection tới Redis
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    @Bean
    @Primary
    public ReactiveRedisTemplate<String, String> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisSerializationContext<String, String> serializationContext = RedisSerializationContext
                .<String, String>newSerializationContext(new StringRedisSerializer())
                .key(new StringRedisSerializer())
                .value(new GenericToStringSerializer<>(String.class))
                .hashKey(new StringRedisSerializer())
                .hashValue(new GenericToStringSerializer<>(String.class))
                .build();

        return new ReactiveRedisTemplate<>(lettuceConnectionFactory, serializationContext);
    }
}

