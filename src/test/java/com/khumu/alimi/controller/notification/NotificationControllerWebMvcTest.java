package com.khumu.alimi.controller.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matcher.*;

import com.khumu.alimi.AlimiApplication;
import com.khumu.alimi.data.Notification;
import com.khumu.alimi.data.SimpleKhumuUser;
import com.khumu.alimi.repository.notification.MemoryNotificationRepository;
import com.khumu.alimi.service.notification.NotificationService;
import com.khumu.alimi.service.notification.NotificationServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NotificationController.class)
public class NotificationControllerWebMvcTest {

    // memory를 일종의 mocking(spy)처럼 이용하도록
    @SpyBean
    MemoryNotificationRepository memoryNotificationRepository;
    @SpyBean
    NotificationServiceImpl notificationService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Notification n1 = new Notification(
                null,
                "댓글이 생성되었습니다.",
                "hello, world 댓글이랍니다~!",
                "new_comment",
                new SimpleKhumuUser("jinsu"),
                false,
                null
        );
        memoryNotificationRepository.create(n1);

        Notification n2 = new Notification(
                null,
                "광고",
                "뭐?! 쿠뮤에서 이번에 새 팀원을 모집한다구~?!",
                "new_comment",
                new SimpleKhumuUser("jinsu"),
                false,
                null
        );
        memoryNotificationRepository.create(n1);
    }

    @AfterEach
    void tearDown() {
        memoryNotificationRepository.clear();
    }

    @Test
    public void 존재하지_않는_페이지() throws Exception {
        this.mockMvc.perform(get("/jinsu-has-never-defined-this-page"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listNotifications() throws Exception {

        this.mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").isArray())
                // setUp에서 만드는 data에 의해
                .andExpect(jsonPath("data.length()").value(greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("data.length()").value(lessThanOrEqualTo(10)));

    }

    @Test
    public void listNotificationsByUsername() throws Exception {
        String recipientUsername = "jinsu";
        this.mockMvc.perform(get("/api/notifications?recipient=" + recipientUsername))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").isArray())
                // setUp에서 만드는 data에 의해
                .andExpect(
                        jsonPath("data[*].recipient.username", everyItem(
                                allOf(equalTo(recipientUsername))
                        ))
                );
    }

    @Test
    public void listNotificationsByWrongUsername() throws Exception {
        String recipientUsername = "FooBarCreatedByJinsu";
        this.mockMvc.perform(get("/api/notifications?recipient=" + recipientUsername))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").isArray())
                // setUp에서 만드는 data에 의해
                .andExpect(
                        jsonPath("data[*].recipient.username", hasSize(0))
                );
    }
}