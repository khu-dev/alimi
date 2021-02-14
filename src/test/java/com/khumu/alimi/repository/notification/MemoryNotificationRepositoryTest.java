package com.khumu.alimi.repository.notification;

import com.khumu.alimi.data.Notification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryNotificationRepositoryTest {
    private MemoryNotificationRepository repository;

    @BeforeEach
    void setUp() {
        System.out.println("테스트 시작");
        repository = new MemoryNotificationRepository();
    }
    @AfterEach
    void tearDown() {
        System.out.println("테스트 종료");
    }

    @Test
    void create() {
        Notification n = new Notification(
                0, "댓글이 생성되었습니다.", "어쩌구 저쩌구", "jinsu", false, null
        );
        Notification created = repository.create(n);
        assertNotNull(created);
        assertEquals(1, created.getId());
    }

    @Test
    @DisplayName("빈 상태에 get하면 null")
    void getWhenEmpty() {
        Notification n = repository.get(1);
        assertNull(n);
    }
    @Test
    void get(){
        // set up
        Notification n = new Notification(
                0, "댓글이 생성되었습니다.", "어쩌구 저쩌구", "jinsu", false, null
        );
        Notification created = repository.create(n);

        // do
        Notification n2 = repository.get(1);
        assertNotNull(n2);
    }
}