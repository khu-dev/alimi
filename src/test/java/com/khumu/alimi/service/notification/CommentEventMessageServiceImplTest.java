package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.Comment;
import com.khumu.alimi.data.EventMessage;
import com.khumu.alimi.data.Notification;
import com.khumu.alimi.data.SimpleKhumuUser;
import com.khumu.alimi.repository.comment.CommentRepository;
import com.khumu.alimi.repository.comment.JpaCommentRepository;
import com.khumu.alimi.repository.comment.JpaCommentRepositoryIfc;
import com.khumu.alimi.repository.notification.JpaNotificationRepository;
import com.khumu.alimi.repository.notification.MemoryNotificationRepository;
import com.khumu.alimi.repository.notification.NotificationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class CommentEventMessageServiceImplTest {
    @Mock
    NotificationRepository notificationRepository;
    @Mock
    CommentRepository commentRepository;
    @InjectMocks
    CommentEventMessageServiceImpl service;

    List<Comment> fixtureComments=new ArrayList<>();
    @BeforeEach
    void setUp() {
        fixtureComments.clear();
        fixtureComments.add(new Comment("jinsu"));
        fixtureComments.add(new Comment("admin"));
        when(commentRepository.listFromArticle(anyLong())).thenReturn(fixtureComments);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * jinsu의 게시물에 jinsu와 admin이 댓글을 닮.
     * jinsu는 두 번이 아닌 한 번 포함됨.
     * 따라서 jinsu, admin에게 알림 전송.
     */
    @Test
    void createNotifications_올바른_Recipient_Article과_Comment에_같은_Author() {
        List<String> desiredRecipientUsernames = new ArrayList<>(Arrays.asList("jinsu", "admin"));
        // fixture들은 article 1L을 사용하도록 되어있음.
        List<Notification> notifications = service.createNotifications(new EventMessage<Comment>("comment", "create", new Comment("jinsu", 1L)));
        assertThat(notifications).hasSize(desiredRecipientUsernames.size());
    }

    /**
     * somebody의 게시물에 jinsu와 admin이 댓글을 닮.
     * somebody, jinsu, admin에게 알림 전송.
     */
    @Test
    void createNotifications_올바른_Recipient_Article과_Comment에_다른_Author() {
        // fixture들은 article 1L을 사용하도록 되어있음.
        List<String> desiredRecipientUsernames = new ArrayList<>(Arrays.asList("somebody", "jinsu", "admin"));
        List<Notification> notifications = service.createNotifications(new EventMessage<Comment>("comment", "create", new Comment("somebody", 1L)));
        assertThat(notifications).hasSize(desiredRecipientUsernames.size());
    }
}