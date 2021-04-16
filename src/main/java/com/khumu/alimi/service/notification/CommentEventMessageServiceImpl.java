package com.khumu.alimi.service.notification;

import com.google.gson.Gson;
import com.khumu.alimi.data.EventKind;
import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.CommentDto;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.entity.*;
import com.khumu.alimi.external.push.PushManager;
import com.khumu.alimi.repository.ArticleRepository;
import com.khumu.alimi.repository.CommentRepository;
import com.khumu.alimi.repository.NotificationRepository;
import com.khumu.alimi.repository.PushSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Comment 관련된 Event message의 기능을 담당.
 * 예를 들어 댓글이 생성되었다는 Event가 발생했을 때 무엇을 할 것인지.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommentEventMessageServiceImpl {
    final NotificationRepository notificationRepository;
    final PushSubscriptionRepository pushSubscriptionRepository;
    final ArticleRepository articleRepository;
    final CommentRepository commentRepository;
    final PushManager pushManager;
    final Gson gson;

    @Transactional
    public List<Notification> createNotifications(ResourceKind resourceKind, EventKind eventKind, CommentDto commentDto) {
        List<Notification> results = new ArrayList<>();
        List<SimpleKhumuUser> recipients = this.getRecipient(commentDto);
        log.info("" + recipients);

        for (SimpleKhumuUser recipient : recipients) {
            Notification tmp = Notification.builder()
                    .recipient(recipient)
                    .title("새로운 댓글이 생성되었습니다.")
                    .content(commentDto.getContent())
                    .kind("커뮤니티")
                    .build();

            Notification n = notificationRepository.save(tmp);

            List<PushSubscription> subscriptions = pushSubscriptionRepository.listByUsername(recipient.getUsername());
            for (PushSubscription subscription : subscriptions) {
                pushManager.notify(n, subscription.getDeviceToken());
                log.info("푸시를 보냅니다. " + subscription.getUser().getUsername());
            }
            results.add(n);
        }
        return results;
    }

    @Transactional
    public List<Notification> createNotifications(EventMessageDto<CommentDto> e) {
        CommentDto commentDto = e.getResource();
        // comment는 comment microservice로부터 article id만을 받는다.
        List<Notification> results = new ArrayList<>();
        List<SimpleKhumuUser> recipients = this.getRecipient(commentDto);
        log.info("" + recipients);

        for (SimpleKhumuUser recipient : recipients) {
            Notification tmp = Notification.builder()
                    .recipient(recipient)
                    .title("새로운 댓글이 생성되었습니다.")
                    .content(commentDto.getContent())
                    .kind("커뮤니티")
                    .build();

            Notification n = notificationRepository.save(tmp);

            List<PushSubscription> subscriptions = pushSubscriptionRepository.listByUsername(recipient.getUsername());
            for (PushSubscription subscription : subscriptions) {
                pushManager.notify(n, subscription.getDeviceToken());
            }
            results.add(n);
        }
        return results;
    }

    @Transactional
    public List<SimpleKhumuUser> getRecipient(CommentDto commentDto) {
        Article article = articleRepository.getOne(commentDto.getArticle());
        String articleAuthorUsername = article.getAuthor().getUsername();
        String newCommentAuthorUsername = commentDto.getAuthor().getUsername();

        List<Comment> commentsInArticle = commentRepository.findByArticleId(commentDto.getArticle());
        List<SimpleKhumuUser> recipients = new ArrayList<>();
        // 게시물 작성자도 수신자. 단, 댓글 작성자가 게시물 작성자가 아닌 경우
        if (!articleAuthorUsername.equals(newCommentAuthorUsername)) {
            recipients.add(SimpleKhumuUser.builder().username(articleAuthorUsername).build());

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