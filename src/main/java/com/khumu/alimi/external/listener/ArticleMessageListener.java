package com.khumu.alimi.external.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component("articleMessageListener")
public interface ArticleMessageListener {
    void onMessage(Message message, byte[] pattern);
}
