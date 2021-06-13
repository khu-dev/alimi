package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.dto.ArticleDto;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import com.khumu.alimi.repository.ResourceNotificationSubscriptionRepository;
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

    final ResourceNotificationSubscriptionRepository resourceNotificationSubscriptionRepository;

    // author가 자기 게시물에 대한 알림을 켜게 함.
    // 익명 게시글의 경우 얘도 동작하기 힘들겠다...
    public void createArticleNotificationSubscriptionForAuthor(EventMessageDto<ArticleDto> eventMessageDto) {
        ArticleDto articleDto = eventMessageDto.getResource();
        if( !resourceNotificationSubscriptionRepository.findAllByArticle(articleDto.getId()).isEmpty()){
            log.warn(articleDto + " 에 대한 author의 알림 구독이 이미 존재합니다.");
            return;
        }

        ResourceNotificationSubscription subscription = resourceNotificationSubscriptionRepository.save(ResourceNotificationSubscription.builder()
                .article(articleDto.getId())
                // 이 username이.... 익명일 수도....
                // 이 부분 fix해야된다. ㅜㅜ
                .subscriber(articleDto.getAuthor().getUsername())
                .isActivated(true)
                .build());
        log.info(subscription + " 생성");
    }
}