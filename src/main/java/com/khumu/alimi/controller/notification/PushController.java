package com.khumu.alimi.controller.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.khumu.alimi.controller.DefaultResponse;
import com.khumu.alimi.data.Notification;
import com.khumu.alimi.data.PushSubscription;
import com.khumu.alimi.data.SimpleKhumuUser;
import com.khumu.alimi.repository.push.PushSubscriptionRepository;
import com.khumu.alimi.service.auth.JwtServiceImpl;
import com.khumu.alimi.service.notification.NotificationService;
import com.khumu.alimi.service.push.PushNotificationService;
import lombok.*;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PushController {
    private final PushSubscriptionRepository psRepository;
    private final PushNotificationService pushNotificationService;
    private final JwtServiceImpl jwtService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value="/api/push-subscriptions", method= RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<DefaultResponse<PushSubscription>> createPushSubscription(@RequestHeader(name="Authorization", required=true) String auth,
        @RequestBody PushSubscription body) {
        logger.info("Authorization header: ", auth);
        String requestUsername = jwtService.getUsernameFromAuthHeader(auth);
        if (requestUsername == null || requestUsername.equals("")) {
            return new ResponseEntity<>(new DefaultResponse<>(
                    "token에서 올바른 username을 찾을 수 없습니다.", null), HttpStatus.UNAUTHORIZED);
        } else{
            body.setUser(new SimpleKhumuUser(requestUsername));
            PushSubscription newSubscription = psRepository.save(body);
            return new ResponseEntity<>(new DefaultResponse<>(
                    null, newSubscription), HttpStatus.OK);
        }
    }
}
