package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import com.khumu.alimi.repository.NotificationRepository;
import com.khumu.alimi.repository.ResourceNotificationSubscriptionRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl{

    final NotificationRepository nr;
    final ResourceNotificationSubscriptionRepository resourceNotificationSubscriptionRepository;
    public Notification getNotification(Long id) {
        Notification n = nr.getOne(id);
//        applyPlainForeignKey(n);
        return n;
    }

    @Transactional
    public void read(Long id) {
        Notification n = nr.getOne(id);
        n.setIsRead(true);
    }

    public List<Notification> listNotifications(){
        System.out.println("NotificationServiceImpl.listNotifications");
        List<Notification> ns = nr.findAll();
        for (Notification n : ns) {
//            applyPlainForeignKey(n);
        }
        return ns;
    }

    public List<Notification> listNotificationsByUsername(String username){
        List<Notification> ns = nr.list(username);
        for (Notification n : ns) {
//            applyPlainForeignKey(n);
        }
        return ns;
    }

    public ResourceNotificationSubscription subscribeResource(SimpleKhumuUserDto requestUser, ResourceNotificationSubscription body) throws Exception {
        body.setSubscriber(requestUser.getUsername());
        if (
                (body.getResourceKind() == ResourceKind.article && body.getArticle() == null) ||
                (body.getResourceKind() == ResourceKind.study_article && body.getStudyArticle() == null) ||
                (body.getResourceKind() == ResourceKind.announcement && body.getAnnouncement() == null)
        ) {
            throw new Exception("resource_kind에 맞는 resource id를 입력해주세요.");
        }
        ResourceNotificationSubscription subscription = resourceNotificationSubscriptionRepository.save(body);
        return subscription;
    }
}
