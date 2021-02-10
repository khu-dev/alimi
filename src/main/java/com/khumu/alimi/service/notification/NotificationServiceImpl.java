package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.Notification;
import com.khumu.alimi.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    private NotificationRepository repo;

    public NotificationServiceImpl() {
        System.out.println("New NotificationServiceImpl");
    }

    @Override
    public Notification getNotification(int id) {
        System.out.println("hello, " + id);
        return null;
    }
}
