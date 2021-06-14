package com.khumu.alimi;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.fasterxml.jackson.databind.MapperFeature;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.config.SimpleMessageListenerContainerFactory;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.QueueMessageHandler;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.SqsMessageMethodArgumentResolver;
import io.awspring.cloud.messaging.support.NotificationMessageArgumentResolver;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.support.HeadersMethodArgumentResolver;
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.reactive.ArgumentResolverConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

@Configuration
class KhumuSqsConfig {
    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSqs) {
        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
        factory.setAmazonSqs(amazonSqs);
        // 한 번에 가져오는 메시지의 수.
        factory.setMaxNumberOfMessages(10);
        // Long polling. wait time까지 기다리거나 메시지가 MaxNumber가 되면 task 실행. 배치 기다리는 시간같은 느낌.
        factory.setWaitTimeOut(20);
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor("SqsListener");
        // 한 번에 가져온 메시지를 동시에 처리하는 개수
        // Promise all 처럼 동작
        // 그 중 하나가 만약 처리되지 못한다면..? fail...
        simpleAsyncTaskExecutor.setConcurrencyLimit(11); // -1은 concurrency 없는 거. MaxNumberOfMessages + 1과 동일하게 동작되는 셈.
        factory.setTaskExecutor(simpleAsyncTaskExecutor);

        factory.setAutoStartup(true);
//        SimpleMessageConverter
//        QueueMessagingTemplate
        return factory;
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory() {
        QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
        // header들을 Map<String, String>으로 받을 수 있는 Resolver,
        // Payload를 messageConverter를 이용해 parse하는 Resolver
//        factory.setArgumentResolvers(Arrays.asList(
//                new SqsMessageMethodArgumentResolver()
//                new HeadersMethodArgumentResolver(),
////                new PayloadArgumentResolver(messageConverter()))
//                new PayloadMethodArgumentResolver(messageConverter()),
//                new NotificationMessageArgumentResolver(messageConverter()))
////        );
        factory.setSqsMessageDeletionPolicy(SqsMessageDeletionPolicy.ON_SUCCESS);
        return factory;
    }

    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.getObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        // set strict content type match to false
        messageConverter.setStrictContentTypeMatch(false);
        return messageConverter;
    }
}