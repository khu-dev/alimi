package com.khumu.alimi.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.khumu.alimi.service.notification.ArticleEventMessageServiceImpl;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("redisArticleMessageListener")
public class RedisArticleMessageListener implements ArticleMessageListener, MessageListener{
    @Autowired
    final ObjectMapper jackson;
    final ArticleEventMessageServiceImpl articleMessageServiceImpl;

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
