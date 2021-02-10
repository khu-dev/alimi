package com.khumu.alimi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

// https://jojoldu.tistory.com/418
@RequiredArgsConstructor
@Configuration
//@EnableRedisRepositories
public class RedisConfig {
//    private final RedisProperties redisProperties;

    // lettuce
    @Autowired
    MessageListener messageListenerAdapter;
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(); // application.properties의 값 이용
    }

    // 딱히 template의 역할은 잘 모르겠음.
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    //MessageListenerAdapter로 우리가 정의한 RedisMessageSubscriber를 전달한다.
    @Bean
    MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(messageListenerAdapter);
    }

    // redis를 subscribe 하는 것 관련된 의존성들을 담는 컨테이너
    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(this.redisConnectionFactory());
        container.addMessageListener(messageListenerAdapter(), topic());
        return container;
    }

    @Bean
    ChannelTopic topic(){
        return new ChannelTopic("tutorial");
    }
}