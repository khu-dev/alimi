package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.ArticleDto;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import com.khumu.alimi.data.resource.ArticleResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 게시물 관련된 이벤트가 발생했을 때 무슨 작업을 수행할 것인지.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleEventMessageService {

    final NotificationService notificationService;

    // ArticleEventMessageService는 NotificationService에 의존한다.
    public void createArticleNotificationSubscriptionForAuthor(EventMessageDto<ArticleResource> eventMessageDto) {
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
}