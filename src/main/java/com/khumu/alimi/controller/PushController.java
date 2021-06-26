package com.khumu.alimi.controller;

import com.khumu.alimi.data.entity.PushOption;
import com.khumu.alimi.data.entity.PushSubscription;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.service.PushService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
public class PushController {
    final PushService pushService;

    @PatchMapping(value="/api/push-subscriptions")
    @ResponseBody
    public ResponseEntity<DefaultResponse<PushSubscription>> createPushSubscription(
            @AuthenticationPrincipal SimpleKhumuUserDto user,
        @RequestBody PushSubscription body) {
        if (user != null) {
            body.setUser(user.getUsername());
        }

        log.info(user + " 유저에 대한 푸시 디바이스를 등록해 푸시 알림을 구독합니다.");
        PushSubscription newSubscription = pushService.subscribePush(body);
        return new ResponseEntity<>(new DefaultResponse<>(
                null, newSubscription), HttpStatus.OK);
    }

    @GetMapping(value="/api/push-options/{userId}")
    @ResponseBody
    public DefaultResponse<PushOption> getPushOption(
            @AuthenticationPrincipal SimpleKhumuUserDto user,
            @PathVariable  String userId
        ) {
        log.info(user + " 유저에 대한 푸시 옵션을 조회합니다.");
        PushOption option = pushService.getPushOption(user, userId);
        return new DefaultResponse<PushOption>(null, option);
    }

    @PutMapping(value="/api/push-options/{userId}")
    @ResponseBody
    public DefaultResponse<PushOption> updatePushOption(
            @AuthenticationPrincipal SimpleKhumuUserDto user,
            @PathVariable  String userId,
            @RequestBody PushOption body
    ) {
        log.info(user + " 유저에 대한 푸시 옵션을 수정합니다.");
        body.setId(userId);
        PushOption option = pushService.updatePushOption(user, body);
        return new DefaultResponse<PushOption>(null, option);
    }
}
