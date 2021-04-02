package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.SimpleKhumuUser;
import com.khumu.alimi.service.push.PushNotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * mock을 이용하지 않고, spy bean을 이용해 Notification service를 테스트
 */
@ExtendWith(SpringExtension.class)
class NotificationServiceImplTest {
    @SpyBean
    MemoryNotificationRepository repo;
    @MockBean
    PushNotificationService pushNotificationService;
    @SpyBean
    NotificationServiceImpl service;
    @BeforeEach
    void setUp() {
        repo.create(new Notification(
                null,
                "테스트 댓글이 작성되었습니다.",
                "이것은~ 테스트일 뿐~ 넘어져도 괜찮아~",
                "커뮤니티",
                new SimpleKhumuUser("tester_jinsu"),
                "tester_jinsu",
                false,
                null
        ));
        System.out.println("NotificationServiceImplTest.setUp");
    }

    @AfterEach
    void tearDown() {
        System.out.println("NotificationServiceImplTest.tearDown");
    }

    @Test
    void getNotification() {
        System.out.println("NotificationServiceImplTest.getNotification");
        Notification n = service.getNotification(1L);
        assertThat(n).isNotNull();
    }

    @Test
    void listNotifications() {
        // setUP에서 만든 데이터를 이용해 테스트
        assertThat(service.listNotifications()).hasSizeLessThanOrEqualTo(10);
        assertThat(service.listNotifications()).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void listNotificationsByUsername() {
        // setUP에서 만든 데이터를 이용해 테스트
        List<Notification> l = service.listNotificationsByUsername("tester_jinsu");
        assertThat(l).hasSizeGreaterThanOrEqualTo(1);
        for (Notification n : l) {
            assertThat(n.getRecipientObj().getUsername()).isEqualTo("tester_jinsu");
        }
    }
}