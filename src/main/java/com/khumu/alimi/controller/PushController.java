package com.khumu.alimi.controller;

import com.khumu.alimi.data.entity.PushSubscription;
import com.khumu.alimi.data.entity.SimpleKhumuUser;
import com.khumu.alimi.repository.PushSubscriptionRepository;
import com.khumu.alimi.service.auth.FakeUserDetailsServiceImpl;
import com.khumu.alimi.service.push.PushNotificationService;
import com.khumu.alimi.service.push.SubscriptionServiceImpl;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
public class PushController {
    private final PushSubscriptionRepository psRepository;
    private final PushNotificationService pushNotificationService;
    private final SubscriptionServiceImpl subscriptionService;
    private final FakeUserDetailsServiceImpl jwtService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value="/api/push-subscriptions", method= RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<DefaultResponse<PushSubscription>> createPushSubscription(
            @AuthenticationPrincipal SimpleKhumuUser user,
        @RequestBody PushSubscription body) {
        if (user != null) {
            body.setUser(user);
        }

        logger.info(user + " 유저에 대한 푸시 등록을 생성하거나 수정합니다.");
        PushSubscription newSubscription = subscriptionService.createOrUpdateSubscription(body);
        return new ResponseEntity<>(new DefaultResponse<>(
                null, newSubscription), HttpStatus.OK);
        }




}
