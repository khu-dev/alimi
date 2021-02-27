package com.khumu.alimi.service.notification;

import com.google.gson.Gson;
import com.khumu.alimi.data.*;
import com.khumu.alimi.repository.article.ArticleRepository;
import com.khumu.alimi.repository.comment.CommentRepository;
import com.khumu.alimi.repository.notification.NotificationRepository;
import com.khumu.alimi.service.push.PushNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Comment Event Message는 articleObj를 이용하지 않고, articleId를 이용한다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommentEventMessageServiceImpl {
    private final NotificationRepository notificationRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final PushNotificationService pushNotificationService;
    private final Gson gson;

    public List<Notification> createNotifications(EventMessage<Comment> e) {
        Comment c = e.getResource();
        // comment는 comment microservice로부터 article id만을 받는다.
        c.setArticleObj(new Article(c.getArticleId()));

        List<Notification> results = new ArrayList<>();
        List<SimpleKhumuUser> recipients = this.getRecipient(c);
        log.info("" + recipients);
        for (SimpleKhumuUser recipient : recipients) {
            Notification tmp = new Notification();
            tmp.setRecipientObj(recipient);
            tmp.setRecipient(recipient.getUsername());
            tmp.setTitle("새로운 댓글이 생성되었습니다.");
            tmp.setContent(c.getContent());
            tmp.setKind("커뮤니티");

            Notification n = notificationRepository.create(
                    tmp
            );
            System.out.println("Create notification: " + n);
            results.add(n);
            pushNotificationService.executeNotify(n);
        }
        return results;
    }

    private List<SimpleKhumuUser> getRecipient(Comment c) {
        System.out.println(c.getArticleId());
        System.out.println(articleRepository.get(1L));
        System.out.println(articleRepository.get(c.getArticleId()));
        String articleAuthorUsername = articleRepository.get(c.getArticleId()).getAuthorObj().getUsername();
        String newCommentAuthorUsername = c.getAuthorObj().getUsername();

        List<Comment> commentsInArticle = commentRepository.listFromArticle(c.getArticleObj().getId());
        List<SimpleKhumuUser> recipients = new ArrayList<>();
        // 게시물 작성자도 수신자. 단, 댓글 작성자가 게시물 작성자가 아닌 경우
        if (!articleAuthorUsername.equals(newCommentAuthorUsername)) {
            recipients.add(new SimpleKhumuUser(articleAuthorUsername));
        }
        for (Comment eachComment : commentsInArticle) {
            // 새로운 댓글의 작성자가 아니면서, 아직 수신자 목록에 없는 경우
            if (!eachComment.getAuthorObj().getUsername().equals(newCommentAuthorUsername) &&
                    recipients.stream().noneMatch(recipient -> recipient.getUsername().equals(eachComment.getAuthorObj().getUsername()))) {
                recipients.add(eachComment.getAuthorObj());
            }
        }
        return recipients;
    }
}