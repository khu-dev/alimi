package com.khumu.alimi.controller.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.khumu.alimi.controller.DefaultResponse;
import com.khumu.alimi.data.Notification;
import com.khumu.alimi.repository.notification.NotificationRepository;
import com.khumu.alimi.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotificationController {
    private NotificationService notificationService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

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
        logger.info(body.toString());

        System.out.println(body.isRead());
        System.out.println(id);
        if (body.isRead()) {
            notificationService.read(id);
        } else {
            logger.warn("Nothing to update");
        }
        return new DefaultResponse<>("Notification을 수정했습니다.", null);
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
