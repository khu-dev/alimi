package com.khumu.alimi.service;

import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.NewAnnouncementCrawledDto;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.data.entity.*;
import com.khumu.alimi.data.resource.ArticleResource;
import com.khumu.alimi.external.push.PushManager;
import com.khumu.alimi.repository.CustomPushDeviceRepository;
import com.khumu.alimi.repository.CustomPushOptionRepository;
import com.khumu.alimi.repository.NotificationRepository;
import com.khumu.alimi.repository.PushDeviceRepository;
import com.khumu.alimi.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 공지사항 관련된 이벤트가 발생했을 때 무슨 작업을 수행할 것인지.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class NotifyAnnouncementCrawledService {
    private final NotificationRepository notificationRepository;
    private final CustomPushDeviceRepository pushDeviceRepository;
    private final CustomPushOptionRepository pushOptionRepository;
    private final PushManager pushManager;

    // 새로운 공지사항이 작성되었다는 이벤트를 바탕으로 알림
    public void notifyNewAnnouncementCrawled(NewAnnouncementCrawledDto event) {
        for (String recipientId:event.getFollowers()){
            PushOption option = pushOptionRepository.getOrCreate(PushOption.builder().pushOptionKind(PushOptionKind.ANNOUNCEMENT_CRAWLED).username(recipientId).isActivated(true).build());
            if (option.getIsActivated()) {
                Notification tmp = Notification.builder()
                        .recipient(recipientId)
                        .title(event.getAnnouncement().getAuthor().getAuthorName() + "이 새로운 공지사항을 등록했습니다!")
                        .content(event.getAnnouncement().getTitle())
                        .kind("공지사항")
                        .reference(null)
                        .link(event.getAnnouncement().getSubLink())
                        .build();

                Notification n = notificationRepository.save(tmp);

                List<PushDevice> devices = pushDeviceRepository.findAllByUser(recipientId);
                for (PushDevice device : devices) {
                    try {
                        pushManager.notify(n, device.getDeviceToken());
                        log.info("푸시를 보냅니다. " + device.getUser());
                    } catch (PushManager.PushException e) {
                        if (e.getMessage().contains("Requested entity was not found.")) {
                            log.warn("더 이상 존재하지 않는 device tokne이므로 삭제합니다." + device.getDeviceToken());
                            pushDeviceRepository.delete(device);
                        } else{
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}