package com.khumu.alimi.subscriber;

import com.google.gson.Gson;
import com.khumu.alimi.data.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber implements MessageListener {
    @Autowired
    Gson gson;

    public RedisMessageSubscriber() {
        System.out.println("RedisMessageSubscriber");
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println(message.toString());
        Notification n = gson.fromJson(message.toString(), Notification.class);
        System.out.println("Notification received: " + n.getKind() + n.getAction());
    }
}
