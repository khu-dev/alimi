package com.khumu.alimi.service.notification;

import com.google.gson.Gson;
import com.khumu.alimi.data.dto.CommentDto;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.entity.Article;
import com.khumu.alimi.data.entity.Comment;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.SimpleKhumuUser;
import com.khumu.alimi.repository.ArticleRepository;
import com.khumu.alimi.repository.CommentRepository;
import com.khumu.alimi.repository.NotificationRepository;
import com.khumu.alimi.service.push.PushNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Comment Event Message는 articleObj를 이용하지 않고, articleId를 이용한다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommentEventMessageServiceImpl {
    final NotificationRepository notificationRepository;
    final ArticleRepository articleRepository;
    final CommentRepository commentRepository;
    final PushNotificationService pushNotificationService;
    final Gson gson;

    public List<Notification> createNotifications(EventMessageDto<CommentDto> e) {
        CommentDto commentDto = e.getResource();
        // comment는 comment microservice로부터 article id만을 받는다.
        List<Notification> results = new ArrayList<>();
        List<SimpleKhumuUser> recipients = this.getRecipient(commentDto);
        log.info("" + recipients);
        for (SimpleKhumuUser recipient : recipients) {
            Notification tmp = new Notification();
            tmp.setRecipientObj(recipient);
            tmp.setRecipient(recipient.getUsername());
            tmp.setTitle("새로운 댓글이 생성되었습니다.");
            tmp.setContent(commentDto.getContent());
            tmp.setKind("커뮤니티");

            Notification n = notificationRepository.save(
                    tmp
            );
            System.out.println("Create notification: " + n);
            results.add(n);
            pushNotificationService.executeNotify(n);
        }
        return results;
    }

    private List<SimpleKhumuUser> getRecipient(CommentDto commentDto) {
        Article article = articleRepository.getOne(commentDto.getArticle());
        String articleAuthorUsername = article.getAuthor().getUsername();
        String newCommentAuthorUsername = commentDto.getAuthor().getUsername();

        List<Comment> commentsInArticle = commentRepository.findByArticleId(commentDto.getArticle());
        List<SimpleKhumuUser> recipients = new ArrayList<>();
        // 게시물 작성자도 수신자. 단, 댓글 작성자가 게시물 작성자가 아닌 경우
        if (!articleAuthorUsername.equals(newCommentAuthorUsername)) {
            recipients.add(new SimpleKhumuUser(articleAuthorUsername));
        }
        for (Comment c : commentsInArticle) {
            // 새로운 댓글의 작성자가 아니면서, 아직 수신자 목록에 없는 경우
            if (!c.getAuthor().getUsername().equals(newCommentAuthorUsername) &&
                    recipients.stream().noneMatch(recipient -> recipient.getUsername().equals(c.getAuthor().getUsername()))) {
                recipients.add(c.getAuthor());
            }
        }
        return recipients;
    }
}