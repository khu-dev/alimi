package com.khumu.alimi.service.push;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.khumu.alimi.AlimiApplication;
import com.khumu.alimi.FireBaseAdminTest;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.PushSubscription;
import com.khumu.alimi.data.entity.SimpleKhumuUser;
import com.khumu.alimi.repository.PushSubscriptionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class) // application
// value 삽입을 위함.
@ActiveProfiles("test")
@TestPropertySource(locations= {"classpath:application-test.properties"})
class PushNotificationServiceImplTest {
    @MockBean
    PushSubscriptionRepository pushSubscriptionRepository;
    PushNotificationServiceImpl pushNotificationService;

    @Value("${firebase.credential.path}")
    String credentialPath;
    @Value("${firebase.credential.database-url}")
    String firebaseDBUrl;
    @Value("${firebase.credential.default-device-token}")
    String defaultDeviceToken;

    FirebaseApp app;
    @BeforeEach
    void setUp() throws IOException {
        pushNotificationService = new PushNotificationServiceImpl(pushSubscriptionRepository);
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(credential))
//                .setDatabaseUrl(firebaseDBUrl)
//                .build();
        InputStream credential = AlimiApplication.class.getResourceAsStream(credentialPath);
        assertThat(credential).isNotNull();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(credential))
                .setDatabaseUrl(firebaseDBUrl)
                .build();
        app = FirebaseApp.initializeApp(options);
        when(pushSubscriptionRepository.listByUsername(anyString())).thenReturn(
                new ArrayList<>(Arrays.asList(new PushSubscription(
                        defaultDeviceToken,
                        SimpleKhumuUser.builder().username("jinsu").build()
                )))
        );
    }

    @AfterEach
    void tearDown() {
        app.delete();
    }

//    @Test
//    void executeNotify() {
//        Notification n = new Notification();
//        n.setRecipient("jinsu");
//        n.setRecipientObj(SimpleKhumuUser.builder().username("jinsu").build());
//        n.setKind("커뮤니티");
//        n.setTitle("새로운 댓글이 생성되었습니다.");
//        n.setContent("TDD를 진행 중입니다. 더 잘 진행하는 방법은 무엇일까요?");
//        pushNotificationService.executeNotify(n);
//    }

    @Test
    void 그냥_단순_푸시_테스트() {
        assertDoesNotThrow(() -> {
            FirebaseMessaging.getInstance().send(
                    Message.builder()
                            .setToken(defaultDeviceToken)
                            .setAndroidConfig(AndroidConfig.builder()
                                    .setNotification(
                                            AndroidNotification.builder()
                                                    .setTitle("단순 푸시 테스트 알림")
                                                    .setBody("두둥등장")
                                                    .build())
                                    .build())
                            .build()
            );
        });

    }
}