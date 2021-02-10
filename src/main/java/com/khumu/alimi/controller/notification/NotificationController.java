package com.khumu.alimi.controller.notification;

import com.khumu.alimi.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @RequestMapping(value="/ping", method= RequestMethod.GET)
    public String ping() {
        notificationService.getNotification(1);
        return "hello";
    }
}
