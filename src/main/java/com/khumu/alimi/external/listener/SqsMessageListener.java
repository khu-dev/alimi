package com.khumu.alimi.external.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.alimi.data.EventKind;
import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.CommentDto;
import com.khumu.alimi.data.dto.SqsMessageBodyDto;
import com.khumu.alimi.data.entity.Article;
import com.khumu.alimi.data.entity.ArticleNotificationSubscription;
import com.khumu.alimi.service.notification.CommentEventMessageServiceImpl;
import com.khumu.alimi.service.notification.NotificationSubscriptionServiceImpl;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SqsMessageListener {
    final CommentEventMessageServiceImpl commentEventMessageService;
    final NotificationSubscriptionServiceImpl notificationSubscriptionService;
    final ObjectMapper objectMapper;

    @SqsListener(value = "khumu-notifications")
    public void receiveMessage(
            SqsMessageBodyDto body,
            @Headers() Map<String, String> headers) throws JsonProcessingException {
        log.info("SQS 메시지를 가져왔습니다.");
        String resourceKindStr = headers.getOrDefault("resource_kind","comments");
        String eventKindStr = headers.getOrDefault("event_kind", "create");
        EventKind eventKind = EventKind.valueOf(eventKindStr);
        ResourceKind resourceKind = ResourceKind.valueOf(resourceKindStr);
        if (resourceKind == ResourceKind.comments) {
            CommentDto commentDto = null;
            try {
                commentDto = objectMapper.readValue(body.getMessage(), CommentDto.class);
                if (eventKind == EventKind.create) {
                    log.info(commentDto.getId() + " 댓글이 생성되었습니다.");
                    notificationSubscriptionService.createSubscriptionIfNotExists(ArticleNotificationSubscription.builder()
                            .article(Article.builder().id(commentDto.getArticle()).build())
                            .subscriber(commentDto.getAuthor()).build()
                    );
                    commentEventMessageService.createNotifications(resourceKind, eventKind, commentDto);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("SQS 메시지 처리 도중 오류 발생!");
            }



        }
        System.out.println("SQS 비용을 줄이기 위한 Dummy wait 시작");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("SQS 비용을 줄이기 위한 Dummy wait 마무리");
    }
}
