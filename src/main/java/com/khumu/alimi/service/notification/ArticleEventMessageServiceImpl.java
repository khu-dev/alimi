package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.ArticleDto;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 게시물 관련된 이벤트가 발생했을 때 무슨 작업을 수행할 것인지.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleEventMessageServiceImpl{

    final NotificationService notificationService;

    // author가 자기 게시물에 대한 알림을 켜게 함.
    // 익명 게시글의 경우 얘도 동작하기 힘들겠다...
    public void createArticleNotificationSubscriptionForAuthor(EventMessageDto<ArticleDto> eventMessageDto) {
        ArticleDto articleDto = eventMessageDto.getResource();
        try {
            notificationService.toggleSubscription(articleDto.getAuthor(), ResourceNotificationSubscription.builder()
                .resourceKind(ResourceKind.article)
                .article(articleDto.getId())

            .build());
        } catch (Exception e) {
            log.error("Event message에 의한 알림 구독 생성이 실패했습니다. " + articleDto);
            e.printStackTrace();

        }
    }
}