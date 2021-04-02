package com.khumu.alimi.listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khumu.alimi.data.dto.CommentDto;
import com.khumu.alimi.data.entity.Comment;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.repository.NotificationRepository;
import com.khumu.alimi.service.notification.CommentEventMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component("redisCommentMessageListener")
public class RedisCommentMessageListener implements CommentMessageListener, MessageListener {
    private Gson gson;
    private CommentEventMessageServiceImpl commentEventMessageService;
    private Type commentMessageType = new TypeToken<EventMessageDto<Comment>>() {
    }.getType();

    @Autowired
    public RedisCommentMessageListener(Gson gson, NotificationRepository notificationRepository, CommentEventMessageServiceImpl commentEventMessageService) {
        this.gson = gson;
        this.commentEventMessageService = commentEventMessageService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("RedisCommentMessageListener.onMessage");
        System.out.println(message.toString());
        EventMessageDto<CommentDto> em = gson.fromJson(message.toString(), commentMessageType);
        commentEventMessageService.createNotifications(em);
    }
}
