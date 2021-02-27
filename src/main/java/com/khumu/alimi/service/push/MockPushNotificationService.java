package com.khumu.alimi.service.push;

import com.khumu.alimi.data.Notification;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

//@Primary
@Service
public class MockPushNotificationService implements PushNotificationService {
    @Override
    public void executeNotify(Notification n) {
        System.out.println("MockPushNotification.notify");
    }
}
