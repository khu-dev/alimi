package com.khumu.alimi;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FireBaseConfig {
    @Value("${firebase.credential.pathType}")
    // firebase credential을 절대 경로로 가져올 것인지 class path로 가져올 것인지
    String FIREBASE_CREDENTIAL_PATH_TYPE;
    @Value("${firebase.credential.classPath}")
    // firebase credential을 class path로 이용하는 경우 그 class path
    String FIREBASE_CREDENTIAL_CLASS_PATH;
    @Value("${firebase.credential.absolutePath}")
    // firebase credential을 절대 경로로 이용하는 경우 그 절대 경로
    String FIREBASE_CREDENTIAL_ABSOLUTE_PATH;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream inputStream = null;
        // jar 빌드 이후 따로 config file을 전달하려는 경우는 절대 경로를 이용해 file로 불러와야한다.
        if (FIREBASE_CREDENTIAL_PATH_TYPE.equals("absolute")) {
            log.info("FIREBASE_CREDENTIAL_PATH_TYPE: absolute");
            inputStream = new FileInputStream(FIREBASE_CREDENTIAL_ABSOLUTE_PATH);
        } else {
            log.info("FIREBASE_CREDENTIAL_PATH_TYPE: classPath 로 처리");
            ClassPathResource classPathResource = new ClassPathResource(FIREBASE_CREDENTIAL_CLASS_PATH);
            inputStream = classPathResource.getInputStream();
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
//                .setDatabaseUrl("")
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(options);
        // 나중에 bean destroy 시에 app.delete를 해주는 게 좋긴 할 듯.
        // 한 App 내에 default app이
        return app;
    }

    @Bean
    public FirebaseMessaging firebaseMessage(FirebaseApp app) {
        // app 이 생긴 뒤에 messaging instance를 얻을 수 있으므로
        // app을 인자로 받게 함.
        return FirebaseMessaging.getInstance();
    }
}
