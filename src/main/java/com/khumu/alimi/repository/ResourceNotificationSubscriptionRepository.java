package com.khumu.alimi.repository;

import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceNotificationSubscriptionRepository extends JpaRepository<ResourceNotificationSubscription, Long> {
    List<ResourceNotificationSubscription> findAllByResourceKindAndResourceId(ResourceKind resourceKind, Long resourceId);
    List<ResourceNotificationSubscription> findAllByResourceKindAndResourceIdAndSubscriber(ResourceKind resourceKind, Long resourceId, String subscriberId);
}
