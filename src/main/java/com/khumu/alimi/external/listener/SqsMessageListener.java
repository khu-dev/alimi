package com.khumu.alimi.external.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khumu.alimi.data.EventKind;
import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.*;
import com.khumu.alimi.data.resource.ArticleResource;
import com.khumu.alimi.external.slack.SlackNotifier;
import com.khumu.alimi.service.NotifyAnnouncementCrawledService;
import com.khumu.alimi.service.notification.ArticleEventService;
import com.khumu.alimi.service.notification.CommentEventService;
import com.khumu.alimi.service.notification.HaksaScheduleEventService;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SqsMessageListener {
    final NotifyAnnouncementCrawledService announcementEventService;
    final ArticleEventService articleEventMessageService;
    final CommentEventService commentEventMessageService;
    final HaksaScheduleEventService haksaScheduleEventService;
    final ObjectMapper objectMapper;
    final SlackNotifier slackNotifier;

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
                    NewAnnouncementCrawledDto event = objectMapper.readValue(body.getMessage(), NewAnnouncementCrawledDto.class);
                    switch (eventKind) {
                        case create:{
                            slackNotifier.sendSlack("새로운 공지사항에 대한 알림을 보냅니다.", event.getAnnouncement().getTitle());
                            announcementEventService.notifyNewAnnouncementCrawled(event);
                        } break;
                    }
                } break;
                case haksa_schedule:{
                    switch (eventKind) {
                        case start:{
                            System.out.println("새로운 학사일정 전달됨!");
                            HaksaScheduleDto haksaScheduleDto = objectMapper.readValue(body.getMessage(), HaksaScheduleDto.class);
                            haksaScheduleEventService.createNotificationForHaksaScheduleStarts(haksaScheduleDto);
                        } break;
                    }
                } break;
                default:
                    System.out.println("default");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("SQS 메시지 처리 도중 오류 발생!");
            slackNotifier.sendSlack("SQS 메시지 처리 도중 오류 발생!", e.getStackTrace().toString());
        }
    }
}
