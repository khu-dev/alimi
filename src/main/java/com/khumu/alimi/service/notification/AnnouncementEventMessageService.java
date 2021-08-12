package com.khumu.alimi.service.notification;

import com.google.gson.Gson;
import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.AnnouncementAuthorFollowDto;
import com.khumu.alimi.data.dto.AnnouncementDto;
import com.khumu.alimi.data.dto.CommentDto;
import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.PushOption;
import com.khumu.alimi.data.entity.PushDevice;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import com.khumu.alimi.external.push.PushManager;
import com.khumu.alimi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.khumu.alimi.service.KhumuException.WrongResourceKindException;

/**
 * Comment 관련된 Event message의 기능을 담당.
 * 예를 들어 댓글이 생성되었다는 Event가 발생했을 때 무엇을 할 것인지.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnnouncementEventMessageService {
    final NotificationRepository notificationRepository;
    final CustomPushDeviceRepository pushDeviceRepository;
    final CustomPushOptionRepository pushOptionRepository;
    final AnnouncementAuthorFollowRepository announcementAuthorFollowRepository;

    final NotificationService notificationService;

    final PushManager pushManager;
    final Gson gson;

    @Transactional
    public List<Notification> createNotificationsForNewAnnouncement(AnnouncementDto announcementDto) {
        List<Notification> results = new ArrayList<>();
        List<String> recipientIds = this.getRecipientIds(announcementDto);
        log.info("새로운 공지사항 " + announcementDto + "에 대한 알림을 받을 사함들 " + recipientIds);

        for (String recipientId : recipientIds) {
            Notification tmp = Notification.builder()
                    .recipient(recipientId)
                    .title(announcementDto.getAuthorName() + "의 새로운 공지사항이 작성되었어요!")
                    .content(announcementDto.getTitle())
                    .kind("공지사항")
                    .reference("announcements")
                    .build();

            Notification n = notificationRepository.save(tmp);

            List<PushDevice> subscriptions = pushDeviceRepository.findAllByUser(recipientId);
            for (PushDevice subscription : subscriptions) {
                pushManager.notify(n, subscription.getDeviceToken());
                log.info("푸시를 보냅니다. " + subscription.getUser());
            }
            results.add(n);
        }
        return results;
    }

    // 댓글 생성 생성에 대한 recipient 찾기
    @Transactional
    public List<String> getRecipientIds(AnnouncementDto announcementDto) {
        // 지금은 우선 article에 대해서만 동작
        List<AnnouncementAuthorFollowDto> followDtos = announcementAuthorFollowRepository.findAllByAuthorName(announcementDto.getAuthorName());
        List<String> recipientIds = followDtos.stream().map(follow -> follow.getFollowerName()).collect(Collectors.toList());
        return recipientIds;
    }
}