package com.khumu.alimi.service.push;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.khumu.alimi.AlimiApplication;
import com.khumu.alimi.FireBaseAdminTest;
import com.khumu.alimi.FireBaseConfig;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.PushSubscription;
import com.khumu.alimi.data.dto.SimpleKhumuUserDto;
import com.khumu.alimi.external.push.PushManager;
import com.khumu.alimi.external.push.PushManagerImpl;
import com.khumu.alimi.repository.PushSubscriptionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.FileInputStream;
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
// 또 다른 Configuration을 사용하기 위함.
@ContextConfiguration(classes= FireBaseConfig.class)
@TestPropertySource(locations= {"classpath:application-test.properties"})
class PushNotificationServiceImplTest {
    @MockBean
    PushSubscriptionRepository pushSubscriptionRepository;
    @SpyBean
    FirebaseApp firebaseApp;
    @InjectMocks
    PushManagerImpl pushManager;

    @Value("${firebase.credential.default-device-token}")
    String defaultDeviceToken;

    @BeforeEach
    void setUp() throws IOException {
        when(pushSubscriptionRepository.listByUsername(anyString())).thenReturn(
                new ArrayList<>(Arrays.asList(new PushSubscription(
                        defaultDeviceToken,
                        "bo314"
                )))
        );
    }

    @AfterEach
    void tearDown() {
//        firebaseApp.delete();
    }

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

    @Test
    void executeNotify() {
        pushManager.notify(Notification.builder()
                .recipient("bo314")
                .title("Execute notify")
                .content("내용")
                .build(), defaultDeviceToken
        );
    }
}