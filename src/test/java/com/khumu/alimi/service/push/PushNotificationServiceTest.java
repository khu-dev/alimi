package com.khumu.alimi.service.push;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.*;
import com.khumu.alimi.FireBaseConfig;
import com.khumu.alimi.data.entity.Notification;
import com.khumu.alimi.data.entity.PushDevice;
import com.khumu.alimi.external.push.FcmPushManager;
import com.khumu.alimi.repository.PushDeviceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
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
class PushNotificationServiceTest {
    @MockBean
    PushDeviceRepository pushDeviceRepository;
    @SpyBean
    FirebaseApp firebaseApp;
    @InjectMocks
    FcmPushManager pushManager;

    @Value("${firebase.credential.default-device-token}")
    String defaultDeviceToken;

    @BeforeEach
    void setUp() throws IOException {
        when(pushDeviceRepository.listByUsername(anyString())).thenReturn(
                new ArrayList<>(Arrays.asList(new PushDevice(
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