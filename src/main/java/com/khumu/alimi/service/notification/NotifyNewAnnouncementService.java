package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.dto.AnnouncementAuthorFollowDto;
import com.khumu.alimi.data.dto.AnnouncementDto;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.PushDevice;
import com.khumu.alimi.external.push.PushManager;
import com.khumu.alimi.repository.AnnouncementAuthorFollowRepository;
import com.khumu.alimi.repository.CustomPushDeviceRepository;
import com.khumu.alimi.repository.CustomPushOptionRepository;
import com.khumu.alimi.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 새 공지사항이 생성되었을 때 알림
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotifyNewAnnouncementService {
    final CustomPushDeviceRepository pushDeviceRepository;
    private final NotificationRepository notificationRepository;
    final PushManager pushManager;

    @Transactional
    public List<Notification> notify(AnnouncementDto announcementDto) {
        List<Notification> results = new ArrayList<>();
        log.info("새로운 공지사항 " + announcementDto.getId() + "에 대한 알림 전송 시작");

        for (String recipientId : announcementDto.getFollowers()) {
            Notification n = notificationRepository.save(Notification.builder()
                    .recipient(recipientId)
                    .title(announcementDto.getAuthorName() + "의 새로운 공지사항이 작성되었어요!")
                    .content(announcementDto.getTitle())
                    .kind("공지사항")
                    .reference("announcements/" + announcementDto.getId())
                    .link(announcementDto.getLink())
                    .build());

            // TODO: 푸시 알림 조건 반영
            List<PushDevice> subscriptions = pushDeviceRepository.findAllByUser(recipientId);
            for (PushDevice subscription : subscriptions) {
                pushManager.notify(n, subscription.getDeviceToken());
            }
            results.add(n);
        }
        return results;
    }
}