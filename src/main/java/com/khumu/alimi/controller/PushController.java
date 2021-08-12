package com.khumu.alimi.controller;

import com.khumu.alimi.data.entity.PushOption;
import com.khumu.alimi.data.entity.PushDevice;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.PushOptionKind;
import com.khumu.alimi.service.PushService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class PushController {
    final PushService pushService;

    @PostMapping(value="/api/push/subscribe")
    @ResponseBody
    public ResponseEntity<DefaultResponse<PushDevice>> createPushDevice(
            @AuthenticationPrincipal SimpleKhumuUserDto user,
        @RequestBody PushDevice body) {
        if (user != null) {
            body.setUser(user.getUsername());
        }

        log.info(user + " 유저에 대한 푸시 디바이스를 등록해 푸시 알림을 구독합니다.");
        PushDevice newSubscription = pushService.subscribePush(body);
        return new ResponseEntity<>(new DefaultResponse<>(
                null, newSubscription), HttpStatus.OK);
    }

    @GetMapping(value="/api/push/options/{userId}")
    @ResponseBody
    public DefaultResponse<Map<PushOptionKind, PushOption>> getPushOption(
            @AuthenticationPrincipal SimpleKhumuUserDto user,
            @PathVariable  String userId
        ) {
        log.info(user + " 유저에 대한 푸시 옵션들을 모두 조회합니다.");
        Map<PushOptionKind, PushOption> optionInfo = pushService.getPushOption(user, userId);
        return new DefaultResponse<Map<PushOptionKind, PushOption>>(null, optionInfo);
    }

    @PatchMapping(value="/api/push/options/{optionId}")
    @ResponseBody
    public DefaultResponse<PushOption> updatePushOption(
            @AuthenticationPrincipal SimpleKhumuUserDto user,
            @PathVariable  Long optionId,
            @RequestBody PushOption body
    ) {
        log.info(user + " 유저에 대한 푸시 옵션을 수정합니다.");
        body.setId(optionId);
        PushOption option = pushService.updatePushOption(user, body);
        return new DefaultResponse<PushOption>(null, option);
    }
}
