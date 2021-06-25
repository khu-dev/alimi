package com.khumu.alimi.external.push;

import com.google.firebase.messaging.*;
import com.khumu.alimi.data.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class FcmPushManager implements PushManager{
    final FirebaseMessaging firebaseMessaging;

    public void notify(Notification n, String deviceToken) {
        try {
            System.out.println("Execute push notify to " + n.getRecipient() + "(" + deviceToken + ")");
            String result = FirebaseMessaging.getInstance().send(
                    createMessage(n, deviceToken)
            );
            System.out.println(result);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage(), n, deviceToken);
        }
    }

    public Message createMessage(Notification n, String deviceToken){
        Message message = Message.builder()
                .setToken(deviceToken)
                .setAndroidConfig(AndroidConfig.builder()
                        .setNotification(
                                AndroidNotification.builder() // title이나 body 중 하나라도 null이 아니어야 알림이 간다!
                                        .setTitle(n.getTitle())
                                        .setBody(n.getContent())
                                        .build())
                        .build())
                .build();
        return message;
    }
}
