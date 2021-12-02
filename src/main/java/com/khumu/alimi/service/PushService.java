package com.khumu.alimi.service;

import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.PushOption;
import com.khumu.alimi.data.entity.PushDevice;
import com.khumu.alimi.data.entity.PushOptionKind;
import com.khumu.alimi.repository.CustomPushOptionRepository;
import com.khumu.alimi.repository.CustomPushDeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Primary
@RequiredArgsConstructor
@Service
@Slf4j
public class PushService {
    final CustomPushDeviceRepository pushDeviceRepository;
    final CustomPushOptionRepository pushOptionRepository;

    @Transactional
    public PushDevice subscribePush(PushDevice subscriptionReq){
        PushDevice subscription = pushDeviceRepository.save(subscriptionReq);

        // 만약 인증된 유저라면
        // 디바이스를 등록할 때 만약 이 유저에 대한 PushOption이 존재하지 않으면 생성함.
        if (subscriptionReq.getUser() != null) {
            for (PushOptionKind kind : PushOptionKind.values()) {
                PushOption pushOption = pushOptionRepository.getOrCreate(PushOption.builder().username(subscriptionReq.getUser()).pushOptionKind(kind).build());
            }
        }
        return subscription;
    }

    @Transactional
    public List<PushDevice> unsubscribe(String username) {
        List<PushDevice> devices = pushDeviceRepository.findAllByUser(username);
        pushDeviceRepository.deleteAll(devices);
        log.info(username + "의 Device 정보들을 삭제합니다.");

        return devices;
    }

    @Transactional
    // 딱히 Dto를 사용할 것도 없네.
    public Map<PushOptionKind, PushOption> getPushOption(SimpleKhumuUserDto requestUser, String username) {
        Map<PushOptionKind, PushOption> pushOptionInfo = new HashMap<>();
        for (PushOptionKind kind : PushOptionKind.values()) {
            pushOptionInfo.put(kind, pushOptionRepository.getOrCreate(PushOption.builder().username(username).pushOptionKind(kind).build()));
        }

        return pushOptionInfo;
    }

    @Transactional
    // 어떤 종류의 알림들을 push로 받을 것인지
    public PushOption updatePushOption(SimpleKhumuUserDto user, PushOption body) {
        // persistent한 PushOption instance를 리턴받아야 제대로 값이 자동 update 된다.
        PushOption option = pushOptionRepository.findById(body.getId()).get();
        option.setIsActivated(body.getIsActivated());
        return option;
    }
}
