package com.khumu.alimi.repository.notification;

import com.khumu.alimi.data.Notification;
import com.khumu.alimi.data.SimpleKhumuUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

// Custom JPA Repository
// 알아서 구현된 Jpa Repository와 그걸 담은 interface는 composition으로 이용.
@Repository
@Primary
//@Transactional
public class JpaNotificationRepository implements NotificationRepository{
    private final JpaNotificationRepositoryIfc jpa;
    @Autowired
    public JpaNotificationRepository(JpaNotificationRepositoryIfc jpa) {
        System.out.println("JpaNotificationRepositoryImpl.JpaNotificationRepositoryImpl");
        this.jpa = jpa;
    }

    @Override
    public Notification create(Notification n){
        System.out.println("NotificationRepositoryJpaImpl.create");
        Notification created = jpa.save(n);
        return created;
    }

    @Override
    public Notification get(Long id) {
        return jpa.findById(id).get();
    }

    @Override
    public List<Notification> list() {
        System.out.println("JpaNotificationRepository.list");
        return jpa.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public List<Notification> list(String username) {
        System.out.println("JpaNotificationRepository.list");
        return jpa.list(username);
    }

    @Override
    public Notification update(Notification n) {
        return jpa.save(n);
    }

    public void preprocess(Notification n) {
        if (n.getRecipientObj() == null) {
            n.setRecipientObj(new SimpleKhumuUser(n.getRecipient()));
        } else {
            n.setRecipient(n.getRecipientObj().getUsername());
        }
    }
}