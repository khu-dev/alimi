package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.PushDevice;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import com.khumu.alimi.data.resource.ArticleResource;
import com.khumu.alimi.external.push.PushManager;
import com.khumu.alimi.repository.NotificationRepository;
import com.khumu.alimi.repository.PushDeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 게시물 관련된 이벤트가 발생했을 때 무슨 작업을 수행할 것인지.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleEventMessageService {
    final NotificationRepository notificationRepository;
    final NotificationService notificationService;
    final PushDeviceRepository pushDeviceRepository;
    final PushManager pushManager;

    // 게시글이 생성되었다는 이벤트를 통해 해당 게시글의 author는 자신의 게시글을 자동으로 subscribe시킴
    public void subscribeByNewArticle(EventMessageDto<ArticleResource> eventMessageDto) {
        ArticleResource articleResource = eventMessageDto.getResource();
        try {
            notificationService.subscribe(SimpleKhumuUserDto.builder().username(articleResource.getAuthor()).build(), ResourceNotificationSubscription.builder()
                    .resourceKind(ResourceKind.article)
                    .resourceId(articleResource.getId())
                    .build());
        } catch (Exception e) {
            log.error("Event message에 의한 알림 구독 생성이 실패했습니다. " +articleResource);
            e.printStackTrace();

        }
    }

    public void createNewHotArticleNotification(EventMessageDto<ArticleResource> eventMessageDto) {
        ArticleResource article = eventMessageDto.getResource();
        String author = article.getAuthor();
        Notification tmp = Notification.builder()
                .recipient(author)
                .title("내가 작성한 글이 핫 게시글로 선정되었습니다.")
                .content("게시글(" + article.getTitle() + ")")
                .kind("커뮤니티")
                .reference("articles/" + article.getId())
                .build();

        Notification n = notificationRepository.save(tmp);

        List<PushDevice> subscriptions = pushDeviceRepository.findAllByUser(author);
        for (PushDevice subscription : subscriptions) {
            pushManager.notify(n, subscription.getDeviceToken());
            log.info("푸시를 보냅니다. " + subscription.getUser());
        }
    }
}