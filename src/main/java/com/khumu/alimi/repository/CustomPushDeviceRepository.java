package com.khumu.alimi.repository;

import com.khumu.alimi.data.entity.PushDevice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * Push subscription은 간단하니까 Jpa에 바로 기능 추가.
 */
@Slf4j
@Repository
public class CustomPushDeviceRepository {
    @Autowired
    @Lazy
    PushDeviceRepository pushDeviceRepository;
    @Autowired
    EntityManager em;

    String name = "zz";
    public PushDevice getOrCreate(PushDevice subscription) {
        Optional<PushDevice> subscriptionRow = pushDeviceRepository.findById(subscription.getDeviceToken());
        PushDevice result = null;
        if (subscriptionRow.isPresent()) {
            subscription = subscriptionRow.get();
            log.info(subscription.getDeviceToken().substring(0,
                    Math.min(subscription.getDeviceToken().length(), 10)) + "에 대한 Push 구독이 존재합니다. 업데이트를 실시합니다.");
            // 같은 device에 대한 다른 유저
            subscription.setUser(subscription.getUser());
        } else {
            subscription = pushDeviceRepository.save(subscription);
            log.info(subscription.getDeviceToken().substring(0,
                    Math.min(subscription.getDeviceToken().length(), 10)) + "에 대한 Push 구독이 존재하지 않습니다. 새로 생성합니다.");
        }

        return subscription;
    }

    public List<PushDevice> findAllByUser(String username) {
        return pushDeviceRepository.findAllByUser(username);
//        return pushDeviceRepository.findAll();
    }

    public PushDevice save(PushDevice subscription) {
        return pushDeviceRepository.save(subscription);
    }
}
