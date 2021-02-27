package com.khumu.alimi.repository.push;

import com.khumu.alimi.data.PushSubscription;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Push subscription은 간단하니까 Jpa에 바로 기능 추가.
 */
@Primary
@Repository
public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, String> {
    @Query("select s from PushSubscription s where s.user.username=:username")
    List<PushSubscription> listByUsername(@Param("username") String username);
}
