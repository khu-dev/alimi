package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.entity.Notification;

import java.util.List;

public interface NotificationService {
    Notification getNotification(Long id);

    void read(Long id);
    List<Notification> listNotifications();
    List<Notification> listNotificationsByUsername(String username);

}
