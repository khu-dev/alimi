package com.khumu.alimi.service.notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.gson.Gson;
import com.khumu.alimi.data.ResourceKind;
import com.khumu.alimi.data.dto.CommentDto;
import com.khumu.alimi.data.dto.HaksaScheduleDto;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.PushDevice;
import com.khumu.alimi.data.entity.PushOptionKind;
import com.khumu.alimi.data.entity.ResourceNotificationSubscription;
import com.khumu.alimi.external.push.PushManager;
import com.khumu.alimi.repository.CustomPushDeviceRepository;
import com.khumu.alimi.repository.CustomPushOptionRepository;
import com.khumu.alimi.repository.NotificationRepository;
import com.khumu.alimi.repository.ResourceNotificationSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.khumu.alimi.service.KhumuException.WrongResourceKindException;

/**
 * HasksaSchedule 관련된 Event message의 기능을 담당.
 * 예를 들어 새로운 학사일정이 시작될 때 Event가 발생했을 때 무엇을 할 것인지.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HaksaScheduleEventService {
    final NotificationRepository notificationRepository;
    final CustomPushDeviceRepository pushDeviceRepository;
    final CustomPushOptionRepository pushOptionRepository;
    final NotificationService notificationService;
    final ResourceNotificationSubscriptionRepository resourceNotificationSubscriptionRepository;
    final PushManager pushManager;
    final Gson gson;

    @Transactional
    public List<Notification> createNotificationForHaksaScheduleStarts(HaksaScheduleDto haksaScheduleDto) {
        List<Notification> results = new ArrayList<>();
        // 알림을 끈 유저들만 빼고 다 알림을 보냄
        List<String> usersIgnored = pushOptionRepository.findAllByPushOptionKindAndIsActivated(PushOptionKind.HAKSA_SCHEDULE, false).stream()
                .map(option->option.getUsername()).collect(Collectors.toList());
        List<PushDevice> devices = pushDeviceRepository.findAll();
        // dto에는 UTC + 9 string가 전달되고
        // 그걸 해석을 잘 못해서 Timezone 정보가 누락됨.
        // 그리고 9시간 더해서 실제 한국시로 변환함
        LocalDateTime startDate = haksaScheduleDto.getStartsAt().plusHours(9);
        LocalDateTime endDate = haksaScheduleDto.getEndsAt().plusHours(9);

        String content = "[" + startDate.getYear() + "/" + startDate.getMonthValue() + "/" + startDate.getDayOfMonth();;
        if (startDate.getDayOfYear() == endDate.getDayOfYear() &&
            startDate.getDayOfMonth() == endDate.getDayOfMonth() &&
            startDate.getDayOfWeek() == endDate.getDayOfWeek()
        ) {
            content += "~" + endDate.getYear() + "/" + endDate.getMonthValue() + "/" + endDate.getDayOfMonth();;
        }
        content += "] " + haksaScheduleDto.getTitle();

        for (PushDevice device : devices) {
            if (!usersIgnored.contains(device.getUser())) {
                Notification tmp = Notification.builder()
                        .recipient(device.getUser())
                        .title("새로운 학사일정이 있어요!")
                        .content(content)
                        .kind("학사일정")
                        .build();

                Notification n = notificationRepository.save(tmp);
                try {
                    pushManager.notify(n, device.getDeviceToken());
                    log.info("푸시를 보냈습니다. " + device.getUser());
                    results.add(n);
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

        return results;
    }
}