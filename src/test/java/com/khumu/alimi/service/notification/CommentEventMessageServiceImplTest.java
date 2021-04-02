//package com.khumu.alimi.service.notification;
//
//import com.khumu.alimi.data.dto.CommentDto;
//import com.khumu.alimi.data.dto.EventMessageDto;
//import com.khumu.alimi.data.entity.*;
//import com.khumu.alimi.fixture.Fixture;
//import com.khumu.alimi.mapper.CommentMapper;
//import com.khumu.alimi.repository.*;
//import com.khumu.alimi.service.push.PushNotificationService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.mock.mockito.SpyBean;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@ExtendWith(SpringExtension.class)
//@Transactional
//@ActiveProfiles({"test"})
//class CommentEventMessageServiceImplTest {
//    @MockBean
//    BoardRepository boardRepository;
//    @MockBean
//    KhumuUserRepository khumuUserRepository;
//    @MockBean
//    NotificationRepository notificationRepository;
//    @MockBean
//    CommentRepository commentRepository;
//    @MockBean
//    ArticleRepository articleRepository;
//    @MockBean
//    PushNotificationService pushNotificationService;
//    @SpyBean
//    CommentEventMessageServiceImpl service;
//    @SpyBean
//    CommentMapper commentMapper;
//
//    List<Comment> fixtureComments=new ArrayList<>();
//    Article fixtureArticle;
//    Board fixtureBoard;
//    SimpleKhumuUser fixtureUserJinsu;
//    SimpleKhumuUser fixtureUserPenguin;
//    SimpleKhumuUser fixtureUserKitty;
//    @BeforeEach
//    void setUp() {}
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    /**
//     * jinsu의 게시물에 jinsu와 penguin이 댓글 존재.
//     * jinsu가 댓글 생성.
//     * jinsu는 댓글을 방금 단 당사자니까 제외. penguin에게만 알림 발송
//     */
//    @Test
//    void createNotifications_올바른_Recipient_Article과_Comment에_같은_Author() {
//        Comment penguinComment = commentRepository.save(Fixture.provideDefaultComment(fixtureArticle, fixtureUserPenguin));
//        Comment jinsuComment = commentRepository.save(Fixture.provideDefaultComment(fixtureArticle, fixtureUserJinsu));
//
//        List<Notification> notifications = service.createNotifications(new EventMessageDto<CommentDto>("comment", "create", commentMapper.toDto(jinsuComment)));
//        assertThat(notifications).hasSize(1);
//        assertThat(notifications.get(0).getRecipientObj().getUsername()).isEqualTo(fixtureUserJinsu.getUsername());
//    }
//
//    /**
//     * somebody의 게시물에 jinsu와 admin이 댓글 존재.
//     * somebody가 댓글 작성.
//     * somebody는 댓글을 방금 단 당사자니까 제외. jinsu와 admin에게만 알림 발송
//     */
////    @Test
////    void createNotifications_올바른_Recipient_Article과_Comment에_다른_Author() {
////        // setup
////        // fixture comment들이 바라보는 article을 somebody가 작성한 article로 변경
////        fixtureArticle.setAuthorObj(new SimpleKhumuUser("somebody"));
////        List<String> desiredRecipientUsernames = new ArrayList<>(Arrays.asList("jinsu", "admin"));
////        Comment c = new Comment("somebody");
////        c.setArticleObj(fixtureArticle);
////        fixtureComments.add(c);
////
////        // test
////        List<Notification> notifications = service.createNotifications(new EventMessageDto<Comment>("comment", "create", c));
////        for (Notification n : notifications) {
////            assertThat(desiredRecipientUsernames).contains(n.getRecipientObj().getUsername());
////        }
////    }
//}