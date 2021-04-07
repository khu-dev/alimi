package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    final NotificationRepository nr;

    @Override
    public Notification getNotification(Long id) {
        Notification n = nr.getOne(id);
//        applyPlainForeignKey(n);
        return n;
    }

    @Override
    @Transactional
    public void read(Long id) {
        Notification n = nr.getOne(id);
        n.setRead(true);
    }

    @Override
    public List<Notification> listNotifications(){
        System.out.println("NotificationServiceImpl.listNotifications");
        List<Notification> ns = nr.findAll();
        for (Notification n : ns) {
//            applyPlainForeignKey(n);
        }
        return ns;
    }

    @Override
    public List<Notification> listNotificationsByUsername(String username){
        List<Notification> ns = nr.list(username);
        for (Notification n : ns) {
//            applyPlainForeignKey(n);
        }
        return ns;
    }
}
