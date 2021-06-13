package com.khumu.alimi.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import com.khumu.alimi.service.notification.NotificationServiceImpl;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class NotificationController {
    final NotificationServiceImpl notificationService;

    @RequestMapping(value="/ping", method=RequestMethod.GET)
    public String ping() {
        return "pong";
    }

    @RequestMapping(value="/api/notifications", method=RequestMethod.GET)
    @ResponseBody
    public DefaultResponse<List<Notification>> list(@RequestParam(value="recipient", required = false) String recipientUsername) {
        List<Notification> notifications = null;
        if (recipientUsername != null) {
            notifications = notificationService.listNotificationsByUsername(recipientUsername);
        } else {
            notifications = notificationService.listNotifications();
        }
        System.out.println(recipientUsername);

        return new DefaultResponse<>(null, notifications);
    }

    @RequestMapping(value = "/api/notifications/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    public DefaultResponse<Object> update(@PathVariable Long id, @RequestBody NotificationUpdateBody body) {
        log.info(body.toString());

        System.out.println(body.isRead());
        System.out.println(id);
        if (body.isRead()) {
            notificationService.read(id);
        } else {
            log.warn("Nothing to update");
        }
        return new DefaultResponse<>("Notification을 수정했습니다.", null);
    }

    @RequestMapping(value = "/api/subscriptions", method = RequestMethod.POST)
    @ResponseBody
    public DefaultResponse<Object> subscribeResource(@AuthenticationPrincipal SimpleKhumuUserDto user, @RequestBody ResourceNotificationSubscription body) throws Exception {

        ResourceNotificationSubscription resourceNotificationSubscription = notificationService.subscribeResource(user, body);

        return new DefaultResponse<>(null, resourceNotificationSubscription);
    }



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationUpdateBody{
        @JsonProperty("is_read")
        private boolean isRead;
    }

}
