package com.khumu.alimi.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.*;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import com.khumu.alimi.service.NotifyAnnouncementCrawledService;
import com.khumu.alimi.service.notification.NotificationService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.khumu.alimi.service.KhumuException.*;

@RequiredArgsConstructor
@Slf4j
@RestController
public class NotificationController {
    private final NotificationService notificationService;
    private final NotifyAnnouncementCrawledService announcementEventService;

    @RequestMapping(value="/ping", method=RequestMethod.GET)
    public String ping() {
        return "pong";
    }

    @RequestMapping(value="/api/notifications", method=RequestMethod.GET)
    @ResponseBody
    public DefaultResponse<List<NotificationDto>> list(
            @AuthenticationPrincipal SimpleKhumuUserDto user,
            @RequestParam(value="recipient", required = false) String recipientUsername,
            @PageableDefault(page=0, size=30) Pageable pageable) throws NoPermissionException, UnauthenticatedException {
        List<NotificationDto> notifications = null;
        if (recipientUsername != null) {
            notifications = notificationService.listNotificationsByUsername(user, recipientUsername, pageable);
        } else {
            notifications = notificationService.listNotifications(pageable);
        }
        System.out.println(recipientUsername);

        return new DefaultResponse<>(null, notifications);
    }

    @PatchMapping(value = "/api/notifications/all/read")
    @ResponseBody
    public DefaultResponse<Object> readAll(@AuthenticationPrincipal SimpleKhumuUserDto user) throws UnauthenticatedException {
        notificationService.readAll(user);
        return new DefaultResponse<>("jinsu" + "의 모든 Notifications를 읽음 처리 했습니다.", null);
    }

    @PatchMapping(value = "/api/notifications/{id}/read")
    @ResponseBody
    public DefaultResponse<Object> read(@AuthenticationPrincipal SimpleKhumuUserDto user, @PathVariable Long id) throws NoPermissionException {
        notificationService.read(user, id);
        return new DefaultResponse<>("Notification(id=" + id + ")를 읽음 처리 했습니다.", null);
    }

    @PatchMapping(value = "/api/notifications/all/unread")
    @ResponseBody
    public DefaultResponse<Object> unreadAll(@AuthenticationPrincipal SimpleKhumuUserDto user) throws UnauthenticatedException {
        notificationService.unreadAll(user);
        return new DefaultResponse<>(user.getUsername() + "의 모든 Notifications를 읽지 않음 처리 했습니다.", null);
    }

    @PatchMapping(value = "/api/notifications/{id}/unread")
    @ResponseBody
    public DefaultResponse<Object> unread(@AuthenticationPrincipal SimpleKhumuUserDto user, @PathVariable Long id) throws NoPermissionException {
        notificationService.unread(user, id);
        return new DefaultResponse<>("Notification(id=" + id + ")를 읽지 않음 처리 했습니다.", null);
    }

    @DeleteMapping(value = "/api/notifications/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @ResponseBody
    public DefaultResponse<Object> delete(@AuthenticationPrincipal SimpleKhumuUserDto user, @PathVariable Long id) throws NoPermissionException {
        notificationService.delete(user, id);
        return new DefaultResponse<>("Notification(id=" + id + ")를 삭제했습니다.", null);
    }

    @PostMapping(value = "/api/notifications/notify-new-announcement")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    // TODO: 지금은 편의상 그냥 Notification을 return
    // 근데 얘는 영속화된 Notification이 아니라 푸시알림만 보냄.
    public void notifyNewAnnouncement(@RequestBody NewAnnouncementCrawledDto body) {
        announcementEventService.notifyNewAnnouncementCrawled(body);
    }

    @PostMapping(value = "/api/subscriptions")
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
        ResourceNotificationSubscriptionDto subscriptionDto = notificationService.getSubscription(
                user,
                ResourceNotificationSubscription.builder()
                        .subscriber(username)
                        .resourceKind(resourceKind)
                        .resourceId(resourceId)
                        .build()
        );
        return new DefaultResponse<>(null, subscriptionDto);
    }

    @DeleteMapping(value = "/api/subscriptions")
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
