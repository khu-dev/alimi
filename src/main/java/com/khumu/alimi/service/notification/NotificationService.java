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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

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

    // 리소스에 대한 구독을 activate하거나 inactivate합니다.
    @Transactional
    public ResourceNotificationSubscription toggleSubscription(SimpleKhumuUserDto requestUser, ResourceNotificationSubscription body) throws Exception {
        body.setSubscriber(requestUser.getUsername());
        if (
                (body.getResourceKind() == ResourceKind.article && body.getArticle() == null) ||
                (body.getResourceKind() == ResourceKind.study_article && body.getStudyArticle() == null) ||
                (body.getResourceKind() == ResourceKind.announcement && body.getAnnouncement() == null)
        ) {
            throw new Exception("resource_kind에 맞는 resource id를 입력해주세요.");
        }

        List<ResourceNotificationSubscription> myExistingSubscriptions = resourceNotificationSubscriptionRepository.findAllByArticleAndSubscriber(
                body.getArticle(), body.getSubscriber());

        if (myExistingSubscriptions.isEmpty()) {
            log.info(body.getArticle() + " 번 게시물에 대한 " + body.getSubscriber() + " 유저의 게시글 알림 구독을 생성합니다.");
            return resourceNotificationSubscriptionRepository.save(body);
        }

        ResourceNotificationSubscription subscription = null;
        if (myExistingSubscriptions.size() > 1) {
            // 존재하던 구독이 많으면 첫 번째꺼 하나만 남김
            for (int i = 0; i < myExistingSubscriptions.size() - 1; i++) {
                log.warn(body.getArticle() + " 번 게시물에 대한 " + body.getSubscriber() + " 유저의 게시글 알림 구독이 너무 많아 마지막것만 남기고 나머지는 삭제합니다.");
                resourceNotificationSubscriptionRepository.delete(myExistingSubscriptions.get(i));
            }
            // myExistingSubscriptions이 db에서 삭제 시 같은 list를 참조하면서 삭제되었다해도 1칸짜리(index: 0)인 list에서
            // 첫 칸에 접근하는 것이니 올바른 동작임.
            // persistent layer에 있는 instance를 가져옴
            subscription = resourceNotificationSubscriptionRepository.getOne(myExistingSubscriptions.get(myExistingSubscriptions.size() - 1).getId());
            // 기존 알림 구독의 부정
            subscription.setIsActivated(!subscription.getIsActivated());
        } else {
            // 존재하던 구독이 없으면 새로 만듦.
            // 기본은 알림 구독
            subscription = resourceNotificationSubscriptionRepository.save(body);
            subscription.setIsActivated(true);
        }

        return subscription;
    }
}
