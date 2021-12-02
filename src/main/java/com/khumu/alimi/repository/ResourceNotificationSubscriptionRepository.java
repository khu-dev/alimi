package com.khumu.alimi.repository;

import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import com.khumu.alimi.service.KhumuException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ResourceNotificationSubscriptionRepository extends JpaRepository<ResourceNotificationSubscription, Long> {
    List<ResourceNotificationSubscription> findAllByResourceKindAndResourceId(ResourceKind resourceKind, Long resourceId);
    List<ResourceNotificationSubscription> findAllBySubscriberAndResourceKindAndResourceId(String subscriberId, ResourceKind resourceKind, Long resourceId);

    List<ResourceNotificationSubscription> findAllBySubscriber(String subscriberId);

    // 해당 유저의 해당 ResourceKind의 ResourceNotificationSubscription을 조회하거나 만들고 조회함.
    default ResourceNotificationSubscription getOrCreate(ResourceNotificationSubscription subscription){
        // user의 해당 리소스에 대한 구독 정보를 조회해주거나
        // 없는 경우 activated된 구독을 생성함.
        // 한 user에 대해 여러 개의 구독 정보가 존재하는 경우
        // 가장 마지막 거 하나만 남기고 나머진 모두 삭제함
        List<ResourceNotificationSubscription> subscriptions = this.findAllBySubscriberAndResourceKindAndResourceId(subscription.getSubscriber(), subscription.getResourceKind(), subscription.getResourceId());
        if (subscriptions.isEmpty()) {
            System.out.println(subscription.getSubscriber() + "의 해당 Article에 대한 구독을 생성합니다.");
            // 기본적으로 활성화시킴
            subscription.setIsActivated(true);
            this.save(subscription);
        } else {
            System.out.println("이미 " + subscription.getSubscriber() + "가 해당 Article을 구독 중입니다. 새로운 구독 생성 작업을 수행하지 않겠습니다.");
            // 마지막 구독을 할당하고 나머지는 삭제
            subscription = subscriptions.get(subscriptions.size() - 1);
            if (subscriptions.size() > 1) {
                System.out.println(subscription.getResourceKind().name() + "(" + subscription.getResourceId() + ")에 대한 " + subscription.getSubscriber() + "의 구독이 이미 2개 이상 존재합니다. 마지막 구독만 남기고 나머지를 삭제합니다.");
                this.deleteAll(subscriptions.subList(0, subscriptions.size() - 1));
            }
        }

        return subscription;
    }
}
