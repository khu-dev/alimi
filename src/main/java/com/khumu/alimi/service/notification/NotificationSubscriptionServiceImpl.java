package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.entity.ArticleNotificationSubscription;
import com.khumu.alimi.repository.ArticleNotificationSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 무언가에 대한 알림 사항을 받고자 구독하는 것 관련.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationSubscriptionServiceImpl {
    final ArticleNotificationSubscriptionRepository articleNotificationSubscriptionRepository;

    @Transactional
    public ArticleNotificationSubscription createOrUpdateArticleNotificationSubscription(ArticleNotificationSubscription subscription) {
        List<ArticleNotificationSubscription> myExistingSubscriptions = articleNotificationSubscriptionRepository.findAllByArticleIdAndSubscriberUsername(
                subscription.getArticle().getId(), subscription.getSubscriber().getUsername());

        // 없으면 만듦.
        if (myExistingSubscriptions.isEmpty()) {
            return articleNotificationSubscriptionRepository.save(subscription);
        }

        // 존재하던 구독이 많으면 첫 번째꺼 하나만 남김
        for (int i = 1; i < myExistingSubscriptions.size(); i++) {
            articleNotificationSubscriptionRepository.delete(myExistingSubscriptions.get(i));
        }

        // 존재하던 것 중 남겨진 첫 번째꺼의 is_activated를 요청온 대로 수정
        ArticleNotificationSubscription existingSubscription = myExistingSubscriptions.get(0);
        existingSubscription.setIsActivated(subscription.getIsActivated());

        return existingSubscription;
    }

    /**
     * 구독 정보 자체가 존재하지 않는 경우에만 구독을 생성하고
     * 만약 구독 상태가 is_activated=false이면 아무 작업 안함.
     * @param subscription
     * @return
     */
    @Transactional
    public ArticleNotificationSubscription createSubscriptionIfNotExists(ArticleNotificationSubscription subscription) {
        List<ArticleNotificationSubscription> myExistingSubscriptions = articleNotificationSubscriptionRepository.findAllByArticleIdAndSubscriberUsername(
                subscription.getArticle().getId(), subscription.getSubscriber().getUsername());
        if (myExistingSubscriptions.isEmpty()) {
            log.info(subscription.getArticle() + " 번 게시물에 대한 " + subscription.getSubscriber().getUsername() + " 유저의 게시글 알림 구독을 생성합니다.");
            return articleNotificationSubscriptionRepository.save(subscription);
        }

        log.info(subscription.getArticle() + " 번 게시물에 대한 " + subscription.getSubscriber().getUsername() + " 유저의 게시글 알림 구독이 존재하므로 새로 생성하지 않습니다.");
        return myExistingSubscriptions.get(0);
    }
}
