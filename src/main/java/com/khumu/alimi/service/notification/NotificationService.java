package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.Notification;

import java.util.List;

public interface NotificationService {
    Notification getNotification(Long id);

    List<Notification> listNotifications();
    List<Notification> listNotificationsByUsername(String username);

}