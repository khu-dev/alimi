package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.Notification;
import com.khumu.alimi.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository nr;

    @Autowired
    public NotificationServiceImpl(NotificationRepository nr) {
        System.out.println("New NotificationServiceImpl");
        System.out.println("### Repository: " + nr);
        this.nr = nr;
    }

    @Override
    public Notification getNotification(Long id) {
        return nr.get(id);
    }

    @Override
    public List<Notification> listNotifications(){
        return nr.list();
    }

    @Override
    public List<Notification> listNotificationsByUsername(String username){
        return nr.list(username);
    }
}
