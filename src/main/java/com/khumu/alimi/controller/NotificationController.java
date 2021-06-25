package com.khumu.alimi.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.NotificationDto;
import com.khumu.alimi.data.dto.ResourceNotificationSubscriptionDto;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import com.khumu.alimi.service.notification.NotificationService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@RestController
public class NotificationController {
    final NotificationService notificationService;

    @RequestMapping(value="/ping", method=RequestMethod.GET)
    public String ping() {
        return "pong";
    }

    @RequestMapping(value="/api/notifications", method=RequestMethod.GET)
    @ResponseBody
    public DefaultResponse<List<NotificationDto>> list(
            @RequestParam(value="recipient", required = false) String recipientUsername,
            @PageableDefault(page=0, size=30) Pageable pageable) {
        List<NotificationDto> notifications = null;
        if (recipientUsername != null) {
            notifications = notificationService.listNotificationsByUsername(recipientUsername, pageable);
        } else {
            notifications = notificationService.listNotifications(pageable);
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

    @PostMapping(value = "/api/subscribe")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public DefaultResponse<Object> subscribeResource(@AuthenticationPrincipal SimpleKhumuUserDto user, @RequestBody ResourceNotificationSubscription body) throws Exception {
        notificationService.subscribe(user, body);
        return new DefaultResponse<>(null, null);
    }

    // 내부적인 API
    @GetMapping(value = "/api/subscriptions/{username}/{resourceKind}/{resourceId}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public DefaultResponse<Object> subscribeResource(
            @AuthenticationPrincipal SimpleKhumuUserDto user,
            @PathVariable String username,
            @PathVariable ResourceKind resourceKind,
            @PathVariable Long resourceId) throws Exception {
        ResourceNotificationSubscriptionDto subscriptionDto = notificationService.getSubscription(user, username, resourceKind, resourceId);
        return new DefaultResponse<>(null, subscriptionDto);
    }

    @DeleteMapping(value = "/api/subscribe")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public DefaultResponse<Object> unsubscribeResource(@AuthenticationPrincipal SimpleKhumuUserDto user, @RequestBody ResourceNotificationSubscription body) throws Exception {
        notificationService.unsubscribe(user, body);
        return new DefaultResponse<>(null, null);
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
