package com.khumu.alimi.service;

import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.PushOption;
import com.khumu.alimi.data.entity.PushSubscription;
import com.khumu.alimi.repository.PushOptionRepositoryImpl;
import com.khumu.alimi.repository.PushSubscriptionRepository;
import com.khumu.alimi.repository.PushSubscriptionRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Primary
@RequiredArgsConstructor
@Service
@Slf4j
public class PushService {
    final PushSubscriptionRepositoryImpl pushSubscriptionRepository;
    final PushOptionRepositoryImpl pushOptionRepository;

    @Transactional
    public PushSubscription subscribePush(PushSubscription subscriptionReq){
        PushSubscription subscription = pushSubscriptionRepository.save(subscriptionReq);

        // 만약 인증된 유저라면
        // 디바이스를 등록할 때 만약 이 유저에 대한 PushOption이 존재하지 않으면 생성함.
        if (subscriptionReq.getUser() != null) {
            PushOption pushOption = pushOptionRepository.getOrCreate(PushOption.builder().id(subscriptionReq.getUser()).build());
        }
        return subscription;
    }

    @Transactional
    // 딱히 Dto를 사용할 것도 없네.
    public PushOption getPushOption(SimpleKhumuUserDto requestUser, String username) {
        PushOption option = pushOptionRepository.getOrCreate(PushOption.builder().id(username).build());
        return option;
    }

    @Transactional
    // 어떤 종류의 알림들을 push로 받을 것인지
    public PushOption updatePushOption(SimpleKhumuUserDto user, PushOption body) {
        // persistent한 PushOption instance를 리턴받아야 제대로 값이 자동 update 된다.
        PushOption option = pushOptionRepository.getOrCreate(PushOption.builder().id(user.getUsername()).build());
        option.setIsAnnouncementNotificationActivated(body.getIsAnnouncementNotificationActivated());
        option.setIsCommentNotificationActivated(body.getIsCommentNotificationActivated());
        option.setIsKhumuNotificationActivated(body.getIsKhumuNotificationActivated());

        return option;
    }
}
