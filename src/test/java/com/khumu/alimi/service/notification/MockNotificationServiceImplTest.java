package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.SimpleKhumuUser;
import com.khumu.alimi.service.push.PushNotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;


/**
 * mock을 이용해 Notification service를 테스트
 */
@ExtendWith(MockitoExtension.class)
class MockNotificationServiceImplTest {
    @Mock
    NotificationRepository repo;
    @Mock
    PushNotificationService pushNotificationService;
    @InjectMocks
    NotificationServiceImpl service;
    List<Notification> fixtureNotifications = new ArrayList<>();
    @BeforeEach
    void setUp() {
        fixtureNotifications.clear();
        fixtureNotifications.add(
                new Notification("jinsu", "test notificaiton")
        );
        fixtureNotifications.add(
                new Notification("jinsu-foo", "test notificaiton")
        );
        fixtureNotifications.add(
                new Notification("jinsu-bar", "test notificaiton")
        );

        // 해당 테스트 케이스에서 mock한 function을 안 쓸 수 있는 경우 lenient를 붙여준다.
        lenient().when(repo.list()).thenReturn(fixtureNotifications);
        lenient().when(repo.list(anyString())).thenAnswer(
                invocation -> {
                    return fixtureNotifications.stream().filter(
                            n -> n.getRecipientObj().getUsername().equals(invocation.getArgument(0))
                    ).collect(Collectors.toList());
                }
        );
        lenient().when(repo.get(anyLong())).thenAnswer(
                invocation -> {
                    Long id = (long) (invocation.getArgument(0));
                    return fixtureNotifications.get(id.intValue());
                }
        );
        repo.create(new Notification(
                null,
                "테스트 댓글이 생성되었습니다.",
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
        List<Notification> l = service.listNotificationsByUsername("jinsu");
        assertThat(l).hasSizeGreaterThanOrEqualTo(1);
        for (Notification n : l) {
            assertThat(n.getRecipientObj().getUsername()).isEqualTo("jinsu");
        }
    }

    @Test
    void read(){
        assertThat(service.getNotification(1L).isRead()).isFalse();
        service.read(1L);
        assertThat(service.getNotification(1L).isRead()).isTrue();
    }
}