package com.khumu.alimi.service;

import com.google.gson.Gson;
import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.CommentDto;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.entity.*;
import com.khumu.alimi.external.push.PushManager;
import com.khumu.alimi.repository.*;
import com.khumu.alimi.service.notification.NotificationService;
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
public class CommentService {
    final NotificationRepository notificationRepository;
    final CustomPushDeviceRepository pushDeviceRepository;
    final CustomPushOptionRepository pushOptionRepository;
    final NotificationService notificationService;
    final ResourceNotificationSubscriptionRepository resourceNotificationSubscriptionRepository;
    final PushManager pushManager;
    final Gson gson;

    @Transactional
    public List<Notification> createNotificationsForNewComment(CommentDto commentDto) throws PushManager.PushException {
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

            List<PushDevice> devices = pushDeviceRepository.findAllByUser(recipientId);
            for (PushDevice device : devices) {
                try {
                    log.info("푸시를 보냅니다. " + n.getRecipient());
                    pushManager.notify(n, device.getDeviceToken());
                } catch (PushManager.ExpiredPushTokenException e) {
                    log.warn("더 이상 존재하지 않는 device tokne이므로 삭제합니다." + device.getDeviceToken());
                    pushDeviceRepository.delete(device);
                }
            }
            results.add(n);
        }
        return results;
    }

    // 댓글 생성 생성에 대한 recipient 찾기
    @Transactional
    public List<String> getRecipientIds(CommentDto commentDto) {
        // 현재 댓글의 게시글에 대한 구독 정보
        List<ResourceNotificationSubscription> subscriptions = resourceNotificationSubscriptionRepository.findAllByResourceKindAndResourceId(ResourceKind.article, commentDto.getArticle());

        // 구독자 중 댓글 작성자 본인은 빼고 recipient로 filter
        return subscriptions.stream().filter(subscription -> !subscription.getSubscriber().equals(commentDto.getAuthor().getUsername()))
                .map(subscription -> subscription.getSubscriber()).collect(Collectors.toList());
    }

    @Transactional
    // SqsListener가 Comment event에 반응하여 사용할 메소드
    // CommentEvenetMessageService는 NotificationService에 의존한다.
    // 새로 댓글이 작성된 경우 새 댓글의 작성자는 그 댓글의 게시글을 subscribe한다.
    public void subscribeArticle(CommentDto commentDto) throws WrongResourceKindException {
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
            log.error("알림 구독 생성이 실패했습니다. " + commentDto);
            e.printStackTrace();
        }
    }
}