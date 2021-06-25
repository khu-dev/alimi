package com.khumu.alimi.service.notification;

import com.google.gson.Gson;
import com.khumu.alimi.data.EventKind;
import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.CommentDto;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.*;
import com.khumu.alimi.data.resource.ArticleResource;
import com.khumu.alimi.external.push.PushManager;
import com.khumu.alimi.repository.*;
import com.khumu.alimi.service.KhumuException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.khumu.alimi.service.KhumuException.*;

/**
 * Comment 관련된 Event message의 기능을 담당.
 * 예를 들어 댓글이 생성되었다는 Event가 발생했을 때 무엇을 할 것인지.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommentEventMessageService {
    final NotificationRepository notificationRepository;
    final PushSubscriptionRepository pushSubscriptionRepository;
    final NotificationService notificationService;
    final ResourceNotificationSubscriptionRepository resourceNotificationSubscriptionRepository;
    final PushManager pushManager;
    final Gson gson;

    @Transactional
    public List<Notification> createNotifications(EventMessageDto<CommentDto> e) {
        List<Notification> notifications = new ArrayList<>();
        switch (e.getResourceKind()) {
            case comment:
                CommentDto commentDto = e.getResource();
                switch (e.getEventKind()) {
                    case create:
                        notifications = createNotificationsForNewComment(commentDto);
                        break;
                }
                break;
        }

        return notifications;
    }

    @Transactional
    public List<Notification> createNotificationsForNewComment(CommentDto commentDto) {
        List<Notification> results = new ArrayList<>();
        List<String> recipientIds = this.getRecipientIds(commentDto);
        log.info("" + recipientIds);

        for (String recipientId : recipientIds) {
            Notification tmp = Notification.builder()
                    .recipient(recipientId)
                    .title("새로운 댓글이 생성되었습니다.")
                    .content(commentDto.getContent())
                    .kind("커뮤니티")
                    .reference("articles/" + commentDto.getArticle())
                    .build();

            Notification n = notificationRepository.save(tmp);

            List<PushSubscription> subscriptions = pushSubscriptionRepository.listByUsername(recipientId);
            for (PushSubscription subscription : subscriptions) {
                pushManager.notify(n, subscription.getDeviceToken());
                log.info("푸시를 보냅니다. " + subscription.getUser());
            }
            results.add(n);
        }
        return results;
    }

    // 댓글 생성 생성에 대한 recipient 찾기
    @Transactional
    public List<String> getRecipientIds(CommentDto commentDto) {
        List<ResourceNotificationSubscription> subscriptions = resourceNotificationSubscriptionRepository.findAllByArticle(commentDto.getArticle());

        return subscriptions.stream().filter(subscription -> {
                // 현 댓글 작성자는 알림을 보내지 않는다.
                return !subscription.getSubscriber().equals(commentDto.getAuthor().getUsername());
            }).map(subscription -> subscription.getSubscriber()).collect(Collectors.toList());
    }

    @Transactional
    // SqsListener가 Comment event에 반응하여 사용할 메소드
    // CommentEvenetMessageService는 NotificationService에 의존한다.
    public void createArticleNotificationSubscriptionForCommentAuthor(EventMessageDto<CommentDto> eventMessageDto) throws WrongResourceKindException {
        CommentDto commentDto = eventMessageDto.getResource();
        ResourceKind resourceKind = null;
        Long resourceId = null;
        if (commentDto.getArticle() != null) {
            resourceKind = ResourceKind.article;
            resourceId = commentDto.getArticle();
        } else if (commentDto.getStudyArticle() != null) {
            resourceKind = ResourceKind.study_article;
            resourceId = commentDto.getStudyArticle();
        } else{
            throw new WrongResourceKindException();
        }

        try {
            notificationService.subscribe(commentDto.getAuthor(), ResourceNotificationSubscription.builder()
                    .resourceKind(resourceKind)
                    .resourceId(resourceId)
                    .build());
        } catch (Exception e) {
            log.error("Event message에 의한 알림 구독 생성이 실패했습니다. " + eventMessageDto);
            e.printStackTrace();
        }
    }
}