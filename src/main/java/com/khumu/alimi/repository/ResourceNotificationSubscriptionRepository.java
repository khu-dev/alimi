package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceNotificationSubscriptionRepository extends JpaRepository<ResourceNotificationSubscription, Long> {
    List<ResourceNotificationSubscription> findAllByArticle(Long article);
    List<ResourceNotificationSubscription> findAllByArticleAndSubscriber(Long article, String subscriber);

    List<ResourceNotificationSubscription> findAllByStudyArticle(Long article);
    List<ResourceNotificationSubscription> findAllByStudyArticleAndSubscriber(Long article, String subscriber);

    List<ResourceNotificationSubscription> findAllByAnnouncement(Long article);
    List<ResourceNotificationSubscription> findAllByAnnouncementAndSubscriber(Long article, String subscriber);
}
