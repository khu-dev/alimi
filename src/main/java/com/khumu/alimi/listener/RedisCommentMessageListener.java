package com.khumu.alimi.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khumu.alimi.data.dto.CommentDto;
import com.khumu.alimi.data.entity.Comment;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.repository.NotificationRepository;
import com.khumu.alimi.service.notification.CommentEventMessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@RequiredArgsConstructor
@Component("redisCommentMessageListener")
public class RedisCommentMessageListener implements CommentMessageListener, MessageListener {
    final ObjectMapper jackson;
    final CommentEventMessageServiceImpl commentEventMessageService;
    final TypeReference<EventMessageDto<CommentDto>> commentMessageType = new TypeReference<>(){};

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("RedisCommentMessageListener.onMessage");
        System.out.println(message.toString());
        EventMessageDto<CommentDto> em = null;
        try {
            em = jackson.readValue(message.toString(), commentMessageType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        commentEventMessageService.createNotifications(em);
    }
}
