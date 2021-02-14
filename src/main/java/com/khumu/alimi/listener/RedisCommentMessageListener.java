package com.khumu.alimi.listener;

import com.google.gson.Gson;
import com.khumu.alimi.data.EventMessage;
import com.khumu.alimi.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service("CommentMessageListener")
public class RedisCommentMessageListener implements MessageListener, CommentMessageListener {
    @Autowired
    Gson gson;
    @Autowired
    NotificationRepository notificationRepository;

    public RedisCommentMessageListener() {
        System.out.println("CommentMessageListener");
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventMessage e = gson.fromJson(message.toString(), EventMessage.class);
        System.out.println(message.toString());
        System.out.println("EventMessage received:" + "\n\t" + e.getResourceKind() + "\n\t" + e.getEventKind());
    }

}
