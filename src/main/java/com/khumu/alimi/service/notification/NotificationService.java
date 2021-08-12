package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.NotificationDto;
import com.khumu.alimi.data.dto.ResourceNotificationSubscriptionDto;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import com.khumu.alimi.mapper.NotificationMapper;
import com.khumu.alimi.repository.NotificationRepository;
import com.khumu.alimi.repository.ResourceNotificationSubscriptionRepository;
import com.khumu.alimi.service.KhumuException;
import javassist.Loader;
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

import static com.khumu.alimi.service.KhumuException.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    final NotificationRepository nr;
    final ResourceNotificationSubscriptionRepository resourceNotificationSubscriptionRepository;

    final NotificationMapper notificationMapper;
    public Notification getNotification(Long id) {
        Notification n = nr.findById(id).get();
//        applyPlainForeignKey(n);
        return n;
    }

    public List<NotificationDto> listNotifications(Pageable pageable){
        System.out.println("NotificationServiceImpl.listNotifications");
        Page<Notification> ns = nr.findAll(pageable);

        return ns.map(notificationMapper::toDto).toList();
    }

    public List<NotificationDto> listNotificationsByUsername(SimpleKhumuUserDto requestUser, String username, Pageable pageable) throws NoPermissionException, UnauthenticatedException {
        log.info("listNotificationsByUsername의 인자 로깅. requestUser: " + requestUser + ", recipientUsername: " + username);
        if (username.equals("me")) {
            if (requestUser == null || requestUser.getUsername() == null) {
                throw new UnauthenticatedException("설정한 경우 인증된 유저만이 recipient를 me로 설정할 수 있습니다.");
            }
            username = requestUser.getUsername();
            log.info("recipient: me => recipient: " + username + "으로 올바른 recipient 값을 설정합니다.");
        }

        if (requestUser == null || requestUser.getUsername() == null || !requestUser.getUsername().equals(username)) {
            log.error("알림을 조회할 권한이 없습니다. requestUser: " + requestUser + ", 조회하려는 recipient의 username: " + username);
            throw new NoPermissionException("현재는 본인의 알림만을 조회할 수 있습니다.");
        }
        Page<Notification> ns = nr.findAllByRecipient(username, pageable);

        return ns.map(notificationMapper::toDto).toList();
    }

    @Transactional
    public void read(SimpleKhumuUserDto requestUser, Long id) throws NoPermissionException {
        Notification n = nr.findById(id).get();
        n.setIsRead(true);
        throwWhenNotRecipient(requestUser, n);
        log.info(n + "을 읽음 처리했습니다.");
    }

    @Transactional
    public void readAll(SimpleKhumuUserDto requestUser) throws UnauthenticatedException {
        if (requestUser == null) {
            throw new UnauthenticatedException();
        }
        List<Notification> notifications = nr.findAllUnreadByRecipient(requestUser.getUsername(), Pageable.unpaged()).getContent();
        for (Notification n : notifications) {
            n.setIsRead(true);
            log.info(n + "을 읽음 처리했습니다.");
        }
    }

    @Transactional
    public void unread(SimpleKhumuUserDto requestUser, Long id) throws NoPermissionException {
        Notification n = nr.getOne(id);
        n.setIsRead(false);
        throwWhenNotRecipient(requestUser, n);
        log.info(n + "을 읽지 않음 처리했습니다.");
    }

    @Transactional
    public void unreadAll(SimpleKhumuUserDto requestUser) throws UnauthenticatedException {
        if (requestUser == null) {
            throw new UnauthenticatedException();
        }
        List<Notification> notifications = nr.findAllReadByRecipient(requestUser.getUsername(), Pageable.unpaged()).getContent();
        for (Notification n : notifications) {
            n.setIsRead(false);
            log.info(n + "을 읽지 않음 처리했습니다.");
        }
    }

    @Transactional
    public void delete(SimpleKhumuUserDto requestUser, Long id) throws NoPermissionException {
        Notification n = nr.getOne(id);
        throwWhenNotRecipient(requestUser, n);
        nr.delete(n);
    }

    private void throwWhenNotRecipient(SimpleKhumuUserDto requestUser, Notification notification) throws NoPermissionException {
        if (requestUser == null || requestUser.getUsername() == null || !requestUser.getUsername().equals(notification.getRecipient())) {
            throw new NoPermissionException("본인의 알림이 아니라 해당 작업을 수행할 권한이 없습니다.");
        }
    }

    @Transactional
    // Controller가 사용할 메소드
    // 해당 구독 정보에 해당하는 구독을 생성하거나 존재하는 경우 기존 구독 정보를 activated로 설정함.
    // 존재하지 않으면 activated 상태로 생성한다.
    public void subscribe(SimpleKhumuUserDto requestUser, ResourceNotificationSubscription body) throws WrongResourceKindException, UnauthenticatedException {
        if (requestUser == null) {
            throw new UnauthenticatedException();
        }
        ResourceNotificationSubscription subscription = getOrCreateSubscription(requestUser, body);
        log.info("구독을 활성화합니다.");
        subscription.setIsActivated(true);
    }

    @Transactional
    // Controller가 사용할 메소드
    // 해당 구독 정보에 해당하는 구독을 inactivated 시킴
    public void unsubscribe(SimpleKhumuUserDto requestUser, ResourceNotificationSubscription body) throws WrongResourceKindException, UnauthenticatedException {
        if (requestUser == null) {
            throw new UnauthenticatedException();
        }
        ResourceNotificationSubscription subscription = getOrCreateSubscription(requestUser, body);
        log.info("구독을 비활성화합니다.");
        subscription.setIsActivated(false);
    }

    @Transactional
    // 해당 구독 정보 1개를 Get
    public ResourceNotificationSubscriptionDto getSubscription(SimpleKhumuUserDto requestUser, String subscriberId, ResourceKind resourceKind, Long resourceId) {
        // 우선 인증 패스
        List<ResourceNotificationSubscription> existingSubscriptions = resourceNotificationSubscriptionRepository.findAllBySubscriberAndResourceKindAndResourceId(subscriberId, resourceKind, resourceId);
        if (existingSubscriptions.isEmpty()) {
            // 사실 내부적으로는 구독 자체가 존재하지 않지만 클라이언트는 그런 내용을 알고 싶지않다.
            // isActivated인지만 궁금.
            // 추후 필요한 정보들을 위해 혹은 디버깅을 위해 요청 정보에 포함된 필드들도 몇 개 전달해줌.
            return ResourceNotificationSubscriptionDto.builder()
                    .subscriber(subscriberId)
                    .isActivated(false)
                    .resourceKind(resourceKind)
                    .resourceId(resourceId)
                    .build();
        } else {
            // 존재하는 구독 중 마지막 것.
            return notificationMapper.toDto(existingSubscriptions.get(existingSubscriptions.size() - 1));
        }
    }

    // user의 해당 리소스에 대한 구독 정보를 조회해주거나
    // 없는 경우 activated된 구독을 생성함.
    // 한 user에 대해 여러 개의 구독 정보가 존재하는 경우
    // 가장 마지막 거 하나만 남기고 나머진 모두 삭제함
    @Transactional
    public ResourceNotificationSubscription getOrCreateSubscription(SimpleKhumuUserDto subscriber, ResourceNotificationSubscription body) throws WrongResourceKindException {
        if (body.getResourceKind() != ResourceKind.article &&
            body.getResourceKind() != ResourceKind.study_article &&
            body.getResourceKind() != ResourceKind.announcement
        ) {
            throw new WrongResourceKindException();
        }
        List<ResourceNotificationSubscription> subscriptions = resourceNotificationSubscriptionRepository.findAllBySubscriberAndResourceKindAndResourceId(subscriber.getUsername(), body.getResourceKind(), body.getResourceId());
        ResourceNotificationSubscription subscription = null;
        ResourceKind resourceKind = body.getResourceKind();
        
        if (subscriptions.isEmpty()) {
            log.info(subscriber.getUsername() + "의 해당 Article에 대한 구독을 생성합니다.");
            subscription = resourceNotificationSubscriptionRepository.save(ResourceNotificationSubscription.builder()
                    .subscriber(subscriber.getUsername())
                    .resourceKind(resourceKind)
                    .resourceId(body.getResourceId())
                    .isActivated(true) // 기본값은 true이다.
                    .build());
        } else {
            log.info("이미 " + subscriber.getUsername() + "가 해당 Article을 구독 중입니다. 새로운 구독 생성 작업을 수행하지 않겠습니다.");
            // 마지막 구독을 allocate
            subscription = subscriptions.get(subscriptions.size() - 1);
            if (subscriptions.size() > 1) {
                log.warn(body.getResourceKind().name() + "(" + body.getResourceId() + ")에 대한 " + subscriber.getUsername() + "의 구독이 이미 2개 이상 존재합니다. 마지막 구독만 남기고 나머지를 삭제합니다.");

                resourceNotificationSubscriptionRepository.deleteAll(subscriptions.subList(0, subscriptions.size() - 1));
            }
        }

        return subscription;
    }
}
