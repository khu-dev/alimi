package com.khumu.alimi.listener;

import org.springframework.data.redis.connection.Message;

public interface ArticleMessageListener {
    void onMessage(Message message, byte[] pattern);
}
