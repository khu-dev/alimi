package com.khumu.alimi.service.push;

import com.khumu.alimi.data.entity.Notification;

public interface PushNotificationService {
    // 그냥 notify라고 하기에는 Java의 Object class와 이름이 겹침.
    void executeNotify(Notification n);
}
