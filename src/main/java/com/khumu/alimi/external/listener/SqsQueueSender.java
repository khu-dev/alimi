/**
 * SQS에 메시지 보내지는지 테스트용
 */

//package com.khumu.alimi.external.listener;
//
//import com.amazonaws.services.sqs.AmazonSQSAsync;
//import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SqsQueueSender {
//
//    private final QueueMessagingTemplate queueMessagingTemplate;
//
//    @Autowired
//    public SqsQueueSender(AmazonSQSAsync amazonSQSAsync) {
//        this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSQSAsync);
//    }
//
//    public void send(String message) {
////        this.queueMessagingTemplate.send("khumu-notifications", MessageBuilder.withPayload(message).build());
////        this.queueMessagingTemplate.send("khumu-notifications", MessageBuilder.withPayload(message).build());
//        this.queueMessagingTemplate.send("https://sqs.ap-northeast-2.amazonaws.com/312979016576/khumu-notifications", MessageBuilder.withPayload(message).build());
//    }
//}
