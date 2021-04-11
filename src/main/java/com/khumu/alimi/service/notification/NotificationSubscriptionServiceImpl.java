package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.entity.ArticleNotificationSubscription;
import com.khumu.alimi.repository.ArticleNotificationSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class NotificationSubscriptionServiceImpl {
    final ArticleNotificationSubscriptionRepository articleNotificationSubscriptionRepository;

    @Transactional
    public ArticleNotificationSubscription subscribeOrUpdateArticle(ArticleNotificationSubscription subscription) {
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

        // 첫 번째꺼의 is_activated를 요청온 대로 수정
        ArticleNotificationSubscription existingSubscription = myExistingSubscriptions.get(0);
        existingSubscription.setIsActivated(subscription.getIsActivated());

        return existingSubscription;
    }
}
