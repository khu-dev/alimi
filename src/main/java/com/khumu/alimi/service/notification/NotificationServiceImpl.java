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
        System.out.println("Repository: " + nr.getClass());
        this.nr = nr;
    }

    @Override
    public Notification getNotification(Long id) {
        Notification n = nr.get(id);
        applyPlainForeignKey(n);
        return n;
    }

    @Override
    public void read(Long id) {
        Notification n = nr.get(id);
        n.setRead(true);
        nr.update(n);
    }

    @Override
    public List<Notification> listNotifications(){
        System.out.println("NotificationServiceImpl.listNotifications");
        List<Notification> ns = nr.list();
        for (Notification n : ns) {
            applyPlainForeignKey(n);
        }
        return ns;
    }

    @Override
    public List<Notification> listNotificationsByUsername(String username){
        List<Notification> ns = nr.list(username);
        for (Notification n : ns) {
            applyPlainForeignKey(n);
        }
        return ns;
    }

    /**
     * Repository 계층은 plain하게 foreing key를 field로 사용할 수 없기 떄문에
     * instance에서 필요한 값을 꺼내 plain한 field에 적용한다.
     * @param n
     */
    private void applyPlainForeignKey(Notification n){
        n.setRecipient(n.getRecipientObj().getUsername());
    }
}
