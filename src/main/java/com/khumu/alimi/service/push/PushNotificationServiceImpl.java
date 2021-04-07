package com.khumu.alimi.service.push;

import com.google.firebase.messaging.*;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.PushSubscription;
import com.khumu.alimi.repository.PushSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@RequiredArgsConstructor
@Service
public class PushNotificationServiceImpl implements  PushNotificationService{

    private final PushSubscriptionRepository psr;

    @Override
    public void executeNotify(Notification n) {
        System.out.println("PushNotificationServiceImpl.executeNotify");
        List<PushSubscription> subscriptions = psr.listByUsername(n.getRecipient().getUsername());
        for (PushSubscription s : subscriptions) {
            String token = s.getDeviceToken();
            try {
                System.out.println("execute push notify to "+s.getUser().getUsername());
                System.out.println(token);
                String result = FirebaseMessaging.getInstance().send(
                        createMessage(n, token)
                );
                System.out.println(result);
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
            }

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
