package com.khumu.alimi.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public interface CommentMessageListener {
    void onMessage(Message message, byte[] pattern);
}
