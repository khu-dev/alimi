package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.NotificationDto;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import com.khumu.alimi.mapper.NotificationMapper;
import com.khumu.alimi.repository.NotificationRepository;
import com.khumu.alimi.repository.ResourceNotificationSubscriptionRepository;
import com.khumu.alimi.service.KhumuException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    final NotificationRepository nr;
    final ResourceNotificationSubscriptionRepository resourceNotificationSubscriptionRepository;

    final NotificationMapper notificationMapper;
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

    public List<NotificationDto> listNotifications(Pageable pageable){
        System.out.println("NotificationServiceImpl.listNotifications");
        Page<Notification> ns = nr.findAll(pageable);

        return ns.map(notificationMapper::toDto).toList();
    }

    public List<NotificationDto> listNotificationsByUsername(String username, Pageable pageable){
        Page<Notification> ns = nr.findAllByRecipient(username, pageable);

        return ns.map(notificationMapper::toDto).toList();
    }

    @Transactional
    // Controller가 사용할 메소드
    public void subscribe(SimpleKhumuUserDto requestUser, ResourceNotificationSubscription body) throws KhumuException.WrongResourceKindException {
        ResourceNotificationSubscription subscription = getOrCreateSubscription(requestUser, body);
        log.info("구독을 활성화합니다.");
        subscription.setIsActivated(true);
    }

    @Transactional
    // Controller가 사용할 메소드
    public void unsubscribe(SimpleKhumuUserDto requestUser, ResourceNotificationSubscription body) throws KhumuException.WrongResourceKindException {
        ResourceNotificationSubscription subscription = getOrCreateSubscription(requestUser, body);
        log.info("구독을 비활성화합니다.");
        subscription.setIsActivated(false);
    }

    @Transactional
    private ResourceNotificationSubscription getOrCreateSubscription(SimpleKhumuUserDto subscriber, ResourceNotificationSubscription body) throws KhumuException.WrongResourceKindException {
        List<ResourceNotificationSubscription> subscriptions = null;
        ResourceNotificationSubscription subscription = null;
        ResourceKind resourceKind = body.getResourceKind();
        if (resourceKind == ResourceKind.article) {
            subscriptions = resourceNotificationSubscriptionRepository.findAllByArticleAndSubscriber(body.getArticle(), subscriber.getUsername());
        } else if (resourceKind == ResourceKind.study_article) {
            subscriptions = resourceNotificationSubscriptionRepository.findAllByStudyArticleAndSubscriber(body.getStudyArticle(), subscriber.getUsername());
        } else{
            throw new KhumuException.WrongResourceKindException();
        }

        if (subscriptions.isEmpty()) {
            log.info(subscriber.getUsername() + "의 해당 Article에 대한 구독을 생성합니다.");
            subscription = resourceNotificationSubscriptionRepository.save(ResourceNotificationSubscription.builder()
                    .article(body.getArticle())
                    .studyArticle(body.getStudyArticle())
                    .subscriber(subscriber.getUsername())
                    .resourceKind(resourceKind)
                    .isActivated(true) // 기본값은 true이다.
                    .build());
        } else {
            log.info("이미 " + subscriber.getUsername() + "가 해당 Article을 구독 중입니다. 새로운 구독 생성 작업을 수행하지 않겠습니다.");
            // 마지막 구독을 allocate
            subscription = subscriptions.get(subscriptions.size() - 1);
            if (subscriptions.size() > 1) {
                log.warn("Article(" + body.getArticle() + ")에 대한 " + subscriber.getUsername() + "의 구독이 이미 2개 이상 존재합니다. 마지막 구독만 남기고 나머지를 삭제합니다.");

                resourceNotificationSubscriptionRepository.deleteAll(subscriptions.subList(0, subscriptions.size() - 1));
            }
        }

        return subscription;
    }

    // 리소스에 대한 구독을 activate하거나 inactivate합니다.
//    @Transactional
//    public ResourceNotificationSubscription toggleSubscription(SimpleKhumuUserDto requestUser, ResourceNotificationSubscription body) throws Exception {
//        body.setSubscriber(requestUser.getUsername());
//        if (
//                (body.getResourceKind() == ResourceKind.article && body.getArticle() == null) ||
//                (body.getResourceKind() == ResourceKind.study_article && body.getStudyArticle() == null) ||
//                (body.getResourceKind() == ResourceKind.announcement && body.getAnnouncement() == null)
//        ) {
//            throw new Exception("resource_kind에 맞는 resource id를 입력해주세요.");
//        }
//
//        List<ResourceNotificationSubscription> myExistingSubscriptions = resourceNotificationSubscriptionRepository.findAllByArticleAndSubscriber(
//                body.getArticle(), body.getSubscriber());
//
//        if (myExistingSubscriptions.isEmpty()) {
//            log.info(body.getArticle() + " 번 게시물에 대한 " + body.getSubscriber() + " 유저의 게시글 알림 구독을 생성합니다.");
//            return resourceNotificationSubscriptionRepository.save(body);
//        }
//
//        ResourceNotificationSubscription subscription = null;
//        if (myExistingSubscriptions.size() > 1) {
//            // 존재하던 구독이 많으면 첫 번째꺼 하나만 남김
//            for (int i = 0; i < myExistingSubscriptions.size() - 1; i++) {
//                log.warn(body.getArticle() + " 번 게시물에 대한 " + body.getSubscriber() + " 유저의 게시글 알림 구독이 너무 많아 마지막것만 남기고 나머지는 삭제합니다.");
//                resourceNotificationSubscriptionRepository.delete(myExistingSubscriptions.get(i));
//            }
//            // myExistingSubscriptions이 db에서 삭제 시 같은 list를 참조하면서 삭제되었다해도 1칸짜리(index: 0)인 list에서
//            // 첫 칸에 접근하는 것이니 올바른 동작임.
//            // persistent layer에 있는 instance를 가져옴
//            subscription = resourceNotificationSubscriptionRepository.getOne(myExistingSubscriptions.get(myExistingSubscriptions.size() - 1).getId());
//            // 기존 알림 구독의 부정
//            subscription.setIsActivated(!subscription.getIsActivated());
//        } else {
//            // 존재하던 구독이 없으면 새로 만듦.
//            // 기본은 알림 구독
//            subscription = resourceNotificationSubscriptionRepository.save(body);
//            subscription.setIsActivated(true);
//        }
//
//        return subscription;
//    }
}
