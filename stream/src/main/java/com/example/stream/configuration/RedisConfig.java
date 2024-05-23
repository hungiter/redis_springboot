package com.example.stream.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.stream.ObjectRecord;
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

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${redis.host:127.0.0.1}")
    String redisHost;
    @Value("${redis.port:6379}")
    int redisPort;
    @Value("${redis.stream_key}")
    String streamKey;
    @Value("${redis.channel_topic:channel-events}")
    String topic;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
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

    // Unknown
    @Bean
    public RedisProperties properties() {
        return new RedisProperties();
    }

    // Injects a StreamListener bean to handle Redis Stream messages
    @Autowired
    private StreamListener<String, ObjectRecord<String, String>> streamListener;

    @Bean
    public Subscription subscription() throws UnknownHostException {
        // Work as a MessageListener (<-Stream)
        // Configures StreamMessageListenerContainer options with a 1-second poll timeout and String as target type
        var options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .targetType(String.class)
                .build();

        // Creates the StreamMessageListenerContainer using the redisConnectionFactory and options
        var listenerContainer = StreamMessageListenerContainer
                .create(redisConnectionFactory(), options);

        // Sets up a subscription that auto-acknowledges messages
        var subscription = listenerContainer.receiveAutoAck(
                Consumer.from(streamKey, InetAddress.getLocalHost().getHostName()), // Creates a consumer using the stream key and local host name
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()), // Starts from the last consumed message
                streamListener); // Uses the injected StreamListener to handle messages

        listenerContainer.start(); // Starts the listener container
        return subscription; // Returns the subscription bean
    }

    // Required for listening
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(topic);
    }

    @Bean
    MessagePublisher messagePublisher() {
        return new RedisMessagePublisher(redisTemplate(redisConnectionFactory()), channelTopic());
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
        return new MessageListenerAdapter(new RedisMessageSubscriber(), "onMessage");
    }
}
