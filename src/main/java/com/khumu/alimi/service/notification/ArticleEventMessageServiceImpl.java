package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.dto.ArticleDto;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.entity.ArticleNotificationSubscription;
import com.khumu.alimi.repository.ArticleNotificationSubscriptionRepository;
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

    final ArticleNotificationSubscriptionRepository articleNotificationSubscriptionRepository;

    // author가 자기 게시물에 대한 알림을 켜게 함.
    public void createArticleNotificationSubscriptionForAuthor(EventMessageDto<ArticleDto> eventMessageDto) {
        ArticleDto articleDto = eventMessageDto.getResource();
        if( !articleNotificationSubscriptionRepository.findAllByArticleId(articleDto.getId()).isEmpty()){
            log.warn(articleDto + " 에 대한 author의 알림 구독이 이미 존재합니다.");
            return;
        }
        ArticleNotificationSubscription subscription = articleNotificationSubscriptionRepository.save(ArticleNotificationSubscription.builder()
                .article(articleDto.getId())
                .subscriber(articleDto.getAuthor().getUsername())
                .isActivated(true)
                .build());
        log.info(subscription + " 생성");
    }
}