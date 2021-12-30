package com.khumu.alimi.service;

import com.khumu.alimi.data.dto.KhumuUserDto;
import com.khumu.alimi.data.entity.PushDevice;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import com.khumu.alimi.repository.NotificationRepository;
import com.khumu.alimi.repository.PushDeviceRepository;
import com.khumu.alimi.repository.ResourceNotificationSubscriptionRepository;
import com.khumu.alimi.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 게시물 관련된 이벤트가 발생했을 때 무슨 작업을 수행할 것인지.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteUserService {
    private final ResourceNotificationSubscriptionRepository resourceNotificationSubscriptionRepository;
    private final PushDeviceRepository pushDeviceRepository;

    @Transactional
    // 유저와 관련된 정보들을 삭제합니다.
    public void delete(KhumuUserDto user) {
        log.info(user + "와 관련된 정보를 삭제합니다.");
        if (user == null || user.getUsername() == null) {
            log.error("유저 정보가 존재하지 않습니다. " + user);
            throw new RuntimeException("유저 정보가 존재하지 않음");
        } else {
            List<PushDevice> devices = pushDeviceRepository.findAllByUser(user.getUsername());
            for (PushDevice device : devices) {
                // @Transactional 있어야 함.
                device.deleteUserInfo();
            }

            List<ResourceNotificationSubscription> subscriptions = resourceNotificationSubscriptionRepository.findAllBySubscriber(user.getUsername());
            resourceNotificationSubscriptionRepository.deleteAll(subscriptions);
        }
    }
}