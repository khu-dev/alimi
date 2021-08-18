package com.khumu.alimi.external.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.alimi.data.EventKind;
import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.AnnouncementDto;
import com.khumu.alimi.data.dto.CommentDto;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.dto.SqsMessageBodyDto;
import com.khumu.alimi.data.resource.ArticleResource;
import com.khumu.alimi.service.notification.AnnouncementEventService;
import com.khumu.alimi.service.notification.ArticleEventService;
import com.khumu.alimi.service.notification.CommentEventService;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SqsMessageListener {
    final CommentEventService commentEventMessageService;
    final ArticleEventService articleEventMessageService;
    final AnnouncementEventService announcementEventMessageService;
    final ObjectMapper objectMapper;

    @SqsListener(value = "${sqs.notificationQueue.name}")
    public void receiveMessage(SqsMessageBodyDto body) {
        log.info("SQS 메시지를 가져왔습니다.");
        try{
            ResourceKind resourceKind = ResourceKind.valueOf(body.getMessageAttributes().getResourceKind().getValue());
            EventKind eventKind = EventKind.valueOf(body.getMessageAttributes().getEventKind().getValue());
            EventMessageDto<Object> eventMessageDto = EventMessageDto.builder().eventKind(eventKind).resourceKind(resourceKind).build();
            log.info("ResourceKind: " + resourceKind + ", EventKind: " + eventKind + ", EventMessageDto: " + eventMessageDto);
            // 실제 발생한 이벤트 속 정보를 얻기 위해선 body.getMessage()를 Deserialize 해야한다.
            // body.getMessage()는 각각의 리소스들의 Dto를 Json 문자열로 나타낸 것이다.
            // 예를 들어 CommentDto의 Json 문자열 버젼.
            switch (resourceKind){
                case comment:{
                    CommentDto commentDto = objectMapper.readValue(body.getMessage(), CommentDto.class);
                    switch (eventMessageDto.getEventKind()) {
                        case create:
                            commentEventMessageService.subscribeArticle(commentDto);
                            commentEventMessageService.createNotificationsForNewComment(commentDto);
                            break;
                    }

                } break;
                case article: {
                    ArticleResource article = objectMapper.readValue(body.getMessage(), ArticleResource.class);
                    switch (eventKind) {
                        case create:{
                            articleEventMessageService.subscribeByNewArticle(article);
                        }break;

                        case new_hot_article:{
                            articleEventMessageService.notifyNewHotArticle(article);
                        }break;
                    }
                } break;
                case announcement:{
                    AnnouncementDto announcementDto = objectMapper.readValue(body.getMessage(), AnnouncementDto.class);
                    switch (eventKind) {
                        case create:{
                            announcementEventMessageService.createNotificationsForNewAnnouncement(announcementDto);
                        } break;
                    }
                } break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("SQS 메시지 처리 도중 오류 발생!");
        }


        System.out.println("SQS 비용을 줄이기 위한 Dummy wait 시작");
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("SQS 비용을 줄이기 위한 Dummy wait 마무리");
    }
}
