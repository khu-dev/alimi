package com.khumu.alimi.external.push;

import com.google.firebase.messaging.*;
import com.khumu.alimi.data.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class FcmPushManager implements PushManager{
    final FirebaseMessaging firebaseMessaging;
    @Value("${khumu.notification.rootLink}")
    String NOTIFICATION_ROOT_LINK;

    public void notify(Notification n, String deviceToken) {
        try {
            System.out.println("Execute push notify to " + n.getRecipient() + "(" + deviceToken + ")");
            String result = firebaseMessaging.send(
                    createMessage(n, deviceToken)
            );
            System.out.println(result);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            log.error(e.getMessage(), n, deviceToken);
        }
    }

    public Message createMessage(Notification n, String deviceToken){
        String link = null;
        if (n.getLink() != null) {
            // announcement의 경우 reference도 있고 reference도 있긴함.
            link = n.getLink();
        } else{
            link = NOTIFICATION_ROOT_LINK + "/" + n.getReference();
        }
        Message message = Message.builder()
                .setToken(deviceToken)
                .setNotification(com.google.firebase.messaging.Notification.builder()
                    .setTitle(n.getTitle())
                    .setBody(n.getContent())
                    .build())
                .putData("link", link)
                .build();

        return message;
    }
}
