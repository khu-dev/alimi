package com.khumu.alimi.controller.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.khumu.alimi.controller.NotificationController;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.mapper.NotificationMapper;
import com.khumu.alimi.service.KhumuException;
import com.khumu.alimi.service.notification.NotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebMvcTest(NotificationController.class)
public class NotificationControllerWebMvcTest {

    @MockBean
    NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    NotificationMapper notificationMapper;

    private List<Notification> fixtureNotifications = new ArrayList<Notification>();
    @BeforeEach
    void setUp() throws KhumuException.UnauthenticatedException, KhumuException.NoPermissionException {

        Notification n1 = Notification.builder()
                .id(1L).title("댓글이 생성되었습니다.").content("hello, world 댓글이랍니다~!")
                .kind("community")
                .recipient("jinsu")
                .isRead(false)
                .build();
        fixtureNotifications.add(n1);

        Notification n2 = Notification.builder()
                .id(1L).title("광고").content("뭐?! 쿠뮤에서 이번에 새 팀원을 모집한다구~?!")
                .kind("ad")
                .recipient("jinsu")
                .isRead(false)
                .build();

        fixtureNotifications.add(n2);
        when(notificationService.listNotifications(Pageable.unpaged())).thenReturn(fixtureNotifications.stream().map(notificationMapper::toDto).collect(Collectors.toList()));
        when(notificationService.listNotificationsByUsername(null, anyString(), Pageable.unpaged())).thenReturn(fixtureNotifications.stream().map(notificationMapper::toDto).collect(Collectors.toList()));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void 존재하지_않는_페이지() throws Exception {
        this.mockMvc.perform(get("/jinsu-has-never-defined-this-page"))
                .andExpect(status().isNotFound());
    }

//    @Test
//    public void listNotifications() throws Exception {
//        this.mockMvc.perform(get("/api/notifications"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("data").isArray())
//                // setUp에서 만드는 data에 의해
//                .andExpect(jsonPath("data.length()").value(greaterThanOrEqualTo(1)))
//                .andExpect(jsonPath("data.length()").value(lessThanOrEqualTo(10)));
//    }

    @Test
    public void listNotifications_json_필드명_체크() throws Exception {
        String recipientUsername = "jinsu";
        this.mockMvc.perform(get("/api/notifications?recipient=" + recipientUsername))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data").isArray())
                // setUp에서 만드는 data에 의해
//                .andExpect(
//                        jsonPath("data[*].recipient.username", everyItem(
//                                allOf(equalTo(recipientUsername))
//                        ))
//                )
                .andExpect(jsonPath("data[0].id").isNumber())
                .andExpect(jsonPath("data[0].recipient").isString())
                .andExpect(jsonPath("data[0].recipientObj").doesNotHaveJsonPath())
                .andExpect(jsonPath("data[0].recipient_obj").doesNotHaveJsonPath())
                .andExpect(jsonPath("data[0].recipient_id").doesNotHaveJsonPath())
                .andExpect(jsonPath("data[0].createdAt").doesNotHaveJsonPath())
                .andExpect(jsonPath("data[0].created_at").isString());
    }

//    @Test
//    public void listNotificationsByWrongUsername() throws Exception {
//        String recipientUsername = "FooBarCreatedByJinsu";
//        this.mockMvc.perform(get("/api/notifications?recipient=" + recipientUsername))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("data").isArray())
//                // setUp에서 만드는 data에 의해
//                .andExpect(
//                        jsonPath("data[*].recipient.username", hasSize(0))
//                );
//    }
}