package com.khumu.alimi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// https://jojoldu.tistory.com/418
@PropertySource("application.properties")
@RequiredArgsConstructor
@Configuration
//@EnableRedisRepositories
public class RedisConfig {
//    private final RedisProperties redisProperties;
    @Autowired
    ApplicationContext context;

    @Value("${spring.redis.host}")
    private String redisHostName;

    @Value("${spring.redis.port}")
    private int redisPort;
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHostName, redisPort); // application.properties의 값 이용
    }

    // 딱히 template의 역할은 잘 모르겠음.
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        // 왜 해야하는지..?
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    //MessageListenerAdapter로 우리가 정의한 RedisMessageSubscriber를 전달한다.
    @Bean
    MessageListenerAdapter articleEventMessageListenerAdapter() {
        return new MessageListenerAdapter(context.getBean("ArticleMessageListener", MessageListener.class));
    }
    @Bean
    MessageListenerAdapter commentEventMessageListenerAdapter() {
        return new MessageListenerAdapter(context.getBean("CommentMessageListener", MessageListener.class));
    }

    // redis를 subscribe 하는 것 관련된 의존성들을 담는 컨테이너
    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(this.redisConnectionFactory());

        container.addMessageListener(context.getBean("articleEventMessageListenerAdapter", MessageListenerAdapter.class), new ChannelTopic("article"));
        container.addMessageListener(context.getBean("commentEventMessageListenerAdapter", MessageListenerAdapter.class), new ChannelTopic("comment"));
        return container;
    }
}