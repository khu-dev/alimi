package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.Notification;
import com.khumu.alimi.data.SimpleKhumuUser;
import com.khumu.alimi.repository.notification.MemoryNotificationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class) // <- Spring까지는 쓸 필요 없는 경우, mocking하고 싶은 경우
class NotificationServiceImplTest {
    @Spy
    MemoryNotificationRepository repo;
    @InjectMocks
    NotificationServiceImpl service;
    @BeforeEach
    void setUp() {
        repo.create(new Notification(
                null,
                "테스트 댓글이 생성되었습니다.",
                "이것은~ 테스트일 뿐~ 넘어져도 괜찮아~",
                new SimpleKhumuUser("jinsu"),
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
    }

    @Test
    void listNotificationsByUsername() {
    }
}