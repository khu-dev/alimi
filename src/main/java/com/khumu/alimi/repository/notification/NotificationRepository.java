package com.khumu.alimi.repository.notification;

import com.khumu.alimi.data.Notification;

import java.util.List;


public interface NotificationRepository {
    Notification create(Notification n);
    Notification get(Long id);
    Notification update(Notification n);

    List<Notification> list();
    List<Notification> list(String username);
}
