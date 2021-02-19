package com.khumu.alimi.repository.notification;

import com.khumu.alimi.data.Comment;
import com.khumu.alimi.data.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

// Custom JPA Repository
// 알아서 구현된 Jpa Repository와 그걸 담은 interface는 composition으로 이용.
@Repository
@Primary
@Transactional
public class JpaNotificationRepository implements NotificationRepository{
    private final JpaNotificationRepositoryInterface jpa;
    @Autowired
    public JpaNotificationRepository(JpaNotificationRepositoryInterface jpa) {
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
        return null;
    }

    @Override
    public List<Notification> list() {
        System.out.println("JpaNotificationRepository.list");
        return jpa.findAll();
    }

    @Override
    public List<Notification> list(String username) {
        System.out.println("JpaNotificationRepository.list");
        return jpa.list(username);
    }
}