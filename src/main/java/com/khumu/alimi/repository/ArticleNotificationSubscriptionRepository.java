package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.Article;
import com.khumu.alimi.data.entity.ArticleNotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleNotificationSubscriptionRepository extends JpaRepository<ArticleNotificationSubscription, Long> {
    @Query("select subscription from ArticleNotificationSubscription  subscription where subscription.article.id = :articleId and " +
            "subscription.subscriber.username = :subscriberUsername")
    List<ArticleNotificationSubscription> findAllByArticleIdAndSubscriberUsername(Long articleId, String subscriberUsername);
}
