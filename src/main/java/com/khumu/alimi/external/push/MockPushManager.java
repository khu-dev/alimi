package com.khumu.alimi.external.push;

import com.google.firebase.messaging.*;
import com.khumu.alimi.data.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MockPushManager implements PushManager{
    @Override
    public void notify(Notification n, String deviceToken) {
        log.warn("Pass real push notifications.");
    }

    public Message createMessage(Notification n, String deviceToken){
        log.warn("Pass real create message");
        return null;
    }
}
