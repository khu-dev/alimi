package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.Notification;
import com.khumu.alimi.data.SimpleKhumuUser;
import com.khumu.alimi.repository.notification.MemoryNotificationRepository;
import com.khumu.alimi.repository.notification.NotificationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class) // <- 스프링의 기능을 JUnit 5를 통한 테스트에서 간단히 사용하고 싶을 때 사용하는듯
class SpringNotificationServiceImplTest {
    @SpyBean
    MemoryNotificationRepository repo;
    @SpyBean
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