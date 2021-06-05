package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.ArticleNotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleNotificationSubscriptionRepository extends JpaRepository<ArticleNotificationSubscription, Long> {
    @Query("select subscription from ArticleNotificationSubscription  subscription where subscription.article = :articleId and " +
            "subscription.subscriber = :subscriberUsername")
    List<ArticleNotificationSubscription> findAllByArticleIdAndSubscriberUsername(Long articleId, String subscriberUsername);


    @Query("select subscription from ArticleNotificationSubscription  subscription where subscription.article = :articleId")
    List<ArticleNotificationSubscription> findAllByArticleId(Long articleId);
}
