package com.khumu.alimi.external.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component("commentMessageListener")
public interface CommentMessageListener {
    void onMessage(Message message, byte[] pattern);
}
