package com.khumu.alimi.controller.notification;

import com.khumu.alimi.controller.DefaultResponse;
import com.khumu.alimi.data.Notification;
import com.khumu.alimi.repository.notification.NotificationRepository;
import com.khumu.alimi.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotificationController {
    private NotificationService notificationService;

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

}
