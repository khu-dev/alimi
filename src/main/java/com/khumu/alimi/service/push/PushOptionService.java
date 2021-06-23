package com.khumu.alimi.service.push;

import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.PushOption;
import com.khumu.alimi.data.entity.PushSubscription;
import com.khumu.alimi.repository.PushOptionRepository;
import com.khumu.alimi.repository.PushSubscriptionRepository;
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
public class PushOptionService {
    final PushSubscriptionRepository psr;
    final PushOptionRepository pushOptionRepository;

    @Transactional
    // 어떤 푸시 알림을 받을 지에 대한 개개인의 설정
    // persistent한 PushOption instance를 리턴
    public PushOption getOrCreateOption(String username){
        Optional<PushOption> optionalPushOption = pushOptionRepository.findById(username);
        if (optionalPushOption.isPresent()) {
            return optionalPushOption.get();
        } else {
            PushOption option = pushOptionRepository.save(PushOption.builder()
                    .id(username)
                    // 기본적으로 알림 설정은 다 activated
                    .build()
            );
            return option;
        }
    }

    @Transactional
    // 딱히 Dto를 사용할 것도 없네.
    public PushOption getPushOption(SimpleKhumuUserDto requestUser, String username) {
        PushOption option = getOrCreateOption(username);
        return option;
    }

    @Transactional
    public PushOption updatePushOption(SimpleKhumuUserDto user, PushOption body) {
        // persistent한 PushOption instance를 리턴받아야 제대로 값이 자동 update 된다.
        PushOption option = getOrCreateOption(user.getUsername());
        option.setIsAnnouncementNotificationActivated(body.getIsAnnouncementNotificationActivated());
        option.setIsCommentNotificationActivated(body.getIsCommentNotificationActivated());
        option.setIsKhumuNotificationActivated(body.getIsKhumuNotificationActivated());

        return option;
    }
}
