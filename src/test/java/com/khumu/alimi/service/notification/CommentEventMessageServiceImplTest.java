package com.khumu.alimi.service.notification;

import com.khumu.alimi.data.dto.EventMessageDto;
import com.khumu.alimi.data.entity.Article;
import com.khumu.alimi.data.entity.Comment;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.SimpleKhumuUser;
import com.khumu.alimi.service.push.PushNotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@Transactional
class CommentEventMessageServiceImplTest {
    @Mock
    NotificationRepository notificationRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    ArticleRepository articleRepository;
    @Mock
    PushNotificationService pushNotificationService;
    @InjectMocks
    CommentEventMessageServiceImpl service;

    List<Comment> fixtureComments=new ArrayList<>();
    Article fixtureArticle;
    SimpleKhumuUser fixtureAuthor;
    @BeforeEach
    void setUp() {
        fixtureAuthor = new SimpleKhumuUser("jinsu");
        fixtureArticle = new Article(1L, fixtureAuthor);
        when(articleRepository.get(anyLong())).thenReturn(fixtureArticle);

        fixtureComments.clear();
        // 최대한 Comment server가 redis에 publish한 message 형태 그대로.
        Comment c1 = new Comment();
        c1.setId(1L);
        c1.setAuthor(fixtureAuthor);
        // comment server는 redis에 articleObj가 아닌 articleId만을 전달한다.
        c1.setArticleId(fixtureArticle.getId());
        fixtureComments.add(c1);

        Comment c2 = new Comment("admin");
        c2.setId(2L);
        c2.setAuthorObj(new SimpleKhumuUser("admin"));
        c2.setArticleId(fixtureArticle.getId());

        fixtureComments.add(c2);

        when(commentRepository.listFromArticle(anyLong())).thenReturn(fixtureComments);
        when(notificationRepository.create(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * jinsu의 게시물에 jinsu와 admin이 댓글 존재.
     * jinsu가 댓글 생성.
     * jinsu는 댓글을 방금 단 당사자니까 제외. admin에게만 알림 발송
     */
    @Test
    void createNotifications_올바른_Recipient_Article과_Comment에_같은_Author() {
        String desiredRecipientUsername = "admin";
        // fixture들은 article 1L을 사용하도록 되어있음.
        Comment c = new Comment("jinsu");
        c.setArticleObj(fixtureArticle);
        List<Notification> notifications = service.createNotifications(new EventMessageDto<Comment>("comment", "create", c));
        assertThat(notifications).hasSize(1);
        assertThat(notifications.get(0).getRecipientObj().getUsername()).isEqualTo(desiredRecipientUsername);
    }

    /**
     * somebody의 게시물에 jinsu와 admin이 댓글 존재.
     * somebody가 댓글 작성.
     * somebody는 댓글을 방금 단 당사자니까 제외. jinsu와 admin에게만 알림 발송
     */
    @Test
    void createNotifications_올바른_Recipient_Article과_Comment에_다른_Author() {
        // setup
        // fixture comment들이 바라보는 article을 somebody가 작성한 article로 변경
        fixtureArticle.setAuthorObj(new SimpleKhumuUser("somebody"));
        List<String> desiredRecipientUsernames = new ArrayList<>(Arrays.asList("jinsu", "admin"));
        Comment c = new Comment("somebody");
        c.setArticleObj(fixtureArticle);
        fixtureComments.add(c);

        // test
        List<Notification> notifications = service.createNotifications(new EventMessageDto<Comment>("comment", "create", c));
        for (Notification n : notifications) {
            assertThat(desiredRecipientUsernames).contains(n.getRecipientObj().getUsername());
        }
    }
}