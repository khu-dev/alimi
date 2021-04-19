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
        String resourceKindStr = headers.getOrDefault("resource_kind","comments");
        String eventKindStr = headers.getOrDefault("event_kind", "create");
        EventKind eventKind = EventKind.valueOf(eventKindStr);
        ResourceKind resourceKind = ResourceKind.valueOf(resourceKindStr);

        if (resourceKind == ResourceKind.comments) {
            CommentDto commentDto = null;
            commentDto = objectMapper.readValue(body.getMessage(), CommentDto.class);

            if (eventKind == EventKind.create) {
                log.info(commentDto.getId() + " 댓글이 생성되었습니다.");
                notificationSubscriptionService.createSubscriptionIfNotExists(ArticleNotificationSubscription.builder()
                        .article(Article.builder().id(commentDto.getArticle()).build())
                        .subscriber(commentDto.getAuthor()).build()
                );
                commentEventMessageService.createNotifications(resourceKind, eventKind, commentDto);
            }
        }
    }
}
