package com.khumu.alimi.listener;

import com.google.gson.Gson;
import com.khumu.alimi.data.EventMessage;
import com.khumu.alimi.repository.notification.NotificationRepository;
import com.khumu.alimi.service.notification.ArticleEventMessageServiceImpl;
import com.khumu.alimi.service.notification.CommentEventMessageServiceImpl;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("redisArticleMessageListener")
public class RedisArticleMessageListener implements ArticleMessageListener, MessageListener{
    private Gson gson;
    private ArticleEventMessageServiceImpl articleMessageServiceImpl;
    @Autowired
    public RedisArticleMessageListener(Gson gson, ArticleEventMessageServiceImpl articleEventMessageService) {
        this.gson = gson;
        this.articleMessageServiceImpl = articleEventMessageService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
