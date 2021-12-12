package com.khumu.alimi.service;

import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.*;
import com.khumu.alimi.data.resource.ArticleResource;
import com.khumu.alimi.external.push.PushManager;
import com.khumu.alimi.repository.CustomPushOptionRepository;
import com.khumu.alimi.repository.NotificationRepository;
import com.khumu.alimi.repository.PushDeviceRepository;
import com.khumu.alimi.service.notification.NotificationService;
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
public class ArticleService {
    final NotificationRepository notificationRepository;
    final NotificationService notificationService;
    final PushDeviceRepository pushDeviceRepository;
    final CustomPushOptionRepository pushOptionRepository;
    final PushManager pushManager;

    // 게시글이 생성되었다는 이벤트를 통해 해당 게시글의 author는 자신의 게시글을 자동으로 subscribe시킴
    public void subscribeByNewArticle(ArticleResource article) {
        try {
            notificationService.subscribe(SimpleKhumuUserDto.builder().username(article.getAuthor()).build(), ResourceNotificationSubscription.builder()
                    .resourceKind(ResourceKind.article)
                    .resourceId(article.getId())
                    .build());
        } catch (Exception e) {
            log.error("article에 대한 알림 구독 생성이 실패했습니다. " + article);
            e.printStackTrace();
        }
    }

    public void notifyNewHotArticle(ArticleResource article) throws PushManager.PushException {
        String author = article.getAuthor();
        PushOption pushOption = pushOptionRepository.getOrCreate(PushOption.builder().username(author).pushOptionKind(PushOptionKind.NEW_HOT_ARTICLE).build());
        if (pushOption.getIsActivated() == false){
            log.info("게시글 작성자가 " + PushOptionKind.NEW_HOT_ARTICLE + "에 대한 알림을 비활성화하여 알림을 보내지 않습니다. " + pushOption);
            return;
        }

        Notification tmp = Notification.builder()
                .recipient(author)
                .title("내가 작성한 글이 핫 게시글로 선정되었습니다.")
                .content("게시글(" + article.getTitle() + ")")
                .kind("커뮤니티")
                .reference("articles/" + article.getId())
                .build();

        Notification n = notificationRepository.save(tmp);

        List<PushDevice> devices = pushDeviceRepository.findAllByUser(author);
        for (PushDevice device : devices) {
            try {
                log.info("푸시를 보냅니다. " + n.getRecipient());
                pushManager.notify(n, device.getDeviceToken());
            } catch (PushManager.ExpiredPushTokenException e) {
                log.warn("더 이상 존재하지 않는 device tokne이므로 삭제합니다." + device.getDeviceToken());
                pushDeviceRepository.delete(device);
            }
        }
    }
}