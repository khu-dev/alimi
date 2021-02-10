package com.khumu.alimi.repository.notification;

import com.khumu.alimi.data.Notification;


public interface NotificationRepository {
    Notification create(Notification n);
    Notification get(int id);
}
