// 실제 Notification을 보내거나 생성하는 것 자체에 대한 서비스
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    @Transactional
    // Controller가 사용할 메소드
    // 해당 구독 정보에 해당하는 구독을 생성하거나 존재하는 경우 기존 구독 정보를 activated로 설정함.
    // 존재하지 않으면 activated 상태로 생성한다.
    public void subscribe(SimpleKhumuUserDto requestUser, ResourceNotificationSubscription body) throws WrongResourceKindException, UnauthenticatedException {
        if (requestUser == null) {
            throw new UnauthenticatedException();
        }
        throwOnNullResourceKind(body);

        body.setSubscriber(requestUser.getUsername());
        ResourceNotificationSubscription subscription = resourceNotificationSubscriptionRepository.getOrCreate(body);
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
        throwOnNullResourceKind(body);

        body.setSubscriber(requestUser.getUsername());
        ResourceNotificationSubscription subscription = resourceNotificationSubscriptionRepository.getOrCreate(body);
        log.info("구독을 비활성화합니다.");
        subscription.setIsActivated(false);
    }

    @Transactional
    // subscription에 해당하는 구독 정보 1개를 Get하거나 만든다. 혹은 오류적으로 1개 이상의 구독 정보가 존재하는 경우 나머지를 삭제함.
    // 사실상 isActivated을 조회하기 위함.
    public ResourceNotificationSubscriptionDto getSubscription(SimpleKhumuUserDto requestUser, ResourceNotificationSubscription subscription) {
        // 우선 인증 패스했다고 가정
        subscription = resourceNotificationSubscriptionRepository.getOrCreate(subscription);
        return notificationMapper.toDto(subscription);
    }

    private void throwWhenNotRecipient(SimpleKhumuUserDto requestUser, Notification notification) throws NoPermissionException {
        if (requestUser == null || requestUser.getUsername() == null || !requestUser.getUsername().equals(notification.getRecipient())) {
            throw new NoPermissionException("본인의 알림이 아니라 해당 작업을 수행할 권한이 없습니다.");
        }
    }

    public void throwOnNullResourceKind(ResourceNotificationSubscription subscription) throws WrongResourceKindException {
        if (subscription.getResourceKind() != ResourceKind.article &&
                subscription.getResourceKind() != ResourceKind.study_article &&
                subscription.getResourceKind() != ResourceKind.announcement
        ) {
            throw new KhumuException.WrongResourceKindException();
        }
    }
}
