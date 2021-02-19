package com.khumu.alimi.service.notification;

import com.google.gson.Gson;
import com.khumu.alimi.data.Comment;
import com.khumu.alimi.data.EventMessage;
import com.khumu.alimi.data.Notification;
import com.khumu.alimi.data.SimpleKhumuUser;
import com.khumu.alimi.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentEventMessageServiceImpl {
    private NotificationRepository notificationRepository;
    private Gson gson;
    @Autowired
    public CommentEventMessageServiceImpl(NotificationRepository notificationRepository, Gson gson) {
        this.notificationRepository = notificationRepository;
        this.gson = gson;
    }

    @Transactional
    public Notification createNotification(EventMessage<Comment> e) {
        Comment c = e.getResource();

        Notification n = new Notification(0L, "새로운 댓글이 작성되었습니다.", c.getContent(), c.getAuthorObj(), false, null);
        return notificationRepository.create(n);
    }
}