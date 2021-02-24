package com.khumu.alimi.listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khumu.alimi.data.Comment;
import com.khumu.alimi.data.EventMessage;
import com.khumu.alimi.repository.notification.NotificationRepository;
import com.khumu.alimi.service.notification.CommentEventMessageServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Component("redisCommentMessageListener")
public class RedisCommentMessageListener implements CommentMessageListener, MessageListener {
    private Gson gson;
    private CommentEventMessageServiceImpl commentEventMessageService;
    private Type commentMessageType = new TypeToken<EventMessage<Comment>>() {
    }.getType();

    @Autowired
    public RedisCommentMessageListener(Gson gson, NotificationRepository notificationRepository, CommentEventMessageServiceImpl commentEventMessageService) {
        this.gson = gson;
        this.commentEventMessageService = commentEventMessageService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println(message.toString());
        EventMessage<Comment> em = gson.fromJson(message.toString(), commentMessageType);
        commentEventMessageService.createNotifications(em);
    }
}
