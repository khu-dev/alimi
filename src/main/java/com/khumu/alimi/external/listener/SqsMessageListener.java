package com.khumu.alimi.external.listener;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.alimi.data.EventKind;
import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.CommentDto;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.dto.Tmp;
import com.khumu.alimi.external.push.PushManager;
import com.khumu.alimi.service.notification.CommentEventMessageServiceImpl;
import com.khumu.alimi.service.notification.NotificationServiceImpl;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SqsMessageListener {
    final CommentEventMessageServiceImpl commentEventMessageService;

    final MessageConverter messageConverter;
    final ObjectMapper objectMapper; // jackson과 같은 object mapper를 주입받음.
    final Jackson2ObjectMapperBuilder builder;
    @SqsListener(value = "khumu-notifications")
    @MessageMapping
    public void receiveMessage(
            String payload,
            @Headers() Map<String, String> headers) throws JsonProcessingException {
        String resourceKindStr = headers.getOrDefault("resource_kind","comments");
        String eventKindStr = headers.getOrDefault("event_kind", "create");
        EventKind eventKind = EventKind.valueOf(eventKindStr);
        ResourceKind resourceKind = ResourceKind.valueOf(resourceKindStr);

        if (resourceKind == ResourceKind.comments) {
            CommentDto commentDto = null;
            commentDto = objectMapper.readValue(payload, CommentDto.class);

            if (eventKind == EventKind.create) {
                log.info(commentDto.getId() + " 댓글이 생성되었습니다.");
                commentEventMessageService.createNotifications(resourceKind, eventKind, commentDto);
            }


        }
    }
}
