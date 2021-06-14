package com.khumu.alimi.external.listener;

import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.khumu.alimi.data.EventKind;
import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.ArticleDto;
import com.khumu.alimi.data.dto.CommentDto;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.dto.SqsMessageBodyDto;
import com.khumu.alimi.service.notification.ArticleEventMessageServiceImpl;
import com.khumu.alimi.service.notification.CommentEventMessageServiceImpl;
import io.awspring.cloud.messaging.config.annotation.NotificationMessage;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SqsMessageListener {
    final CommentEventMessageServiceImpl commentEventMessageService;
    final ArticleEventMessageServiceImpl articleEventMessageService;

    final ObjectMapper objectMapper;

    @SqsListener(value = "khumu-notifications")
    public void receiveMessage(
            SqsMessageBodyDto body) throws JsonProcessingException {
        log.info("SQS 메시지를 가져왔습니다.");
        try{
            ResourceKind resourceKind = ResourceKind.valueOf(body.getMessageAttributes().getResourceKind().getValue());
            EventKind eventKind = EventKind.valueOf(body.getMessageAttributes().getEventKind().getValue());
            EventMessageDto eventMessageDto = EventMessageDto.builder().eventKind(eventKind).resourceKind(resourceKind).build();
            log.info("ResourceKind: " + resourceKind + ", EventKind: " + eventKind + ", EventMessageDto: " + eventMessageDto);
            // 실제 발생한 이벤트 속 정보를 얻기 위해선 body.getMessage()를 Deserialize 해야한다.
            // body.getMessage()는 각각의 리소스들의 Dto를 Json 문자열로 나타낸 것이다.
            // 예를 들어 CommentDto의 Json 문자열 버젼.
            switch (resourceKind){
                case comment:{
                    CommentDto commentDto = objectMapper.readValue(body.getMessage(), CommentDto.class);
                    eventMessageDto.setResource(commentDto);
                    commentEventMessageService.createNotifications(eventMessageDto);
                    break;
                }
                case article: {
                    ArticleDto articleDto = objectMapper.readValue(body.getMessage(), ArticleDto.class);
                    eventMessageDto.setResource(articleDto);
                    articleEventMessageService.createArticleNotificationSubscriptionForAuthor(eventMessageDto);
                    break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("SQS 메시지 처리 도중 오류 발생!");
        }


        System.out.println("SQS 비용을 줄이기 위한 Dummy wait 시작");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("SQS 비용을 줄이기 위한 Dummy wait 마무리");
    }
}
