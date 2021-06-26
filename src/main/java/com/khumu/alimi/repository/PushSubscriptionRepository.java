package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.PushSubscription;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Push subscription은 간단하니까 Jpa에 바로 기능 추가.
 */
public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, String> {
    List<PushSubscription> findAllByUser(String user);
}
