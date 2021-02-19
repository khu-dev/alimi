package com.khumu.alimi.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component("commentMessageListener")
public interface CommentMessageListener {
    void onMessage(Message message, byte[] pattern);
}
