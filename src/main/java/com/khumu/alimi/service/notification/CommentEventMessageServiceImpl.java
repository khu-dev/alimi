package com.khumu.alimi.service.notification;

import com.google.gson.Gson;
import com.khumu.alimi.data.Comment;
import com.khumu.alimi.data.EventMessage;
import com.khumu.alimi.data.Notification;
import com.khumu.alimi.data.SimpleKhumuUser;
import com.khumu.alimi.repository.comment.CommentRepository;
import com.khumu.alimi.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentEventMessageServiceImpl {
    private final NotificationRepository notificationRepository;
    private final Gson gson;
    private final CommentRepository commentRepository;

    public List<Notification> createNotifications(EventMessage<Comment> e) {
        Comment c = e.getResource();
        List<Notification> results = new ArrayList<>();
        for (SimpleKhumuUser recipient : this.getRecipient(c)) {
            results.add(notificationRepository.create(
                    new Notification(null, "새로운 댓글이 작성되었습니다.", c.getContent(), "new_comment", recipient, false, null)
            ));
        }
        return results;
    }

    private List<SimpleKhumuUser> getRecipient(Comment c) {
        List<Comment> commentsInArticle = commentRepository.listFromArticle(c.getArticleObj().getId());
        List<SimpleKhumuUser> recipients = new ArrayList<>();
        recipients.add(c.getAuthorObj());
        for (Comment comment : commentsInArticle) {
            if (!c.getAuthor().equals(c.getArticleObj().getAuthor()) &&
                    recipients.stream().noneMatch(recipient -> recipient.getUsername().equals(comment.getAuthor()))) {
                recipients.add(comment.getAuthorObj());
            }
        }
        return recipients;
    }
}