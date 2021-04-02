package com.khumu.alimi.service.push;

import com.khumu.alimi.data.entity.PushSubscription;
import com.khumu.alimi.repository.PushSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@RequiredArgsConstructor
@Service
@Slf4j
public class SubscriptionServiceImpl{
    private final PushSubscriptionRepository psr;

    /**
     * subscription에 row 정보를 담고, subscriptionReq를 참고해 업데이
     * @param subscriptionReq
     * @return
     */
    public PushSubscription createOrUpdateSubscription(PushSubscription subscriptionReq){
        Optional<PushSubscription> subscriptionRow = psr.findById(subscriptionReq.getDeviceToken());
        PushSubscription subscription = null;
        if (subscriptionRow.isPresent()) {
            subscription = subscriptionRow.get();
            log.info(subscription.getDeviceToken().substring(0,
                    Math.min(subscription.getDeviceToken().length(), 10)) + "에 대한 Push 구독이 존재합니다. 업데이트를 실시합니다.");
            subscription.setUser(subscriptionReq.getUser());
        } else {
            subscription = subscriptionReq;
            log.info(subscription.getDeviceToken().substring(0,
                    Math.min(subscription.getDeviceToken().length(), 10)) + "에 대한 Push 구독이 존재하지 않습니다. 새로 생성합니다.");
        }
        subscription = psr.save(subscription);
        return subscription;
    }
}
