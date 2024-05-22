package com.example.pub.configuration;

import com.example.pub.models.Session;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.core.convert.MappingConfiguration;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class RedisConfiguration {
    private static final Logger log = LoggerFactory.getLogger(RedisConfiguration.class);
    // application.properties
    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.topic:channel-events}")
    private String topic;

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, channelTopic());
        return redisMessageListenerContainer;
    }


    @Bean
    MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new RedisMessageSubscriber(), "onMessage");
    }

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(topic);
    }

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
        redisTemplate.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return redisTemplate;
    }


    @Bean
    MessagePublisher messagePublisher() {
        return new RedisMessagePublisher(redisTemplate(redisConnectionFactory()), channelTopic());
    }

    @Bean
    public RedisMappingContext keyValueMappingContext() {
        return new RedisMappingContext(new MappingConfiguration(new IndexConfiguration(), new MyKeyspaceConfiguration()));
    }

    public static class MyKeyspaceConfiguration extends KeyspaceConfiguration {

        @Override
        protected @NotNull Iterable<KeyspaceSettings> initialConfiguration() {
            KeyspaceSettings keyspaceSettings = new KeyspaceSettings(Session.class, "session");
            Long redisTimeToLive = 60L;
            keyspaceSettings.setTimeToLive(redisTimeToLive);
            return Collections.singleton(keyspaceSettings);
        }
    }

    @Component
    public static class SessionExpiredEventListener {
        @EventListener
        public void handleRedisKeyExpiredEvent(RedisKeyExpiredEvent<Session> event) {
            Session expiredSession = (Session) event.getValue();
            assert expiredSession != null;
            log.info("Session with key={} has expired", expiredSession.getId());
        }
    }
}
